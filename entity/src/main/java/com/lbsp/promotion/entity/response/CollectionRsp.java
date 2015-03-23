package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.CollectionTable;

/**
 * Created by HLJ on 2015/3/23.
 */
public class CollectionRsp extends CollectionTable {

    private String customerName;

    private String shopName;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
