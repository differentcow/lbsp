package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Preferential;

/**
 * Created by HLJ on 2015/4/1.
 */
public class PreferentialRsp extends Preferential{

    private String shopName;

    private Long start_time_s;

    private Long end_time_s;

    public Long getStart_time_s() {
        return start_time_s;
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
