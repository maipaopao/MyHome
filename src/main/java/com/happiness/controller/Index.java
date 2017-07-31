package com.happiness.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.happiness.db.entity.UserInfo;
import com.happiness.db.entity.mongodb.User;
import com.happiness.db.mapper.UserInfoMapper;
import com.happiness.db.mapper.repository.UserRepository;
import com.happiness.service.IUsersService;

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
    @Resource
    private UserRepository userRepository;
    
    @Resource
    private IUsersService usersService;
    
    @RequestMapping("/index")
    public String index(){
        
        System.out.println(appkey);
        System.out.println(secret);
        
        return "index";
    }
    
//    @RequestMapping(value="/login", produces = "application/json; charset=utf-8")
    @RequestMapping(value="/login")
    @ResponseBody
    public String login(){
    	
    	UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
    	System.out.println( userInfo.getNickName() );
    	
//    	redisTemplate.renameIfAbsent("ageNum", "ageNum");
    	valueOperations.set("userInfo", userInfo);
    	String jsonStr = valueOperations.get("userInfo").toString();
    	User u = userRepository.findByPwd(userInfo.getPwd());
    	if( null == u ){
    		u = new User();
    		u.setPwd(userInfo.getPwd());
    		u.setUserName(userInfo.getUserName());
    		userRepository.insert(u);
    	}
    	
    	
    	return jsonStr;
    }
    
    @RequestMapping(value="/loginr")
    @ResponseBody
    public String loginr(){
    	
    	List<UserInfo> list = usersService.getList();
    	for( UserInfo info : list ){
    		System.out.println(info.getNickName());
    	}
    	
    	return "读操作";
    }
    
    @RequestMapping(value="/loginw")
    @ResponseBody
    public String loginw(){
    	
    	usersService.batchInsert();
    	
    	return "写操作";
    }
    
}
