package com.lbsp.promotion.entity.base;

public class BaseDeleteEntity extends BaseEntity {
	private String where;

	public BaseDeleteEntity(String table, String where) {
		super(table);
		this.where = where;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

}
