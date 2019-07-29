package club.xyes.zkh.retail.web.commons.vo;

import club.xyes.zkh.retail.service.encrypt.impl.AccessTokenEncoderImpl;
import org.junit.Test;

import javax.servlet.http.Cookie;

/**
 * Create by 郭文梁 2019/5/24 0024 10:59
 * UserLoginCookieTest
 * 测试用户授权逻辑
 *
 * @author 郭文梁
 * @data 2019/5/24 0024
 */
public class UserLoginCookieTest {
    @Test
    public void test() {
        AccessTokenEncoderImpl encoder = new AccessTokenEncoderImpl();
        encoder.init();

        UserLoginCookie.UserInfo userInfo = new UserLoginCookie.UserInfo();
        userInfo.setUserId(1);

        UserLoginCookie cookie = new UserLoginCookie(userInfo, encoder);

        String cookieValue = cookie.getValue();

        Cookie reqCookie = new Cookie("abc", cookieValue);

        UserLoginCookie result = UserLoginCookie.readFromCookie(reqCookie, encoder);

        System.out.println(result.getUserInfo());
    }
}