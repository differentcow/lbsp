package com.lbsp.promotion.entity.model;

import com.lbsp.promotion.entity.table.annotation.MyTable;

/**
 * Created by Barry on 2015/2/17.
 */
@MyTable(primaryKey = "privilege_id",value = "privilege")
public class Privilege extends BaseModel{

    private String privilege_id;
    private String privilege_access;
    private String privilege_access_value;
    private String privilege_master;
    private String privilege_master_value;
    private String privilege_operation;

    public String getPrivilege_id() {
        return privilege_id;
    }

    public void setPrivilege_id(String privilege_id) {
        this.privilege_id = privilege_id;
    }

    public String getPrivilege_access() {
        return privilege_access;
    }

    public void setPrivilege_access(String privilege_access) {
        this.privilege_access = privilege_access;
    }

    public String getPrivilege_access_value() {
        return privilege_access_value;
    }

    public void setPrivilege_access_value(String privilege_access_value) {
        this.privilege_access_value = privilege_access_value;
    }

    public String getPrivilege_master() {
        return privilege_master;
    }

    public void setPrivilege_master(String privilege_master) {
        this.privilege_master = privilege_master;
    }

    public String getPrivilege_master_value() {
        return privilege_master_value;
    }

    public void setPrivilege_master_value(String privilege_master_value) {
        this.privilege_master_value = privilege_master_value;
    }

    public String getPrivilege_operation() {
        return privilege_operation;
    }

    public void setPrivilege_operation(String privilege_operation) {
        this.privilege_operation = privilege_operation;
    }
}
