package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/3 0003 11:02
 * XmlUtils
 * XML相关工具类
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
public class XmlUtils {
    /**
     * 定义类型转换器
     */
    @FunctionalInterface
    interface TypeConverter {
        /**
         * 转换操作
         *
         * @param val 字符串
         * @return 转换结果
         */
        Object convert(String val);
    }

    private static final Map<Class<?>, TypeConverter> CONVERT_MAP = new HashMap<>();

    static {
        CONVERT_MAP.put(String.class, val -> val);
        CONVERT_MAP.put(Integer.class, Integer::parseInt);
        CONVERT_MAP.put(Double.class, Double::parseDouble);
        CONVERT_MAP.put(Float.class, Float::parseFloat);
        CONVERT_MAP.put(Long.class, Long::parseLong);
        CONVERT_MAP.put(Date.class, val -> DateTimeUtils.parse(val, ApplicationConstants.DATE_TIME_FORMAT));
    }

    /**
     * 转换Map为XML字符串
     *
     * @param params map参数
     * @param root   根节点名称
     * @return XML
     */
    public static String asXml(Map<String, String> params, String root) {
        Document document = DocumentHelper.createDocument();
        Element rootEle = document.addElement(root);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Element paramEle = rootEle.addElement(entry.getKey());
            paramEle.addCDATA(entry.getValue());
        }
        return document.asXML();
    }

    /**
     * XML转文档对象
     *
     * @param xml XML字符串
     * @return 文档对象
     * @throws DocumentException 文档转换异常
     */
    public static Document read(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * 转换XML为JAVA对象
     *
     * @param xml   xml
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     * @throws DocumentException 文档转换异常
     */
    public static <T> T parseObject(String xml, Class<T> clazz) throws DocumentException {
        Document document = read(xml);
        Field[] fields = clazz.getDeclaredFields();
        Element rootEle = document.getRootElement();

        T obj;
        try {
            obj = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InternalServerErrorException("Could not install class " + clazz);
        }
        for (Field field : fields) {
            String elementName = getElementName(field);
            Element element = rootEle.element(elementName);
            if (element == null) {
                continue;
            }
            String value = element.getText();
            try {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object fieldValue = convertType(value, type);
                field.set(obj, fieldValue);
            } catch (IllegalAccessException e) {
                throw new InternalServerErrorException("Unable to access field " + field.getName() + " for class " + clazz);
            }
        }
        return obj;
    }

    /**
     * 获取属性对应的节点名称
     *
     * @param field 字段
     * @return 节点名称
     */
    private static String getElementName(Field field) {
        XmlProperty annotation = field.getAnnotation(XmlProperty.class);
        if (annotation == null) {
            return field.getName();
        }
        return annotation.name();
    }

    /**
     * 执行类型转换
     *
     * @param value 被转换字符串
     * @param type  目标类型
     * @return 转换结果
     */
    private static Object convertType(String value, Class<?> type) {
        TypeConverter converter = CONVERT_MAP.get(type);
        if (converter == null) {
            throw new InternalServerErrorException("Unable to convert date " + value + " to type " + type);
        }
        return converter.convert(value);
    }
}
