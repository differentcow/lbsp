package com.lbsp.promotion.webplatform.search;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Barry on 2015/2/24.
 */
public class SolrIndex {

    private String field;

    private Object value;

    private String type;

    public SolrIndex(){
    }
    public SolrIndex(String field,Object value){
        this.field = field;
        this.value = value;
    }
    public SolrIndex(String field,Object value,String type){
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public String getVal(){
        if ("date".equals(type)){
            return new SimpleDateFormat("YYYY-MM-DDThh:mm:ssZ").format((Date)value);
        }
        return value.toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
