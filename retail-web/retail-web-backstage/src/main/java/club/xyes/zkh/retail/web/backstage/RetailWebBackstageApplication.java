package club.xyes.zkh.retail.web.backstage;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Create by 郭文梁 2019/5/28 0028 17:21
 * RetailWebBackstageApplication
 * 后台管理员项目启动入口
 *
 * @author 郭文梁
 * @data 2019/5/28 0028
 */
@SpringBootApplication(scanBasePackages = ApplicationConstants.Context.COMPONENT_PACKAGE)
@MapperScan(ApplicationConstants.Context.MAPPER_PACKAGE)
public class RetailWebBackstageApplication {
    /**
     * 启动主方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(RetailWebBackstageApplication.class, args);
    }
}
