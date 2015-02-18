package com.lbsp.promotion.entity.base;

public class BaseComplexEntity extends BaseEntity {
	private String where;
	private String orderBy;

	public BaseComplexEntity(String table, String where, String orderBy) {
		super(table);
		this.where = where;
		this.orderBy = orderBy;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
