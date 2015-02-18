package com.lbsp.promotion.webplatform.security.filter;

import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.exception.security.*;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import com.lbsp.promotion.webplatform.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CustomUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public static final String VALIDATE_CODE = "validateCode";
	public static final String USERNAME = "userName";
	public static final String PASSWORD = "password";
	@Autowired
	private PermissionService permissionService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new RequestMustPostException("请求必须为POST方式!");
		}
		// 检测验证码
		checkValidateCode(request);
		this.getAllowSessionCreation();
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		// 验证用户账号与密码是否正确
		username = username.trim();
		BaseResult<UserRsp> result =  this.permissionService.checkUserNamePwd(username, password);
		if(result.getResult() == null){
			throw new UserNameOrPwdNotExistException("用户名或密码不存在");
		}
		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordSecurityKeyToken authRequest = new UsernamePasswordSecurityKeyToken(
				username,result.getResult().getUser().getSecurity_key(), result.getResult());
		// Place the last username attempted into HttpSession for views
		// 允许子类设置详细属性
		setDetails(request, authRequest);
		SecurityContextHolder.getContext().setAuthentication(authRequest);
		//request.getSession().setAttribute(GenericConstants.REQUEST_AUTH, authRequest);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	
	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainSessionValidateCode(HttpSession session) {
		Object obj = session.getAttribute(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected void checkValidateCode(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionValidateCode = obtainSessionValidateCode(session);
		// 让上一次的验证码失效
		session.setAttribute(VALIDATE_CODE, null);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter)
				|| !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new ValidateCodeErrorException("验证码错误！");
		}
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String password = request.getParameter(PASSWORD);
		if (password == null || "".equals(password)) {
			throw new PasswordIsEmptyException("密码不能为空!");
		}
		return password;
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		String username = request.getParameter(USERNAME);
		if (username == null || "".equals(username)) {
			throw new UserNameIsEmptyException("用户名不能为空!");
		}
		return username;
	}
	
	
}
