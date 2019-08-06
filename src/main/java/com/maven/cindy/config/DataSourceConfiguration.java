package com.maven.cindy.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * 单数据源配置
 */
@Configuration
@MapperScan(basePackages = "com.maven.cindy.mapper")
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "jdbc.ds")
	public DataSource readDataSource() {
		return new DruidDataSource();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(readDataSource());
		sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return sqlSessionFactoryBean.getObject();
	}
}
