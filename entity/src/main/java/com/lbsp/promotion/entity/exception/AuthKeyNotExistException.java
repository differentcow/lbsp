package com.lbsp.promotion.entity.exception;

public class AuthKeyNotExistException extends RuntimeException{

	private static final long serialVersionUID = 5071316145157857149L;

	public AuthKeyNotExistException(String message){
		super(message);
	}	
}
