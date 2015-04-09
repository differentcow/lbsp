package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Preferential;

/**
 * Created by HLJ on 2015/4/1.
 */
public class PreferentialRsp extends Preferential{

    private String shopName;

    private Long start_time_s;

    private Long end_time_s;

    private Integer category_id;

    private String category_name;

    private Integer org_id;

    private Integer org_category_id;

    public Integer getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }

    public Integer getOrg_category_id() {
        return org_category_id;
    }

    public void setOrg_category_id(Integer org_category_id) {
        this.org_category_id = org_category_id;
    }

    public Long getStart_time_s() {
        return start_time_s;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setStart_time_s(Long start_time_s) {
        this.start_time_s = start_time_s;
    }

    public Long getEnd_time_s() {
        return end_time_s;
    }

    public void setEnd_time_s(Long end_time_s) {
        this.end_time_s = end_time_s;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
