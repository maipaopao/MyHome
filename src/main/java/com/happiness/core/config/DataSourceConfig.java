package com.happiness.core.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

@Configuration
@PropertySource(value = { "classpath:${spring.profiles.active:dev}/jdbc.properties" })
public class DataSourceConfig {

	@Value("${db.driver}")
    private String dbDriver;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.password}")
    private String dbPassword;
    @Value("${db.initialSize}")
    private String initialSize;
    @Value("${db.maxActive}")
    private String maxActive;
    @Value("${db.timebetweenevictionrunsmillis}")
    private String dbTimeBetweenEvictionRunsMillis;
    
    @Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
    public DruidDataSource dataSource() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        Properties properties = new Properties();
        properties.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dbDriver);
        properties.put(DruidDataSourceFactory.PROP_URL, dbUrl);
        properties.put(DruidDataSourceFactory.PROP_USERNAME, dbUsername);
        properties.put(DruidDataSourceFactory.PROP_PASSWORD, dbPassword);
        properties.put(DruidDataSourceFactory.PROP_INITIALSIZE, initialSize);
        properties.put(DruidDataSourceFactory.PROP_MAXACTIVE, maxActive);
        properties.put(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS, dbTimeBetweenEvictionRunsMillis);
        DruidDataSourceFactory.config(dataSource, properties);
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager tran(DruidDataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }
    
}
