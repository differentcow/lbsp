package com.lbsp.promotion.entity.base;

import org.apache.ibatis.session.RowBounds;


public class BaseComplexFieldsEntity extends BaseComplexEntity{

	private String fields;
	private RowBounds rowBounds;

	public BaseComplexFieldsEntity(String table, String where, String orderBy,
			String fields) {
		super(table, where, orderBy);
		this.fields = fields;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public RowBounds getRowBounds() {
		return rowBounds;
	}

	public void setRowBounds(RowBounds rowBounds) {
		this.rowBounds = rowBounds;
	}
	
}
