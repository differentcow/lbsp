package com.lbsp.promotion.entity.response;

import java.util.List;

/**
 * Created by Barry on 2015/2/19.
 */
public class OperateView {

    private String name;
    private String code;
    private String id;
    private List<OperateView> sub;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OperateView> getSub() {
        return sub;
    }

    public void setSub(List<OperateView> sub) {
        this.sub = sub;
    }
}
