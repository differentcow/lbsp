package com.lbsp.promotion.entity.model;

import com.lbsp.promotion.entity.table.annotation.MyTable;

/**
 * Created by Barry on 2015/2/17.
 */
@MyTable(value = "function_operate")
public class FunctionOperate extends BaseModel {

    private String id;
    private String code;
    private String url;
    private String page_id;
    private String name;
    private String method;
    private String base_url;
    private Integer path_param;
    private Integer sort_index;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public Integer getPath_param() {
        return path_param;
    }

    public void setPath_param(Integer path_param) {
        this.path_param = path_param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }
}
