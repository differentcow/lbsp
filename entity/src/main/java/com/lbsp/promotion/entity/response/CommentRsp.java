package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Comment;

/**
 * Created by HLJ on 2015/3/21.
 */
public class CommentRsp extends Comment {

    private String commentor;

    private String preferentialTitle;

    public String getCommentor() {
        return commentor;
    }

    public void setCommentor(String commentor) {
        this.commentor = commentor;
    }

    public String getPreferentialTitle() {
        return preferentialTitle;
    }

    public void setPreferentialTitle(String preferentialTitle) {
        this.preferentialTitle = preferentialTitle;
    }
}
