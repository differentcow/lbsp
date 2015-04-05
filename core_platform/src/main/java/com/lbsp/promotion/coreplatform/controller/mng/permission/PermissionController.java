package com.lbsp.promotion.coreplatform.controller.permission;

import com.lbsp.promotion.core.service.user.UserService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.exception.ServiceIsNullException;
import com.lbsp.promotion.entity.exception.security.UserNameNotExistException;
import com.lbsp.promotion.entity.model.User;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.session.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/user")
public class PermissionController extends BaseController{
	@Autowired
	private UserService<User> userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
//    @LogAnnotation(module = "user_login",operate = "log_operate_login", content = "{log.login.msg}")
	public Object login(
            @RequestParam(required = true, value = "userName") String userName,
			@RequestParam(required = true, value = "password") String password) {
		if (userService == null) {
			throw new ServiceIsNullException("tUserService is null exception");
		}
		
		UserRsp result = userService.getUserByAccountandPassword(userName, password);
		if (result.getUser() != null) {
			// 登陆成功,更新登录时间
            result.getUser().setLogin_time(new Date());
            userService.update(result.getUser());
			//添加session
            UserRsp res = new UserRsp();
            res.setFuncsList(result.getFuncsList());
            res.setRoles(result.getRoles());
            res.setPages(result.getPages());
            res.setCurrentRole(result.getCurrentRole());
            res.setParentRole(result.getParentRole());
			res.setUser(result.getUser());
            res.setNoParamUrl(result.getNoParamUrl());
            res.setPageList(result.getPageList());
			SessionMap.getSessionMapInstance().put(res.getUser().getSecurity_key(), res);
		} else {
			throw new UserNameNotExistException("用户名或密码错误");
		}
		return this.createBaseResult("登陆成功",result);
	}

}
