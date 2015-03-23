package com.lbsp.promotion.entity.model;


import com.lbsp.promotion.entity.table.annotation.MyTable;

/**
 *
 * Created on 2015-03-22 09:48:51
 *
 * @author 简易自动代码创建工具
 */
@MyTable("collection")
public class CollectionTable extends BaseModel {
	private String mark;
	private String type;
	private Integer page;
	private Integer count;
	private Integer customer_id;


	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMark() {
		return this.mark;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPage() {
		return this.page;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public Integer getCustomer_id() {
		return this.customer_id;
	}


}