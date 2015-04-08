package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Category;

/**
 * Created by hp on 2015/4/8.
 */
public class CategoryRsp extends Category {

    private String strId;

    private String parentName;

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
