package club.xyes.zkh.retail.web.front.vo;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Create by 郭文梁 2019/6/4 0004 14:38
 * DateVo
 * 接受Date类型的参数
 *
 * @author 郭文梁
 * @data 2019/6/4 0004
 */
@Data
public class DateVo {
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date date;
    @JsonFormat(pattern = ApplicationConstants.TIME_FORMAT)
    private Date time;
    @JsonFormat(pattern = ApplicationConstants.DATE_TIME_FORMAT)
    private Date dateTime;
}
