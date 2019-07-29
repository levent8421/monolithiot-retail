package club.xyes.zkh.retail.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Create by 郭文梁 2019/4/27 0027 13:05
 * DatetimeRange
 * 时间日期区间类
 *
 * @author 郭文梁
 * @data 2019/4/27 0027
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatetimeRange {
    /**
     * 开始时间
     */
    private Date start;
    /**
     * 结束时间
     */
    private Date end;
}
