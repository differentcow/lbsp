/**
 * 
 */
package com.lbsp.promotion.util.http.request;

import org.apache.commons.lang.StringUtils;

/**
 * @author Barry
 *
 */
public class HttpRequestFactory {
	private String url;

	public void setUrl(String url) {
		this.url = url;
	}
	
	public  HttpRequest createInstance(){
		HttpRequest request = new GenericHttpRequest();
		if(StringUtils.isNotBlank(url)){
			request.setUrl(url);
		}
		return request;
	}

	public  HttpRequest createInstance(RequestParam param){
		HttpRequest request = new GenericHttpRequest(param);
		if(StringUtils.isNotBlank(url)){
			request.setUrl(url);
		}
		return request;
	}
	
	public  HttpRequest createInstance(RequestParam param, String endpoint){
		HttpRequest request = new GenericHttpRequest(param,endpoint);
		if(StringUtils.isNotBlank(url)){
			request.setUrl(url);
		}
		return request;
	}
	
	public  HttpRequest createInstance(RequestParam param, String endpoint,
			Object reqestEntity){
		HttpRequest request = new GenericHttpRequest(param,endpoint,reqestEntity);
		if(StringUtils.isNotBlank(url)){
			request.setUrl(url);
		}
		return request;
	}
}
