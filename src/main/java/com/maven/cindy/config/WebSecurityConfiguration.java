package com.maven.cindy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.maven.cindy.security.CustomDaoAuthenticationProvider;
import com.maven.cindy.security.JwtAuthenticationEntryPoint;
import com.maven.cindy.security.JwtAuthorizationTokenFilter;
import com.maven.cindy.security.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	// Custom JWT based security filter
	@Autowired
	JwtAuthorizationTokenFilter authenticationTokenFilter;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.route.authentication.path}")
	private String authenticationPath;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false);
		provider.setUserDetailsService(jwtUserDetailsService);
		return provider;
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(jwtUserDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.anonymous().disable()
				// we don't need CSRF because our token is invulnerable
				.csrf().disable()

				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

				// don't create session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().anyRequest().authenticated();

		httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		// disable page caching
		httpSecurity.headers().frameOptions().sameOrigin() // required to set
															// for H2 else H2
															// Console will be
															// blank.
				.cacheControl();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// AuthenticationTokenFilter will ignore the below paths
		web.ignoring().antMatchers(authenticationPath, "/hello");
	}

}
