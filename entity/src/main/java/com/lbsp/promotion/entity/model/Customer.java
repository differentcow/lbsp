package com.lbsp.promotion.entity.model;


/**
 *
 * Created on 2015-03-24 12:01:27
 *
 * @author 简易自动代码创建工具
 */
public class Customer extends BaseModel {
	private String type;
	private String account;
	private String name;
	private String password;
	private Integer status;
	private String mobile;
	private String email;
	private String key;
	private Integer gender;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getGender() {
		return this.gender;
	}


}