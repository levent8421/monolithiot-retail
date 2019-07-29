package club.xyes.zkh.retail.commons.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by 郭文梁 2019/6/3 0003 11:19
 * XmlProperty
 * Xml属性注解
 * 由XML Utils解析
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlProperty {
    /**
     * Tag名称
     *
     * @return Tag名称
     */
    String name();
}
