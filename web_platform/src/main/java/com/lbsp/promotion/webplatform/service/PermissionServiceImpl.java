package com.lbsp.promotion.webplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.http.request.GenericHttpParam;
import com.lbsp.promotion.util.http.request.HttpRequest;
import com.lbsp.promotion.util.http.request.HttpRequestFactory;
import com.lbsp.promotion.util.http.request.RequestParam;
import com.lbsp.promotion.webplatform.base.service.CommonServiceImpl;

@Service
public class PermissionServiceImpl extends CommonServiceImpl implements
		PermissionService {
	@Autowired
	private HttpRequestFactory factory;
	
	public BaseResult<UserRsp> checkUserNamePwd(String userName, String pwd) {
		RequestParam param = new GenericHttpParam();
		param.fill("userName", userName);
		param.fill("password", pwd);
		HttpRequest http = factory.createInstance(param, "/user/login");
		BaseResult<UserRsp> result = http.post(new TypeReference<BaseResult<UserRsp>>() {
        });
		this.baseCheck(result);
		return result;
	}

}
