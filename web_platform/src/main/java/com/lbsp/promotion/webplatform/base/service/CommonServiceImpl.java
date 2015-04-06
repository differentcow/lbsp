package com.lbsp.promotion.webplatform.base.service;


import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.exception.HttpClientException;
import com.lbsp.promotion.entity.exception.RequestFailureException;
import com.lbsp.promotion.entity.exception.security.UserNameOrPwdNotExistException;

public abstract class CommonServiceImpl implements CommonService {

	public void baseCheck(BaseResult result) {
		if (result == null) {
			throw new HttpClientException("请求出错");
		}
		switch ((Integer)result.getState().get("code")) {
		case 200000:
			break;
		case 500098:
			throw new UserNameOrPwdNotExistException("用户名或密码不存在");
		default:
			throw new RequestFailureException(String.valueOf(result.getState().get("code")));
		}
	}

}
