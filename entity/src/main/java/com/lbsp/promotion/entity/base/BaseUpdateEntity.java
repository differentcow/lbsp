package com.lbsp.promotion.entity.base;

public class BaseUpdateEntity extends BaseEntity {
	private String where;
	private String kvs;

	public BaseUpdateEntity(String table, String where, String kvs) {
		super(table);
		this.where = where;
		this.kvs = kvs;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getKvs() {
		return kvs;
	}

	public void setKvs(String kvs) {
		this.kvs = kvs;
	}

}
