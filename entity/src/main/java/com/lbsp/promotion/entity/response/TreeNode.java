package com.lbsp.promotion.entity.response;


import java.util.List;

public class TreeNode{

    private List<TreeNode> item;
    private String text;
    private Integer id;
    private Integer child;
    private Integer open;
    private List<TreeNodeUserData> userdata;

    public List<TreeNodeUserData> getUserdata() {
        return userdata;
    }

    public Integer getChild() {
        return child;
    }

    public void setChild(Integer child) {
        this.child = child;
    }

    public void setUserdata(List<TreeNodeUserData> userdata) {
        this.userdata = userdata;
    }

    public List<TreeNode> getItem() {
        return item;
    }

    public void setItem(List<TreeNode> item) {
        this.item = item;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }
}
