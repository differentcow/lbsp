package com.lbsp.promotion.core.key;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service("userSecurityKeyGenerator")
public class UserSecurityKeyGenerator implements KeyGenerate {

	public String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
