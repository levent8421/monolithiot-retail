package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.utils.QrCodeUtil;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.spring.annotation.MapperScan;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/18 0018 11:35
 * UserController
 * 用户相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@RestController
@RequestMapping("/api/user")
@MapperScan(ApplicationConstants.Context.MAPPER_PACKAGE)
@Slf4j
public class UserController extends AbstractEntityController<User> {
    private static final String TEAM_QR_CODE_PREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx45725d38c171c33f&redirect_uri=http%3a%2f%2fwz.jinguanjiazhifu.com%2fretail%2fservice%2fapi%2fopen%2fuser%2f";
    private static final String TEAM_QR_CODE_SUFFIX = "%2fjoin&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    private final UserService userService;
    private final StaticConfigProp staticConfigProp;

    @Autowired
    public UserController(UserService userService, StaticConfigProp staticConfigProp) {
        super(userService);
        this.userService = userService;
        this.staticConfigProp = staticConfigProp;
    }

    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     * @return GR
     */
    @GetMapping("/{id}/detail")
    public GeneralResult<User> detail(@PathVariable("id") Integer userId) {
        @NotNull User user = userService.require(userId);
        return GeneralResult.ok(user);
    }

    /**
     * 当前登录的用户信息
     *
     * @return GR
     */
    @GetMapping("/me")
    public GeneralResult<User> currentUser() {
        User user = requireCurrentUser(userService);
        return GeneralResult.ok(user);
    }

    /**
     * 生成团队邀请二维码
     *
     * @return 二维码文件
     */
    @GetMapping("/team-qr-code")
    public ResponseEntity<byte[]> teamQrCode() {
        @NotNull final User user = requireCurrentUser(userService);
        if (!Objects.equals(user.getRole(), User.ROLE_CAPTAIN)) {
            throw new BadRequestException("无组建团队权限");
        }
        String qrCodeContent = TEAM_QR_CODE_PREFIX + user.getId() + TEAM_QR_CODE_SUFFIX;
        final byte[] bytes = createTeamQrCode(qrCodeContent);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }

    private byte[] createTeamQrCode(String content) {
        final String rootPath = staticConfigProp.getStaticFilePath();
        final String filename = staticConfigProp.getTeamQrCodeBg();
        return QrCodeUtil.generateTeamQrCode(content, QR_CODE_WIDTH, QR_CODE_HEIGHT, new File(rootPath + filename));
    }

    /**
     * 获取当前用户的团队成员
     *
     * @return GR
     */
    @GetMapping("/members")
    public GeneralResult<List<User>> members() {
        List<User> res = userService.findByLeader(requireCurrentUser(userService));
        return GeneralResult.ok(res);
    }

    /**
     * 加入团队
     *
     * @param param 参数
     * @return GR
     */
    @PostMapping("/join")
    public GeneralResult<Void> join(@RequestBody User param) {
        checkJoinParam(param);
        @NotNull final User user = requireCurrentUser(userService);
        if (user.getLeaderId() != null) {
            @NotNull final User leader = userService.require(user.getLeaderId());
            throw new BadRequestException("您也是队长[" + leader.getPromoterName() + "]的队员，不能重复加入团队！");
        }
        if (!Objects.equals(user.getRole(), User.ROLE_USER)) {
            throw new BadRequestException("您已经是推手或队长，不能加入团队！");
        }
        @NotNull final User leader = userService.require(param.getLeaderId());
        if (Objects.equals(leader.getId(), user.getId())) {
            throw new BadRequestException("你不能加入自己的团队");
        }
        final String promoterName = param.getPromoterName();
        final String promoterPhone = param.getPromoterPhone();
        final String promoterWxNo = param.getPromoterWxNo();
        user.setPromoterName(promoterName);
        user.setPromoterPhone(promoterPhone);
        user.setPromoterWxNo(promoterWxNo);
        userService.join(user, leader);
        return GeneralResult.ok();
    }

    /**
     * 检查加入团队的请求参数
     *
     * @param param 参数
     */
    private void checkJoinParam(User param) {
        final Class<BadRequestException> ex = BadRequestException.class;
        ParamChecker.notNull(param, ex, "参数未传");
        ParamChecker.notNull(param.getLeaderId(), ex, "团队所有者ID必填！");
        ParamChecker.notNull(param.getPromoterPhone(), ex, "电话号码必填！");
        ParamChecker.notNull(param.getPromoterName(), ex, "姓名必填！");
    }

    /**
     * 获取当前用户的推手
     * 当用户已经存在推手信息是，返回保存推手，
     * 当用户自己就是推手时，返回当前用户
     * 当用户是普通用户，并且没有保存推手信息时 返回推广码对应的推手信息
     *
     * @param promoCode url传入的推广码
     * @return GR
     */
    @GetMapping("/get-promoter")
    public GeneralResult<User> getPromoter(@RequestParam("promo-code") String promoCode) {
        @NotNull final User user = requireCurrentUser(userService);
        User promoter;
        switch (user.getRole()) {
            case User.ROLE_USER:
                if (user.getPromoterId() != null) {
                    promoter = userService.require(user.getPromoterId());
                } else {
                    promoter = userService.findByPromoCode(promoCode);
                }
                break;
            case User.ROLE_PROMOTERS:
            case User.ROLE_CAPTAIN:
                promoter = user;
                break;
            default:
                throw new InternalServerErrorException("Unknown role " + user.getRole() + "for user!");
        }
        return GeneralResult.ok(promoter);
    }
}
