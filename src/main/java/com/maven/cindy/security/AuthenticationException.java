package com.maven.cindy.security;

public class AuthenticationException extends RuntimeException {
	/**  */
	private static final long serialVersionUID = -2904512203181460256L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
