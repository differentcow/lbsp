package com.lbsp.promotion.entity.exception;

public class NoPermissionException extends RuntimeException{
	private static final long serialVersionUID = 9175120163269832284L;

	public NoPermissionException(String message){
		super(message);
	}
	
}
