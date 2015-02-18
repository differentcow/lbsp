package com.lbsp.promotion.entity.base;

public class BaseFindEntity extends BaseEntity {
	private String where;

	public BaseFindEntity(String table, String where) {
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
