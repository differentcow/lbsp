package com.lbsp.promotion.entity.exception;

public class SqlStatementException extends RuntimeException {

	private static final long serialVersionUID = -4825182878349598758L;

	public SqlStatementException(String message) {
		super(message);
	}
}
