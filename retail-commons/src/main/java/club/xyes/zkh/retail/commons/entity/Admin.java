package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * Create by 郭文梁 2019/6/20 0020 10:04
 * Admin
 * 管理员账户
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@Table(name = "t_admin")
@EqualsAndHashCode(callSuper = true)
@Data
public class Admin extends AbstractEntity {
    /**
     * 管理员登录名
     */
    private String name;
    /**
     * 登录密码
     */
    private String password;
}
