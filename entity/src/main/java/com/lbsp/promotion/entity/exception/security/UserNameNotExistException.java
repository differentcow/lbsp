package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class UserNameNotExistException extends AuthenticationServiceException{
	private static final long serialVersionUID = -5859332024702774926L;

	public UserNameNotExistException(String msg) {
		super(msg);
	}

}
