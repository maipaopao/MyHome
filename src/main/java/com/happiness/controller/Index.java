package com.happiness.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.happiness.db.entity.UserInfo;
import com.happiness.db.mapper.UserInfoMapper;

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
    @Value("${Profile.pppas2:123456}")
    private String pppas2;
    
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource( name="valueOps" )
    private ValueOperations<String, Object> valueOperations;
    
    @RequestMapping("/index")
    public String index(){
        
        System.out.println(appkey);
        System.out.println(secret);
        
        return "index";
    }
    
    @RequestMapping("/login")
    @ResponseBody
    public String login(){
    	
    	UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
    	System.out.println( userInfo.getNickName() );
    	
//    	redisTemplate.renameIfAbsent("age", "agenum");
//    	valueOperations.set("userInfo", userInfo);
    	String jsonStr = valueOperations.get("userInfo").toString();
    	return jsonStr;
    }
    
}
