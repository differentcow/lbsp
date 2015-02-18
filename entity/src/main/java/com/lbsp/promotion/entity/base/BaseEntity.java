package com.lbsp.promotion.entity.base;

public abstract class BaseEntity {
	private String table;

	public BaseEntity(String table) {
		super();
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

}
