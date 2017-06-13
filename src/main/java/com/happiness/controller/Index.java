package com.happiness.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class Index {

    @Value( "${wechat.appkey:123}" )
    private String appkey;
    @Value( "${wechat.secret:123}" )
    private String secret;
    @Value( "${wechat.name:123}" )
    private String name;
    
    @Value("#{appProperties['Profile.pppas']}")
    private String pppas;
    @Value("${Profile.pppas2}")
    private String pppas2;
    
    @RequestMapping("/index")
    public String index(){
        
        System.out.println(appkey);
        System.out.println(secret);
        
        return "index";
    }
    
    @RequestMapping("/login")
    @ResponseBody
    public String login(){
        return "appkey=" + appkey + ",secret=" + secret + ",name=" + name + ",pppas=" + pppas + ",pppas2=" + pppas2;
    }
    
}
