package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.DailyLogin;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Create By leven ont 2019/7/29 22:33
 * Class Name :[DailyLoginMapper]
 * <p>
 * 登陆记录相关数据库访问组件
 *
 * @author leven
 */
@Repository
public interface DailyLoginMapper extends AbstractMapper<DailyLogin> {
    /**
     * 通过用户和日期查询数量
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 数量
     */
    int countByUserAndDate(@Param("userId") Integer userId,
                           @Param("date") Date date);

    /**
     * 通过用户和日期区间查询用户登录记录
     *
     * @param userId 用户ID
     * @param start  开始时间
     * @param end    结束时间
     * @return DailyLogin List
     */
    List<DailyLogin> selectByUserAndDateRange(@Param("userId") Integer userId,
                                              @Param("start") Date start,
                                              @Param("end") Date end);
}
