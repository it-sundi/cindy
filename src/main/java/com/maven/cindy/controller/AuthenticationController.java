package com.maven.cindy.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.maven.cindy.pojo.User;
import com.maven.cindy.security.AuthenticationException;
import com.maven.cindy.security.JwtAuthenticationRequest;
import com.maven.cindy.security.JwtAuthenticationResponse;
import com.maven.cindy.security.JwtTokenUtil;

@RestController
public class AuthenticationController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@RequestMapping("/hello")
	public String helloSprinBoot() {
		return "helloSprinBoot";
	}

	@RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {

		Authentication authentication = authenticate(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());
		// Reload password post-security so we can generate the token
		final User user = (User) authentication.getPrincipal();

		final String token = jwtTokenUtil.generateToken(user);

		// Return the token
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	@ExceptionHandler({ AuthenticationException.class})
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 */
	private Authentication authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return authentication;
		} catch (LockedException e) {
			//throw new AuthenticationException("User is locked!", e);
			throw new AuthenticationException(e.getMessage(), e);
		} catch (UsernameNotFoundException e) {
			//throw new AuthenticationException("user name does not exist!", e);
			throw new AuthenticationException(e.getMessage(), e);
		} catch (BadCredentialsException e) {
			//throw new AuthenticationException("Bad credentials!", e);
			throw new AuthenticationException(e.getMessage(), e);
		}
	}
}
