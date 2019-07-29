package club.xyes.zkh.retail.web.front;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Create by 郭文梁 2019/5/16 0016 15:43
 * RetailWebFrontApplication
 * 前台项目入口
 * 未添加<code>@MapperScan(ApplicationConstants.Context.MAPPER_PACKAGE)</code>的原因是<a href="https://blog.csdn.net/key_xyes/article/details/89382937">链接</a>
 *
 * @author 郭文梁
 * @data 2019/5/16 0016
 */
@SpringBootApplication(scanBasePackages = ApplicationConstants.Context.COMPONENT_PACKAGE)
public class RetailWebFrontApplication {
    /**
     * 启动方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(RetailWebFrontApplication.class, args);
    }
}
