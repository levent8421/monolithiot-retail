package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Create By leven ont 2019/8/8 22:33
 * Class Name :[Feedback]
 * <p>
 * 反馈实体类
 *
 * @author leven
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_feedback")
public class Feedback extends AbstractEntity {
    /**
     * 反馈用户ID
     */
    @Column(name = "user_id", length = 10)
    private Integer userId;
    /**
     * 反馈用户
     */
    private User user;
    /**
     * 反馈用户姓名
     */
    @Column(name = "username", nullable = false)
    private String username;
    /**
     * 联系方式
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 反馈内容
     */
    @Column(name = "content")
    private String content;
}
