package com.happiness.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.happiness.db.entity.UserInfo;
import com.happiness.db.mapper.UserInfoMapper;
import com.happiness.service.IUsersService;

@Service
public class UsersServiceImpl implements IUsersService{

	@Resource
	private UserInfoMapper userInfoMapper;
	
	@Override
	@Transactional
	public void batchInsert() {
		for( int i = 0 ; i < 5; i++ ){
			UserInfo info = new UserInfo();
			info.setCreateTime(new Date());
			info.setNickName("昵称" + i);
			info.setPwd(i+"");
			info.setUserName("名称" + i);
			userInfoMapper.insert(info);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserInfo> getList() {
		return userInfoMapper.selectAll();
	}

}
