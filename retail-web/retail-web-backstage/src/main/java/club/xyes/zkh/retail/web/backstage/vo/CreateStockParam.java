package club.xyes.zkh.retail.web.backstage.vo;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.vo.TimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Create by 郭文梁 2019/6/12 0012 15:58
 * CreateStockParam
 * 批量创建库存参数
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Data
public class CreateStockParam {
    /**
     * 开始日期
     */
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date startDate;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date endDate;
    /**
     * 时间段列表
     */
    private List<TimeRange> timeRangeList;
    /**
     * 星期列表
     */
    private List<Integer> daysOfWeek;
    /**
     * 库存量
     */
    private Integer stockCount;
}
