package com.lbsp.promotion.entity.base;

import java.util.List;

public class BaseInsertAllEntity extends BaseEntity{
	private String key;
	private List<String> values;
	
	public BaseInsertAllEntity(String table, String key, List<String> values) {
		super(table);
		this.key = key;
		this.values = values;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	
}
