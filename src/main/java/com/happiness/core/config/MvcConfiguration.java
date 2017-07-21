package com.happiness.core.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.happiness.interceptor.LoggingInterceptor;
import com.happiness.interceptor.PathInterceptor;

@Configuration
@EnableWebMvc//启动SpringMVC
@ComponentScan("com.happiness")//启动组件扫描
public class MvcConfiguration extends WebMvcConfigurerAdapter{
	
	private final static String DEFAULT_CHARSET_VALUE = "UTF-8";
    private final static Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_VALUE);

    private final static MediaType TEXT_PLAIN_UTF8 = new MediaType("text", "plain", DEFAULT_CHARSET);
    private final static MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", DEFAULT_CHARSET);

    /**
     *  配置静态资源的处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // 对静态资源的请求转发到容器缺省的servlet，而不使用DispatcherServlet
        configurer.enable();
    }
    
    /**
     * 如果项目的一些资源文件放在/WEB-INF/resources/下面
     * 在浏览器访问的地址就是类似：http://host:port/projectName/WEB-INF/resources/xxx.css
     * 但是加了如下定义之后就可以这样访问：
     * http://host:port/projectName/resources/xxx.css
     * 非必须
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/")
        .setCacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES).cachePublic());
    }
    
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        // StringHttpMessageConverter 配置
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter());
        converters.add(new AtomFeedHttpMessageConverter());
        converters.add(new RssChannelHttpMessageConverter());
        // 设置String类型返回Content-Type:text/plain;charset=UTF-8、Content-Type:text/html;charset=UTF-8
        //处理responsebody中文乱码
        StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter(DEFAULT_CHARSET);
        stringMessageConverter.setWriteAcceptCharset(false);
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(TEXT_PLAIN_UTF8);
        types.add(MediaType.APPLICATION_JSON_UTF8);
        types.add(TEXT_HTML_UTF8);
        stringMessageConverter.setSupportedMediaTypes(types);
        converters.add(stringMessageConverter);
        converters.add(mappingJackson2HttpMessageConverter());

    }

    @Bean
    public PathInterceptor initPathInterceptor() {
        PathInterceptor path = new PathInterceptor();
        Map<String, String> secMap = new HashMap<String, String>();
        
        secMap.put("/login", "login");
        
        path.setSecMap(secMap);
        return path;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initPathInterceptor());
        registry.addInterceptor(new LoggingInterceptor());
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        PageableHandlerMethodArgumentResolver methodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(methodArgumentResolver);
    }
}
