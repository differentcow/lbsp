package com.lbsp.promotion.entity.model;


/**
 *
 * Created on 2015-04-01 09:21:24
 *
 * @author 简易自动代码创建工具
 */
public class Category extends BaseModel {
	private String name;
	private Integer parent_id;
	private Integer depth;
	private Integer status;
	private Integer priority;
	private String query_code;


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getParent_id() {
		return this.parent_id;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getDepth() {
		return this.depth;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setQuery_code(String query_code) {
		this.query_code = query_code;
	}

	public String getQuery_code() {
		return this.query_code;
	}


}