package com.happiness.core.config;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Administrator
 * @PropertySource(value = { "classpath:config.properties" })注解可以让在
 * config.properties文件中定义的属性对Spring Envirronment bean可用
 * Environment接口提供了getter方法读取单独的属性值
 * 
 * 写法示例
 * 1、@PropertySource(value = { "classpath:dev/config.properties" })单文件
 * 2、@PropertySource(value = { "classpath:dev/config.properties","classpath:dev/config2.properties" })多文件
 * 3、@PropertySource(value = { "classpath:${spring.profiles.active:dev}/config.properties" }) 
 *              ${spring.profiles.active:dev}在web.xml中spring.profiles.active配置
 */
@Configuration
@PropertySource(value = { "classpath:${spring.profiles.active:dev}/config.properties" })
public class AppConfig {

    /*
     * 这个bean主要用于解决@value中使用的${…}占位符
     * 假如你不使用${…}占位符的话，可以不使用这个bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Profile({ "dev" })
    @Bean(name = "appProperties")
    public PropertiesFactoryBean devPropertiesFactoryBean() throws IOException {
        System.out.println("=========================================================");
        System.out.println("启用dev");
        System.out.println("=========================================================");
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        Resource location = new ClassPathResource("dev/config.properties");
        bean.setLocations(location);
        bean.setSingleton(true);
        return bean;
    }
    
    @Profile({ "production" })
    @Bean(name = "appProperties")
    public PropertiesFactoryBean productionPropertiesFactoryBean() throws IOException {
        System.out.println("=========================================================");
        System.out.println("启用pro");
        System.out.println("=========================================================");
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        Resource location = new ClassPathResource("production/config.properties");
        bean.setLocations(location);
        bean.setSingleton(true);
        return bean;
    }
}
