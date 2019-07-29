package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.dto.CountAndAmount;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/3 0003 11:11
 * XmlUtilsTest
 * 测试XL工具类
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Slf4j
public class XmlUtilsTest {

    @Test
    public void asXml() {
        Map<String, String> params = new HashMap<>(4);
        params.put("a", "1");
        params.put("b", "1");
        params.put("C", "1");
        params.put("D", "1");
        String xml = XmlUtils.asXml(params, "xml");
        log.info("xml = [{}]", xml);
    }

    @Test
    public void parseObject() throws DocumentException {
        String xml = "\n" +
                "<xml>\n" +
                "  <count><![CDATA[1]]></count>\n" +
                "  <b><![CDATA[1]]></b>\n" +
                "  <C><![CDATA[1]]></C>\n" +
                "  <D><![CDATA[1]]></D>\n" +
                "</xml>\n";
        CountAndAmount result = XmlUtils.parseObject(xml, CountAndAmount.class);
        log.info("parse = [{}]", result);
    }
}