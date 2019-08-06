package com.maven.cindy.service;

import org.springframework.stereotype.Service;
import com.maven.cindy.pojo.User;

@Service
public interface UserService {

	 User queryUserByUsername(String username);

}
