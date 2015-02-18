package com.lbsp.promotion.util.http.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lbsp.promotion.entity.base.BaseResult;
import org.apache.commons.httpclient.methods.multipart.Part;

public interface HttpRequest {
	public <T> BaseResult<T> get(TypeReference<BaseResult<T>> type);

	public <T> BaseResult<T> post(TypeReference<BaseResult<T>> type);

	public <T> BaseResult<T> put(TypeReference<BaseResult<T>> type);

	public <T> BaseResult<T> delete(TypeReference<BaseResult<T>> type);

	public String post();
	
	public String put();
	
	public String delete();
	
	public String postMutilPart(Part[] parts);

	public String get();

	public void setRequestType(GenericHttpRequest.RequestType requestType);

	/**
	 * response a=1&b=2&c=3
	 * 
	 * @param cls
	 *            convert class
	 * @return
	 */
	public <T> T post(Class<T> cls);
	
	public <T> T post(Class<T> cls, ResponseType type);

	public void setUrl(String url);

	public void setEndpoint(String endpoint);

	public void setParam(RequestParam param);
}
