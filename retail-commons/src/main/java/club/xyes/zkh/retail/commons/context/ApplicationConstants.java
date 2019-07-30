package club.xyes.zkh.retail.commons.context;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Create by 郭文梁 2019/4/15 0015 15:03
 * ApplicationConstants
 * 系统常量
 *
 * @author 郭文梁
 * @data 2019/4/15 0015
 */
public class ApplicationConstants {
    /**
     * 访问地址
     */
    public static final String BASE_URL = "http://wxapi.berrontech.com";
    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_TIMEZONE = ZoneId.systemDefault();
    /**
     * 默认地区
     */
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;
    /**
     * 系统默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 默认时间如期格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";
    /**
     * 时间格式 不带秒
     */
    public static final String TIME_FORMAT_WITHOUT_SECOND = "HH:mm";
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 日期格式化 不带年
     */
    public static final String DATE_FORMAT_WITHOUT_YEAR = "MM-dd";
    /**
     * 图片分隔符
     */
    public static final String IMAGE_DELIMITER = ",";
    /**
     * OpenId分隔符
     */
    public static final String OPEN_ID_DELIMITER = ",";
    /**
     * 签到返积分规则
     */
    public static final List<Integer> DAILY_LOGIN_SCORE_LIST = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    /**
     * 购买积分
     */
    public static final int COMMISSION_SCORE = 10;

    /**
     * 上下文 系统常量
     */
    public static class Context {
        /**
         * Mapper组建所在的包名
         */
        public static final String MAPPER_PACKAGE = "club.xyes.zkh.retail.repository.dao.mapper";
        /**
         * 控制器(API)所在的包
         */
        public static final String API_CONTROLLER_PACKAGE = "club.xyes.zkh.retail.web";
        /**
         * 组件所在的基础包
         */
        public static final String COMPONENT_PACKAGE = "club.xyes.zkh.retail";
    }

    public static class Security {
    }

    /**
     * 数据库相关常量
     */
    public static class Database {
        /**
         * JDBC主键生成器名称
         */
        public static final String GENERATOR_JDBC = "JDBC";
        /**
         * 降序排序
         */
        public static final String ORDER_DESC = "desc";
        /**
         * 升序排序
         */
        public static final String ORDER_ASC = "asc";
    }

    /**
     * Http相关常量
     */
    public static class Http {
        /**
         * 内容类型 JSON 编码 UTF-8
         */
        public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=utf-8";
        /**
         * 内容类型 xml 编码 UTF-8
         */
        public static final String CONTENT_TYPE_XML_URT8 = "text/xml;charset=utf-8";
        /**
         * API路径
         */
        public static final String API_BASE_PATH = "/api/**";
        /**
         * 开放授权Api地址
         */
        public static final String WITHOUT_AUTH_PATH = "/api/open/**";
        /**
         * 用户附加参数名称
         */
        public static final String USER_TOKEN_EXTEND_PARAM_NAME = "userToken";
        /**
         * 商家附加参数名称
         */
        public static final String STORE_TOKEN_EXTEND_PARAM_NAME = "storeToken";
        /**
         * 管理员账户附加参数名称
         */
        public static final String ADMIN_TOKEN_EXTEND_PARAM_NAME = "adminToken";
    }

    /**
     * 微信相关常量
     */
    public static class WeChat {
        /**
         * （模板ID）支付成功通知模板消息
         */
        public static final String PAY_NOTIFY_MSG_TEMPLATE_ID = "1nzct9OjkHCEoefiJhd9UtsCIQzpxw1faNADwCMWI3w";
        /**
         * （模板ID）预约成功模板消息ID
         */
        public static final String APPOINTMENT_NOTIFY_MSG_TEMPLATE_ID = "517SVr_usrpbmXbfsGa3TMmlEnc9fXHNB-fzaG24ITg";
        /**
         * （模板ID）会员加入成功通知消息模板
         */
        public static final String JOIN_TEAM_NOTIFY_MSG_TEMPLATE_ID = "nVFttg8na8cwf9ozhF5r93P1h-P1w8i_6EIbF6jWeJc";
        /**
         * （模板ID）返现提醒模板消息
         */
        public static final String COMMISSION_NOTIFY_TEMPLATE_MSG_ID = "u7p2z8ciDCOhmSUhX3BPbLPFjyYgLDOf2qQljLwz4WM";
    }
}
