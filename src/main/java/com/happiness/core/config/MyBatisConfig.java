package com.happiness.core.config;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig {
    
    org.apache.ibatis.session.Configuration c = new org.apache.ibatis.session.Configuration();

    private static final String MYBATIS_CONFIG = "mybatis.xml";
    private static final String MAPPER_BASE_PACKAGE = "com.happiness.db.mapper";
    private static final String MAPPER_LOCATIONS = "classpath*:com/happiness/db/sql/*.xml";
    private static final String SQL_SESSION_FACTORY_NAME = "sqlSessionFactory";
    
    @Bean(name = SQL_SESSION_FACTORY_NAME)
    public SqlSessionFactoryBean sqlSessionFactoryBean(DruidDataSource dataSource, ServletContext servletContext) throws IOException {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        
        Resource configLocation = new ClassPathResource(MYBATIS_CONFIG);
        sqlSessionFactory.setConfigLocation(configLocation);
        
        ServletContextResourcePatternResolver resourceResolver = new ServletContextResourcePatternResolver(
                servletContext);
        
        Resource[] mapperLocations = resourceResolver.getResources(MAPPER_LOCATIONS);
        sqlSessionFactory.setMapperLocations(mapperLocations);
        return sqlSessionFactory;
    }
    
    @Bean
    public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(MAPPER_BASE_PACKAGE);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName(SQL_SESSION_FACTORY_NAME);
        return mapperScannerConfigurer;
    }

}
