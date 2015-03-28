package com.lbsp.promotion.entity.response;

import com.lbsp.promotion.entity.model.Role;
import com.lbsp.promotion.entity.model.User;

import java.io.Serializable;
import java.util.List;


public class UserRsp implements Serializable{
	private static final long serialVersionUID = 7085911153163538622L;

	private User user;

    private List<Role> currentRole;

    private List<Role> roles;

    private List<Role> parentRole;

	private List<String> pages;
	
    private List<OperateResource> funcsList;

    private List<OperateResource> pageList;

    private List<String> noParamUrl;

    public List<String> getNoParamUrl() {
        return noParamUrl;
    }

    public void setNoParamUrl(List<String> noParamUrl) {
        this.noParamUrl = noParamUrl;
    }

    public List<OperateResource> getPageList() {
        return pageList;
    }

    public void setPageList(List<OperateResource> pageList) {
        this.pageList = pageList;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public List<Role> getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(List<Role> currentRole) {
        this.currentRole = currentRole;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getParentRole() {
        return parentRole;
    }

    public void setParentRole(List<Role> parentRole) {
        this.parentRole = parentRole;
    }

    public List<OperateResource> getFuncsList() {
        return funcsList;
    }

    public void setFuncsList(List<OperateResource> funcsList) {
        this.funcsList = funcsList;
    }
}
