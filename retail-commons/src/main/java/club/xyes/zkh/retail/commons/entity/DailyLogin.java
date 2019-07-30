package club.xyes.zkh.retail.commons.entity;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create By leven ont 2019/7/29 22:18
 * Class Name :[DailyLogin]
 * <p>
 * 每日签到记录
 *
 * @author leven
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_daily_login")
public class DailyLogin extends AbstractEntity {
    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 10, nullable = false)
    private Integer userId;
    /**
     * 关联的用户对象
     */
    private User user;
    /**
     * 登录时间（仅保存日期）
     */
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    @Column(name = "login_date", nullable = false)
    private Date loginDate;
}
