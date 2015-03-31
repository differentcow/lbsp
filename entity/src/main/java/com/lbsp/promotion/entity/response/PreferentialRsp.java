package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Preferential;

/**
 * Created by HLJ on 2015/4/1.
 */
public class PreferentialRsp extends Preferential{

    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
