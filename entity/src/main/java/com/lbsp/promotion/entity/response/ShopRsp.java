package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Shop;

/**
 * Created by hp on 2015/4/1.
 */
public class ShopRsp extends Shop {

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
