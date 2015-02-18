package com.lbsp.promotion.webplatform.security.token;

import com.lbsp.promotion.entity.response.UserRsp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordSecurityKeyToken extends
		UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 6783564790634793854L;

	private UserRsp userResult;

	public UsernamePasswordSecurityKeyToken(Object principal,
			Object credentials, UserRsp userResult) {
		super(principal, credentials);
		this.userResult = userResult;
	}

	public UsernamePasswordSecurityKeyToken(Object principal,
			Object credentials,
			Collection<? extends GrantedAuthority> authorities, UserRsp userResult) {
		super(principal, credentials, authorities);
		this.userResult = userResult;
	}

	public UserRsp getUserResult() {
		return userResult;
	}
}
