package com.lbsp.promotion.webplatform.service;

import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.webplatform.base.service.CommonService;

public interface PermissionService extends CommonService{
	/**
	 * 验证用户账号与密码是否正确
	 * @return
	 */
	public BaseResult<UserRsp> checkUserNamePwd(String userName, String pwd);
}
