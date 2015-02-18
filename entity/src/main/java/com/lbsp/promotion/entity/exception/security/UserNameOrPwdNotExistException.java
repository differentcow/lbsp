package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class UserNameOrPwdNotExistException extends AuthenticationServiceException{
	private static final long serialVersionUID = 1926691226092958600L;

	public UserNameOrPwdNotExistException(String msg) {
		super(msg);
	}

}
