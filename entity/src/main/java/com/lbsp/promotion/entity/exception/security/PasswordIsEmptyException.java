package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class PasswordIsEmptyException extends AuthenticationServiceException{
	private static final long serialVersionUID = -489988582655146755L;

	public PasswordIsEmptyException(String msg) {
		super(msg);
	}

}
