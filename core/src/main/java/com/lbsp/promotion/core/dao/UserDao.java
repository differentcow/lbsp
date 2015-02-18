/**
 * 
 */
package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barry
 *
 */
public interface UserDao {

	/**
	 * 通过用户账户获取用户信息
	 * 
	 * @param account,status
	 * @return
	 */
	User getUserByAccount(@Param("account") String account, @Param("status") Integer status);

	/**
	 * 通过验证码获取用户信息
	 * 
	 * @param authKey
	 * @return
	 */
	User getUserBySecurityKey(@Param("authKey") String authKey);


    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUserInfo(@Param("user") User user);

    /**
     * 通过权限查找电子邮件
     *
     * @param codes
     * @return
     */
    List<String> getEmailByOperate(@Param("codes") String[] codes);
}
