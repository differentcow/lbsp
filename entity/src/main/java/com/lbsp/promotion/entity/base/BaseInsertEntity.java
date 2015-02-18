package com.lbsp.promotion.entity.base;

public class BaseInsertEntity extends BaseEntity{
	private String keys;
	private String values;

	public BaseInsertEntity(String table,String keys, String values) {
		super(table);
		this.keys = keys;
		this.values = values;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
