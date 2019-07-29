package club.xyes.zkh.retail.web.commons.vo;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Stock;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Create by 郭文梁 2019/6/12 0012 17:32
 * DateStock
 * 每日的库存信息
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Data
public class DateStock {
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date date;
    private List<Stock> stocks;
}
