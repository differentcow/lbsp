package com.lbsp.promotion.entity.exception.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class RequestMustPostException extends AuthenticationServiceException{
	private static final long serialVersionUID = 2504593625977065479L;

	public RequestMustPostException(String msg) {
		super(msg);
	}

}
