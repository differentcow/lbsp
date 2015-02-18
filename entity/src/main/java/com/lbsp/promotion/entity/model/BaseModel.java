package com.lbsp.promotion.entity.model;

import java.util.Date;

public class BaseModel {
    protected String id;

    protected String create_user;

    protected Date create_date;

    protected String update_user;

    protected Date Last_update_date;

    public Date getLast_update_date() {
        return Last_update_date;
    }

    public void setLast_update_date(Date Last_update_date) {
        this.Last_update_date = Last_update_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }
}