package com.lbsp.promotion.util.http.request;

import org.apache.commons.httpclient.HttpClient;

public class HttpClientFactory {
	private static HttpClient client;

	private HttpClientFactory() {

	}

	private static class InnerHttpClientFactory {
		public static HttpClient getHttpClient() {
			if (client == null) {
				client = new HttpClient();
			}
			return client;
		}
	}

	public static HttpClient getHttpClient() {
		return InnerHttpClientFactory.getHttpClient();
	}

}
