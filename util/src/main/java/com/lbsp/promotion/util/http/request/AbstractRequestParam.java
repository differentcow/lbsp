package com.lbsp.promotion.util.http.request;

import java.util.LinkedHashMap;

public class AbstractRequestParam extends LinkedHashMap<String, String> implements RequestParam {
	private static final long serialVersionUID = -7633668235402887126L;

	public void fill(String key, String value) {
		this.put(key, value);
	}
}
