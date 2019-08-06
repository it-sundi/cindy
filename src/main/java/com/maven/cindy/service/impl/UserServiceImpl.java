package com.maven.cindy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maven.cindy.mapper.UserMapper;
import com.maven.cindy.pojo.User;
import com.maven.cindy.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User queryUserByUsername(String username){
		System.out.println("queryUserByUsername");
		return userMapper.queryUserByUsername(username);
	}

}
