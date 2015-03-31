package com.lbsp.promotion.coreplatform.interceptor.permission;

import com.lbsp.promotion.core.service.user.UserService;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.exception.AuthKeyNotExistException;
import com.lbsp.promotion.entity.exception.NoPermissionException;
import com.lbsp.promotion.entity.model.User;
import com.lbsp.promotion.entity.response.OperateResource;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.session.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class PermissionInterceptor extends HandlerInterceptorAdapter {
	private static final String NONE_PERMISSIONMETHOD = "none";

    private List<String> excludes;


    @Autowired
	private UserService<User> userService;

    private Map<String,Integer> excludesWithParam;

    public void setExcludesWithParam(Map<String, Integer> excludesWithParam) {
        this.excludesWithParam = excludesWithParam;
    }

    @Value("${permission.method}")
	private String permissionMethod;

    @Value("${permission.allow.md5}")
    private String md5;

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

    /*private String getRemoteHost(javax.servlet.http.HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }*/

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getServletPath();
        /*if (!"/user/login".equals(uri)){
            throw new AuthKeyNotExistException("no permission to visit rest");
        }*/

		String authKey = request.getParameter(GenericConstants.AUTHKEY);
		if (authKey ==null&&NONE_PERMISSIONMETHOD.equals(permissionMethod)) {
			return true;
		} else {
			UserRsp userRsp = null;
			if(SessionMap.getSessionMapInstance().isExists(authKey) != null){
				userRsp = (UserRsp)SessionMap.getSessionMapInstance().isExists(authKey);
			}else{
				userRsp = userService.getUserBySecurityKey(authKey);
				if (userRsp.getUser() != null) {
					SessionMap.getSessionMapInstance().put(userRsp.getUser().getSecurity_key(), userRsp);
				}
			}
            if(userRsp == null){
                throw new AuthKeyNotExistException("no permission to visit rest");
            }
			if(userRsp != null){
				request.setAttribute(GenericConstants.REQUEST_AUTH, userRsp);
			}

            String method = request.getMethod();
            if (uri.startsWith(GenericConstants.NOT_CHECK)){
                return true;
            }
			if (this.excludes != null && this.excludes.contains(uri)) {
			    return true;
			}
            if (this.excludesWithParam != null) {
                for (Map.Entry<String,Integer> map : this.excludesWithParam.entrySet()){
                    if(uri.startsWith(map.getKey()) && uri.replace(map.getKey(),"").split("/").length == map.getValue()){
                        return true;
                    }
                }
            }

			return isExistPermission(userRsp, uri, method);
		}
	}

	private boolean isExistPermission(UserRsp  userRsp, String path, String method) {
        boolean flag = false;
        List<String> urlParam = userRsp.getNoParamUrl();
        label: for (OperateResource or : userRsp.getFuncsList()){
            if (or.getUrl().equals(path) && or.getMethod().equalsIgnoreCase(method)){
                flag = true;
                break label;
            }else if(or.getMethod().equalsIgnoreCase(method) && !urlParam.contains(path)){
                if(or.getUrl().indexOf("{") != -1 && or.getUrl().indexOf("}") != -1
                    && or.getPath_param() != null && or.getPath_param() > 0) {
                    String url = or.getUrl().substring(0, or.getUrl().indexOf("{"));
                    if (path.replace(url, "").split("/").length == or.getPath_param()) {
                        flag = true;
                        break label;
                    }
                }
            }
        }
        if (!flag) {
            throw new NoPermissionException("no permission to visit rest");
        }
		return flag;
	}


}
