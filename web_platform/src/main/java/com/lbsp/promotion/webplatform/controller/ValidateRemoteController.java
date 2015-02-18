package com.lbsp.promotion.webplatform.controller;

import com.lbsp.promotion.entity.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2014/6/20
 * 
 * @author Barry
 * 
 */
@Controller
@RequestMapping("/remote/validate")
public class ValidateRemoteController {
	@Autowired
	private ThroughCorePlatformController throughCorePlatformController;

	@RequestMapping(value = "/{url}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean validate(@PathVariable String url, HttpServletRequest request,HttpServletResponse response) {
		BaseResult<Boolean> result = (BaseResult<Boolean>) throughCorePlatformController
				.getFromCore(url, request,response);
		return result.getResult();
	}
}
