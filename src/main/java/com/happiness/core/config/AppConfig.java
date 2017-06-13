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
 * @PropertySource(value = { ��classpath:config.properties�� })ע�������
 * ��application.properties�ļ��ж�������Զ�Spring Envirronment bean���ã�
 * Environment�ӿ��ṩ��getter������ȡ����������ֵ
 * 
 * ·��д��ʵ����
 * 1��@PropertySource(value = { "classpath:dev/config.properties" })�����ļ�
 * 2��@PropertySource(value = { "classpath:dev/config.properties","classpath:dev/config2.properties" })����
 * 3��@PropertySource(value = { "classpath:${spring.profiles.active:dev}/config.properties" }) 
 *              ${spring.profiles.active:dev}��web.xml�����õ�ֵ spring.profiles.active��ֵ����ֵ��ֵ�Ļ��ã������ֵ
 */
@Configuration
@PropertySource(value = { "classpath:dev/config.properties","classpath:dev/config2.properties" })
public class AppConfig {

    /*
     * ���bean��Ҫ���ڽ��@value��ʹ�õ�${��}ռλ��
     * �����㲻ʹ��${��}ռλ���Ļ������Բ�ʹ�����bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Profile({ "dev" })
    @Bean(name = "appProperties")
    public PropertiesFactoryBean devPropertiesFactoryBean() throws IOException {
        System.out.println("=========================================================");
        System.out.println("����dev");
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
        System.out.println("����pro");
        System.out.println("=========================================================");
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        Resource location = new ClassPathResource("production/config.properties");
        bean.setLocations(location);
        bean.setSingleton(true);
        return bean;
    }
}
