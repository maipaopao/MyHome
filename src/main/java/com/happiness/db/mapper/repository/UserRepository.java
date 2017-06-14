package com.happiness.db.mapper.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.happiness.db.entity.mongodb.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByPwd(String pwd);
}
