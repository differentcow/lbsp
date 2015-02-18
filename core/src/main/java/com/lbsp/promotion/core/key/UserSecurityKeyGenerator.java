package com.lbsp.promotion.core.key;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userSecurityKeyGenerator")
public class UserSecurityKeyGenerator implements KeyGenerate{

	@Override
	public String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
