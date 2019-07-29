package club.xyes.zkh.retail.web.front.vo;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Create by 郭文梁 2019/7/6 0006 11:00
 * AppointDateParam
 * 预约日期参数
 *
 * @author 郭文梁
 * @data 2019/7/6 0006
 */
@Data
public class AppointDateParam {
    /**
     * 日期
     */
    @DateTimeFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date date;
}
