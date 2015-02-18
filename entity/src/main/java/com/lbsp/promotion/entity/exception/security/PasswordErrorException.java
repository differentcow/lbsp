package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class PasswordErrorException extends AuthenticationServiceException{
	private static final long serialVersionUID = 1595571549180153712L;

	public PasswordErrorException(String msg) {
		super(msg);
	}
}
