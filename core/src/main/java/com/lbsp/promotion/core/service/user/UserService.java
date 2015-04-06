package com.lbsp.promotion.core.service.user;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.User;
import com.lbsp.promotion.entity.response.UserRsp;

public interface UserService<T> extends BaseService<T> {

	/**
	 * 通过用户账户密码获取用户信息
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	UserRsp getUserByAccountandPassword(String account, String password);

	/**
	 * 更新auth_code
	 * 
	 * @param userId
	 * @param authCode
	 * @return
	 */
	boolean updateAuthCode(Integer userId, String authCode);

	/**
	 * 通过验证码获取用户信息
	 * 
	 * @param authKey
	 * @return
	 */
	UserRsp getUserBySecurityKey(String authKey);

	/**
	 * 删除用户
	 * 
	 * @param ids
	 * @return
	 */
	boolean delUser(String ids);

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @return
	 */
	boolean addUser(User user);

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	boolean updateUser(User user);

	/**
	 * 通过权限查找电子邮件
	 * 
	 * @param codes
	 * @return
	 */
	List<String> getEmailByOperate(String[] codes);

}
