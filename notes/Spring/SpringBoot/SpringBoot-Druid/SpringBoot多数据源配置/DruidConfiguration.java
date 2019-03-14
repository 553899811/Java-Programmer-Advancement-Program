//package net.shopin.oms.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.util.Properties;
//
///**
// * <p>ClassName:  </p>
// * <p>Description:Druid数据源配置信息 </p>
// * <p>Company: http://www.shopin.net</p>
// *
// * @author zhangyong@shopin.cn
// * @version 1.0.0
// * @date 2018/7/16 15:45
// */
//@Configuration
//public class DruidConfiguration {
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//
//    @Value("${spring.datasource.removeAbandoned}")
//    private boolean removeAbandoned;
//
//    @Value("${spring.datasource.removeAbandonedTimeoutMillis}")
//    private int removeAbandonedTimeoutMillis;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//    @Value("${spring.datasource.connectionProperties}")
//    private Properties connectionProperties;
//
//    @Bean("dataSource")
//    @Primary
//    public DataSource dataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(url);
//        druidDataSource.setUsername(username);
//        druidDataSource.setPassword(password);
//        druidDataSource.setDriverClassName(driverClassName);
//        // druid配置
//        druidDataSource.setInitialSize(initialSize);
//        druidDataSource.setMaxActive(maxActive);
//        druidDataSource.setRemoveAbandoned(removeAbandoned);
//        druidDataSource.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        druidDataSource.setValidationQuery(validationQuery);
//        druidDataSource.setTestWhileIdle(testWhileIdle);
//        druidDataSource.setTestOnBorrow(testOnBorrow);
//        druidDataSource.setTestOnReturn(testOnReturn);
//
//        try {
//            druidDataSource.setFilters(filters);
//        } catch (SQLException e) {
//            System.err.println("druid configuration initialization filter: " + e);
//        }
//        druidDataSource.setConnectProperties(connectionProperties);
//        return druidDataSource;
//    }
//}
