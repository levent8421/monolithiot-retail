package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.commons.utils.RandomUtils;
import club.xyes.zkh.retail.repository.dao.mapper.UserMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.*;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/18 0018 11:39
 * UserServiceImpl
 * 用户相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Service
@Slf4j
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
    private static final int LEVEL_PRIMARY_THRESHOLD = 0;
    private static final int LEVEL_MIDDLE_THRESHOLD = 20;
    private static final int LEVEL_SENIOR_THRESHOLD = 50;
    private static final String LANG = "zh_CN";
    private final UserMapper userMapper;
    private final Wechat wechat;
    private final WechatConfig wechatConfig;

    @Autowired
    public UserServiceImpl(UserMapper mapper, Wechat wechat, WechatConfig wechatConfig) {
        super(mapper);
        this.userMapper = mapper;
        this.wechat = wechat;
        this.wechatConfig = wechatConfig;
    }

    @Override
    public User loginByOAuthCode(String code) {
        String appId = wechatConfig.getAppId();
        String secret = wechatConfig.getSecret();
        WxOAuth2AccessToken accessToken = wechat.code2Token(appId, secret, code);
        User user = findByOpenId(accessToken.getOpenId());
        if (user == null) {
            //说明用户第一次登陆
            User defaultUser = createDefaultUser(accessToken);
            user = save(defaultUser);
        }
        user = refreshUserInfo(user, accessToken);
        return user;
    }

    @Override
    public User findByOpenId(String openId) {
        User query = new User();
        query.setWxOpenId(openId);
        return findOneByQuery(query);
    }

    @Override
    public User refreshUserInfo(User user, WxOAuth2AccessToken accessToken) {
        WxUserInfo userInfo = wechat.getUserInfo(accessToken.getAccessToken(), user.getWxOpenId(), LANG);
        user.setNickname(userInfo.getNickname());
        user.setWxAvatar(userInfo.getHeadimgurl());
        return updateById(user);
    }

    @Override
    public User findByPromoCode(String promoCode) {
        User query = new User();
        query.setPromoCode(promoCode);
        return findOneByQuery(query);
    }

    @Override
    public User toPromoter(User user) {
        user.setRole(User.ROLE_PROMOTERS);
        user.setTeamHeaderLevel(null);
        user.setTeamIncome(0);
        if (user.getPromoCode() == null) {
            String promoCode = randomPromoCode();
            user.setPromoCode(promoCode);
        }
        return updateById(user);
    }

    @Override
    public User toCaptain(User user) {
        user.setRole(User.ROLE_CAPTAIN);
        //默认为初级小队长
        user.setTeamHeaderLevel(User.LEVEL_PRIMARY);
        if (user.getPromoCode() == null) {
            String promoCode = randomPromoCode();
            user.setPromoCode(promoCode);
        }
        return updateById(user);
    }

    private User createDefaultUser(WxOAuth2AccessToken token) {
        User user = new User();
        user.setWxOpenId(token.getOpenId());
        user.setWxTokenJson(token.getSourceJson());
        user.setRole(User.ROLE_USER);
        return user;
    }

    /**
     * 生成随机推广码
     *
     * @return 推广码
     */
    private String randomPromoCode() {
        return RandomUtils.randomPrettyUUIDString();
    }

    @Override
    public List<User> searchByName(String name) {
        String query = String.format(SEARCH_TEMPLATE, name);
        return userMapper.searchByNameOrNickName(query);
    }

    @Override
    public User toPrimaryUser(User user) {
        user.setRole(User.ROLE_USER);
        user.setTeamHeaderLevel(null);
        user.setTeamIncome(0);
        return updateById(user);
    }

    @Override
    public void join(User user, User leader) {
        if (!Objects.equals(leader.getRole(), User.ROLE_CAPTAIN)) {
            throw new BadRequestException("该用户无权邀请推广者");
        }
        user.setRole(User.ROLE_PROMOTERS);
        user.setLeader(leader);
        user.setLeaderId(leader.getId());
        if (user.getPromoCode() == null) {
            final String promoCode = RandomUtils.randomPrettyUUIDString();
            user.setPromoCode(promoCode);
        }
        updateById(user);
        final int memberCount = countByLeader(leader.getId());
        updateTeamHeaderLevel(leader, memberCount);
        sendJoinSuccessNotifyTemplateMsg(user, leader);
    }

    /**
     * 发送加入团队成功的模板消息通知
     *
     * @param user   用户
     * @param leader 上级用户
     */
    private void sendJoinSuccessNotifyTemplateMsg(User user, User leader) {
        val username = user.getPromoterName() + "(" + user.getPromoterPhone() + ")";
        val date = DateTimeUtils.format(DateTimeUtils.now(), ApplicationConstants.DATE_TIME_FORMAT);
        final Map<String, TemplateMsgData> data = new HashMap<>(16);
        data.put("first", new TemplateMsgData("会员邀请加入成功", TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword1", new TemplateMsgData(username, TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword2", new TemplateMsgData(date, TemplateMsgData.DEFAULT_COLOR));
        data.put("remark", new TemplateMsgData("感谢您的使用", TemplateMsgData.DEFAULT_COLOR));

        final WxTemplateMsgParam msgParam = new WxTemplateMsgParam();
        msgParam.setToUser(leader.getWxOpenId());
        msgParam.setTemplateId(ApplicationConstants.WeChat.JOIN_TEAM_NOTIFY_MSG_TEMPLATE_ID);
        msgParam.setData(data);

        final WxTemplateMsgResult sendRes = wechat.sendTemplateMsg(msgParam);
        log.debug("Send join team success notify msg result [{}]!", sendRes);
    }

    @Override
    public List<User> findByLeader(User leader) {
        User query = new User();
        query.setLeaderId(leader.getId());
        return findByQuery(query);
    }

    /**
     * 根据队长统计推手数量
     *
     * @param leaderId 队长ID
     * @return 推手数量
     */
    private int countByLeader(Integer leaderId) {
        User query = new User();
        query.setLeaderId(leaderId);
        return count(query);
    }

    /**
     * 更新队长等级
     *
     * @param leader      队长
     * @param memberCount 成员数量
     */
    private void updateTeamHeaderLevel(User leader, int memberCount) {
        if (memberCount >= LEVEL_SENIOR_THRESHOLD) {
            leader.setTeamHeaderLevel(User.LEVEL_SENIOR);
        } else if (memberCount >= LEVEL_MIDDLE_THRESHOLD) {
            leader.setTeamHeaderLevel(User.LEVEL_MIDDLE);
        } else if (memberCount >= LEVEL_PRIMARY_THRESHOLD) {
            leader.setTeamHeaderLevel(User.LEVEL_PRIMARY);
        }
        updateById(leader);
    }
}
