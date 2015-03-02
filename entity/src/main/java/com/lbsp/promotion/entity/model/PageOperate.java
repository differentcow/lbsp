package com.lbsp.promotion.entity.model;

import com.lbsp.promotion.entity.table.annotation.MyTable;

/**
 * Created by Barry on 2015/2/17.
 */
@MyTable(value = "page_operate")
public class PageOperate extends BaseModel {

    private String code;
    private Integer parent_id;
    private String parent_code;
    private String name;
    private Integer sort_index;

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }
}
