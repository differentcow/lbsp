package com.lbsp.promotion.entity.model;


/**
 *
 * Created on 2015-04-01 09:21:47
 *
 * @author 简易自动代码创建工具
 */
public class Advert extends BaseModel {
	private String title;
	private String type;
	private String event;
	private String pic_path;
	private String description;
	private String customer;
	private Integer status;


	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent() {
		return this.event;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getPic_path() {
		return this.pic_path;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}


}