package com.maven.cindy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.maven.cindy.pojo.User;
import com.maven.cindy.service.UserService;

@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	/**
	 * 根据用户名获取登录用户信息
	 * 
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername");
		User user = userService.queryUserByUsername(username);
		System.out.println("loadUserByUsername end");
		if (user == null) {
			throw new UsernameNotFoundException("用户名不存在！");
		}
		return user;
	}
}
