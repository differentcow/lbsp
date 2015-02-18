package com.lbsp.promotion.entity.model;

import java.util.Date;

public class LinkModel {

    protected Date create_date;

    protected String operator;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}