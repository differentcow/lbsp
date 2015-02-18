package com.lbsp.promotion.entity.base;

public class BaseFindFieldsEntity extends BaseFindEntity {

	private String fields;

	public BaseFindFieldsEntity(String table, String where, String fields) {
		super(table, where);
		this.fields = fields;

	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

}
