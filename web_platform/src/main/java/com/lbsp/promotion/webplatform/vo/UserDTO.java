package com.lbsp.promotion.webplatform.vo;

import com.lbsp.promotion.entity.response.UserRsp;

import java.util.List;

public class UserDTO {
	private List<UserFunctionDTO> funcs;
	private List<String> auths;
	private UserRsp userRsp;
	private Boolean isPlatformAdmin;
	public List<UserFunctionDTO> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<UserFunctionDTO> funcs) {
		this.funcs = funcs;
	}

	public UserRsp getUserRsp() {
		return userRsp;
	}

	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}

	public void setUserRsp(UserRsp userRsp) {
		this.userRsp = userRsp;
	}

	public Boolean getIsPlatformAdmin() {
		/*if(userRsp.getRoles()!=null&&!userRsp.getRoles().isEmpty()){
			for(Role role:userRsp.getRoles()){
				if("1".equals(role.getId())){
					isPlatformAdmin = true;
					break;
				}
			}
			return isPlatformAdmin;
		}*/
		return null;
	}

}
