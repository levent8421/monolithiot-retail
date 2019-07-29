package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/18 0018 10:34
 * UserMapper
 * 用户相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Repository
public interface UserMapper extends AbstractMapper<User> {
    /**
     * 通过用户名或昵称搜索
     *
     * @param name 搜索字段
     * @return User list
     */
    List<User> searchByNameOrNickName(@Param("name") String name);
}
