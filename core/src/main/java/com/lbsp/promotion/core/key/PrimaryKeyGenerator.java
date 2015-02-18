package com.lbsp.promotion.core.key;

import java.util.UUID;

public class PrimaryKeyGenerator implements KeyGenerate{

	@Override
	public String generate() {
		return String.valueOf(UUID.randomUUID().hashCode());
	}
	
}
