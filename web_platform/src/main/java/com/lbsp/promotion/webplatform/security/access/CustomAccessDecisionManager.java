package com.lbsp.promotion.webplatform.security.access;

import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Iterator;

public class CustomAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		if(SecurityContextHolder.getContext()
				.getAuthentication() instanceof UsernamePasswordSecurityKeyToken){
				UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken)SecurityContextHolder.getContext()
					.getAuthentication();
			Iterator<ConfigAttribute> it = configAttributes.iterator();
			String funcID = null;
			while (it.hasNext()) {
				funcID = it.next().getAttribute();
			}
			if (CustomMetadataSource.ALL.equals(funcID)) {
				return;
			}
			if (token == null /**|| !token.getUserResult().getFuncList().contains(funcID)*/) {
				throw new AccessDeniedException("没有权限访问");
			}
		}
	}

	@Override
	public boolean supports(ConfigAttribute paramConfigAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> paramClass) {
		return true;
	}

}
