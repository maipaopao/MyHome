package com.happiness.db.entity.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class User {

	private String id;
	
	private String userName;
	
	private String pwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
