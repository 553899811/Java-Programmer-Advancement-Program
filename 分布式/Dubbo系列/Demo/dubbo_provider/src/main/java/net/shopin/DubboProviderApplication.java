package net.shopin;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * <p>ClassName:DubboProviderApplication</p>
 * <p>Description:	</p>
 * <p>Company: www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0
 * @date 2018/6/6 11:41
 */

@SpringBootApplication
@DubboComponentScan("net.shopin.service.impl")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DubboProviderApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DubboProviderApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }
}
