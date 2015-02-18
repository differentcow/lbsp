package com.lbsp.promotion.entity.model;

import java.util.Date;

public class User extends BaseModel{
    private String username;

    private String account;
    
    private String password;
    
    private String email;
    
    private String status;
    
    private String security_key;
    
    private Date security_time;

    private Date login_time;

    private String auth_code;

    private Date auth_time;

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public Date getAuth_time() {
        return auth_time;
    }

    public void setAuth_time(Date auth_time) {
        this.auth_time = auth_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurity_key() {
        return security_key;
    }

    public void setSecurity_key(String security_key) {
        this.security_key = security_key;
    }

    public Date getSecurity_time() {
        return security_time;
    }

    public void setSecurity_time(Date security_time) {
        this.security_time = security_time;
    }

    public Date getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Date login_time) {
        this.login_time = login_time;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}