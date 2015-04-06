package com.lbsp.promotion.util.http.request;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGenericHttpRequest implements HttpRequest {
	private String url = "http://localhost:7171/core-platform";

	public AbstractGenericHttpRequest() {

	}

	public AbstractGenericHttpRequest(String domain) {
		this.url = domain;
	}

	protected String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private HttpClient client = new HttpClient(new HttpClientParams(),
			new SimpleHttpConnectionManager(true));

	protected HttpClient getClient() {
		return client;
	}

	public <T> T post(Class<T> cls) {
		String queryString = this.post();
		String[] pairs = queryString.split("&");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		for (String mapStr : pairs) {
			String[] maps = mapStr.split("=");
			String key = maps[0];
			key = key.substring(0, 1).toLowerCase()+key.substring(1, key.length());
			queryMap.put(key, maps[1]);
		}
		try {
			T obj = cls.newInstance();
			BeanUtils.populate(obj, queryMap);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException("Map convert pojo error:" + e);
		}
	}

}
