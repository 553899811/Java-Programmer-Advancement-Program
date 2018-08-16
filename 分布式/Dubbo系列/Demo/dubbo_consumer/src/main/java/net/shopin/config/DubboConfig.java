//package net.shopin.config;
//
//import com.alibaba.dubbo.config.ApplicationConfig;
//import com.alibaba.dubbo.config.ConsumerConfig;
//import com.alibaba.dubbo.config.RegistryConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//@Configuration
//public class DubboConfig {
//
//    @Autowired
//    Environment env;
//    @Bean(name = "defaultConsumer")
//    public ConsumerConfig consumerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
//        ConsumerConfig config = new ConsumerConfig();
//        config.setTimeout(new Integer(env.getProperty("spring.dubbo.consumer.timeout").trim()));
//        config.setRetries(new Integer(env.getProperty("spring.dubbo.consumer.retries").trim()));
//        config.setApplication(applicationConfig);
//        config.setRegistry(registryConfig);
//        return config;
//    }
//}
