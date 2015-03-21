package com.lbsp.promotion.entity.request;

import com.lbsp.promotion.entity.model.Comment;

import java.util.List;

/**
 * Created by HLJ on 2015/3/21.
 */
public class CommentReq extends Comment {

    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setId(List<Integer> ids) {
        this.ids = ids;
    }
}
