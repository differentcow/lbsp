package com.lbsp.promotion.gen.assist.model;

import java.util.Map;

/**
 * Created by hp on 2015/4/21.
 */
public class Select {

    private boolean isStatic;

    private Map<String,String> staticMap;

    private String selectName;

    private String selectType;

    public Map<String, String> getStaticMap() {
        return staticMap;
    }

    public void setStaticMap(Map<String, String> staticMap) {
        this.staticMap = staticMap;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
