package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/25 0025 10:13
 * CashApplicationMapper
 * 提现申请相关数据库访问对象
 *
 * @author 郭文梁
 * @data 2019/5/25 0025
 */
@Repository
public interface CashApplicationMapper extends AbstractMapper<CashApplication> {
    /**
     * 通过状态查询提现申请 同时管线查出用户信息
     *
     * @param status 状态
     * @return CashApplication List
     */
    List<CashApplication> selectByStatusWithUser(@Param("status") Integer status);

    /**
     * 通过用户ID查找用户的U最后一条提现记录
     *
     * @param userId 用户ID
     * @return CashApplication
     */
    CashApplication selectLastOneByUserId(@Param("userId") Integer userId);

    /**
     * 搜索提现记录搜索条件（电话 姓名 微信号）
     *
     * @param search 搜索条件（电话 姓名 微信号）
     * @return Application List
     */
    List<CashApplication> search(@Param("search") String search);
}
