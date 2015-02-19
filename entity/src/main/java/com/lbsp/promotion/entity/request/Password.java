package com.lbsp.promotion.entity.request;

/**
 * Created by Barry on 2014/10/29.
 */
public class Password {

    private String oldPassword;

    private String newPassword;

    private String surePassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getSurePassword() {
        return surePassword;
    }

    public void setSurePassword(String surePassword) {
        this.surePassword = surePassword;
    }
}
