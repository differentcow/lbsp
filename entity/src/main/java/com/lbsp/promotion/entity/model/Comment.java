package com.lbsp.promotion.entity.model;

public class Comment extends BaseModel{
    private Integer preferential_id;

    private String content;

    private String anonymous_user;

    private Integer customer_id;

    private Integer status;

    public Integer getPreferential_id() {
        return preferential_id;
    }

    public void setPreferential_id(Integer preferential_id) {
        this.preferential_id = preferential_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnonymous_user() {
        return anonymous_user;
    }

    public void setAnonymous_user(String anonymous_user) {
        this.anonymous_user = anonymous_user;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}