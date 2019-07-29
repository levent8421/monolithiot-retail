package club.xyes.zkh.retail.repository.dao;

import club.xyes.zkh.retail.commons.entity.AbstractEntity;
import tk.mybatis.mapper.common.*;

/**
 * Create by 郭文梁 2019/5/18 0018 10:32
 * AbstractMapper
 * 基础Mapper
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public interface AbstractMapper<T extends AbstractEntity>
        extends Mapper<T>,
        MySqlMapper<T>,
        ExampleMapper<T>,
        IdsMapper<T>,
        ConditionMapper<T> {
}
