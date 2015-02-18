package com.lbsp.promotion.webplatform.security.token.provider;

import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider{

	@Override
	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails user) {
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken)authentication;
		UsernamePasswordSecurityKeyToken result = new UsernamePasswordSecurityKeyToken(principal,authentication.getCredentials(), user.getAuthorities(),token.getUserResult());
		result.setDetails(authentication.getDetails());
		return result;
	}
}
