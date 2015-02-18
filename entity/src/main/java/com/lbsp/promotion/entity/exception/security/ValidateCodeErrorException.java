package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class ValidateCodeErrorException extends AuthenticationServiceException{
	private static final long serialVersionUID = -7579906085234875449L;

	public ValidateCodeErrorException(String msg) {
		super(msg);
	}

}
