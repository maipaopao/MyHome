package com.happiness.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

@Configuration
public class ViewConfiguration {

    @Bean
    public ViewResolver urlBasedViewResolver() {
        UrlBasedViewResolver viewResolver;
        viewResolver = new UrlBasedViewResolver();
        viewResolver.setOrder(2);
        viewResolver.setPrefix("/WEB-INF/page/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        // for debug envirment
        viewResolver.setCache(false);
        return viewResolver;
    }
//    @Bean
//    public ViewResolver tilesViewResolver() {
//        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
//        urlBasedViewResolver.setOrder(1);
//        urlBasedViewResolver.setViewClass(TilesView.class);
//        //urlBasedViewResolver.
//        return urlBasedViewResolver;
//    }
//    @Bean
//    public TilesConfigurer tilesConfigurer() {
//        TilesConfigurer tilesConfigurer = new TilesConfigurer();
//        tilesConfigurer.setDefinitions(new String[] { "classpath:tiles.xml" });
//        return tilesConfigurer;
//    }
}
