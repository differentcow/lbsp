package com.lbsp.promotion.util.http.request;

import java.util.Map;

public interface RequestParam extends Map<String, String> {
	public void fill(String key, String value);
}
