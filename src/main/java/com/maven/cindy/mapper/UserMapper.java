package com.maven.cindy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.maven.cindy.pojo.User;

@Mapper
public interface UserMapper {
	
	@Select("SELECT * FROM user where username=#{username}")
	User queryUserByUsername(@Param("username")String username);
	
}
