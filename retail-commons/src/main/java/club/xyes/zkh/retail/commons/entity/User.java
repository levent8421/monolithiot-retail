package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Create by 郭文梁 2019/5/16 0016 16:40
 * User
 * 用户实体类
 *
 * @author 郭文梁
 * @data 2019/5/16 0016
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_user")
public class User extends AbstractEntity {
    /**
     * 角色：普通用户
     */
    public static final int ROLE_USER = 0x01;
    /**
     * 角色：普通推手
     */
    public static final int ROLE_PROMOTERS = 0x02;
    /**
     * 角色：队长（达人）
     */
    public static final int ROLE_CAPTAIN = 0x03;
    /**
     * 初级队长
     */
    public static final int LEVEL_PRIMARY = 0x01;
    /**
     * 中级队长
     */
    public static final int LEVEL_MIDDLE = 0x02;
    /**
     * 高级队长
     */
    public static final int LEVEL_SENIOR = 0x03;
    /**
     * 微信昵称
     */
    @Column(name = "nickname")
    private String nickname;
    /**
     * 真实姓名(收货姓名，订单自动填写姓名)
     */
    @Column(name = "name")
    private String name;
    /**
     * 电话号码（订单自动填写电话号码）
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 微信OpenId
     */
    @Column(name = "wx_open_id")
    private String wxOpenId;
    /**
     * 微信头像地址
     */
    @Column(name = "wx_avatar")
    private String wxAvatar;
    /**
     * 角色
     */
    @Column(name = "role", length = 2, nullable = false)
    private Integer role;
    /**
     * 上次活动位置经度
     */
    @Column(name = "position_longitude")
    private String positionLongitude;
    /**
     * 上次活动位置纬度
     */
    @Column(name = "position_latitude")
    private String positionLatitude;
    /**
     * 删词活动位置
     */
    @Column(name = "position")
    private String position;
    /**
     * 直接推广收益
     */
    @Column(name = "direct_income")
    private Integer directIncome;
    /**
     * 团队推广收益
     */
    @Column(name = "team_income")
    private Integer teamIncome;
    /**
     * 积分
     */
    @Column(name = "score", length = 6)
    private Integer score;
    /**
     * 连续签到次数
     */
    @Column(name = "daily_login_times", length = 6)
    private Integer dailyLoginTimes;
    /**
     * 已提现金额
     */
    @Column(name = "withdrawals")
    private Integer withdrawals;
    /**
     * 推广码
     */
    @Column(name = "promo_code")
    private String promoCode;
    /**
     * 达人等级
     */
    @Column(name = "team_header_level")
    private Integer teamHeaderLevel;
    /**
     * 上级达人ID
     */
    @Column(name = "leader_id")
    private Integer leaderId;
    /**
     * 推手ID，即用户第一次下单时使用的返现推手
     */
    @Column(name = "promoter_id", length = 10)
    private Integer promoterId;
    /**
     * 关联的上级达人信息
     */
    private User leader;
    /**
     * 微信AccessTokenJson
     */
    @Column(name = "wx_token_json")
    private String wxTokenJson;
    /**
     * 推广者电话
     */
    @Column(name = "promoter_phone", length = 100)
    private String promoterPhone;
    /**
     * 推广者微信号
     */
    @Column(name = "promoter_wx_no")
    private String promoterWxNo;
    /**
     * 推广者姓名
     */
    @Column(name = "promoter_name")
    private String promoterName;
}
