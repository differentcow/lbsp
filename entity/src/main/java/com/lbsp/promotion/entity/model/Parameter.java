package com.lbsp.promotion.entity.model;

import com.lbsp.promotion.entity.table.annotation.MyTable;

@MyTable(primaryKey = "id",value = "parameter")
public class Parameter extends BaseModel{
    private String code;

    private String name;

    private String type;

    private String type_meaning;

    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_meaning() {
        return type_meaning;
    }

    public void setType_meaning(String type_meaning) {
        this.type_meaning = type_meaning;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}