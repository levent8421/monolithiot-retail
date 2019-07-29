package club.xyes.zkh.retail.commons.vo;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Create by 郭文梁 2019/6/12 0012 15:59
 * TimeRange
 * 时间区间
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Data
public class TimeRange {
    /**
     * 开始时间
     */
    @JsonFormat(pattern = ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND)
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND)
    private Date endTime;
}
