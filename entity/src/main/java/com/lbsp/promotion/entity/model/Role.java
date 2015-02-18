package com.lbsp.promotion.entity.model;

public class Role extends BaseModel{
    private String name;

    private String status;

    private Integer func_count;

    private String parent_id;

    private Integer user_count;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFunc_count() {
        return func_count;
    }

    public void setFunc_count(Integer func_count) {
        this.func_count = func_count;
    }

    public Integer getUser_count() {
        return user_count;
    }

    public void setUser_count(Integer user_count) {
        this.user_count = user_count;
    }
}