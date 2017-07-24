package com.happiness.service;

import java.util.List;

import com.happiness.db.entity.UserInfo;

public interface IUsersService {

	public void batchInsert();
	
	public List<UserInfo> getList();
}
