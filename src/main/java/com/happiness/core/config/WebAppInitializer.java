package com.happiness.core.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import ch.qos.logback.ext.spring.web.LogbackConfigListener;

public class WebAppInitializer implements WebApplicationInitializer {
    
    private static final long MAX_FILE_UPLOAD_SIZE = 1024 * 1024 * 5; // 5 Mb
    
    private static final int FILE_SIZE_THRESHOLD = 1024 * 1024; // After 1Mb
   
    private static final long MAX_REQUEST_SIZE = -1L; // No request size limit

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //springmvc上下文 
//        AnnotationConfigWebApplicationContext springMvcContext = new AnnotationConfigWebApplicationContext();
//        springMvcContext.register(MvcConfiguration.class);
        AnnotationConfigWebApplicationContext springMvcContext = createWebContext(MvcConfiguration.class, ViewConfiguration.class);
//        AnnotationConfigWebApplicationContext springMvcContext = createWebContext(MvcConfiguration.class);
        
        //加载日志文件，日志路径配置在web.xml
        LogbackConfigListener logbackConfigListener = new LogbackConfigListener();
        servletContext.addListener(logbackConfigListener);
        
        //DispatcherServlet  
        DispatcherServlet dispatcherServlet = new DispatcherServlet(springMvcContext);  
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcherServlet", dispatcherServlet);  
        dynamic.setLoadOnStartup(1);  
        dynamic.addMapping("/");
//        dynamic.setMultipartConfig(new MultipartConfigElement(null, MAX_FILE_UPLOAD_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
        
        //CharacterEncodingFilter   字符集过滤器解决项目中出现的中文乱码问题
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
        FilterRegistration.Dynamic filterDynamic = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        filterDynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
//        FilterRegistration filterRegistration = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
//        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }
    
    private AnnotationConfigWebApplicationContext createWebContext(Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(annotatedClasses);
     
        return webContext;
    }

}
