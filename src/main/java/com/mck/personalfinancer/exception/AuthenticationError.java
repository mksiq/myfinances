package com.mck.personalfinancer.exception;

public class AuthenticationError extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AuthenticationError(String message) {
		super(message);
	}
}
