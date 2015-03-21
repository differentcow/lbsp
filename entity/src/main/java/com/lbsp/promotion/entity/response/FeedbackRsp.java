package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Feedback;

/**
 * Created by HLJ on 2015/3/21.
 */
public class FeedbackRsp extends Feedback {

    private String customer;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
