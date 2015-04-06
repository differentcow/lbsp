package com.lbsp.promotion.webplatform.security.service;

import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SecurityUserService implements UserDetailsService,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1293075779433963127L;

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder
				.getContext().getAuthentication();
		/*for (final String func : token.getUserResult().getFuncList()) {
			authorities.add(new GrantedAuthority() {
				private static final long serialVersionUID = 4277762378492738718L;

				@Override
				public String getAuthority() {
					return func;
				}
			});
		}*/
		return new User(token.getPrincipal().toString(), token.getCredentials().toString(),
				authorities);
	}
}
