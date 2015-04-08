package com.lbsp.promotion.entity.request;

import com.lbsp.promotion.entity.model.Category;

import java.util.List;

/**
 * Created by hp on 2015/4/8.
 */
public class CategoryOperate {

    private List<Category> saveOperateList;

    private List<Category> editOperateList;

    private List<Category> deleteOperateList;

    public List<Category> getSaveOperateList() {
        return saveOperateList;
    }

    public void setSaveOperateList(List<Category> saveOperateList) {
        this.saveOperateList = saveOperateList;
    }

    public List<Category> getEditOperateList() {
        return editOperateList;
    }

    public void setEditOperateList(List<Category> editOperateList) {
        this.editOperateList = editOperateList;
    }

    public List<Category> getDeleteOperateList() {
        return deleteOperateList;
    }

    public void setDeleteOperateList(List<Category> deleteOperateList) {
        this.deleteOperateList = deleteOperateList;
    }
}
