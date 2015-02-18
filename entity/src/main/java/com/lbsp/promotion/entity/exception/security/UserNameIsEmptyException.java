package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class UserNameIsEmptyException extends AuthenticationServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7550867629105481181L;

	public UserNameIsEmptyException(String msg) {
		super(msg);
	}

}
