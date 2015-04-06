package com.lbsp.promotion.util.http.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.exception.CommonRunException;
import com.lbsp.promotion.entity.exception.HttpClientException;
import com.lbsp.promotion.util.handler.LogUtil;
import com.lbsp.promotion.util.json.JsonMapper;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class GenericHttpRequest extends AbstractGenericHttpRequest {
	private String contentType = "application/json";

	public final static String DEFAULT_ENCODING = "utf-8";
	private String encoding = DEFAULT_ENCODING;
	private RequestParam param = new GenericHttpParam();
	private String endpoint = "";
	private Object requestEntity;
	private RequestType requestType = RequestType.PARAM_IN_URL;

	private RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public enum RequestType {
		PARAM_IN_URL, PARAM_IN_BODY
	}

	public GenericHttpRequest(String domain, RequestParam param, String endpoint) {
		super(domain);
		this.param = param;
		this.endpoint = endpoint;
	}

	public GenericHttpRequest(String domain, RequestParam param,
			String endpoint, String contentType) {
		super(domain);
		this.param = param;
		this.endpoint = endpoint;
		this.contentType = contentType;
	}

	public GenericHttpRequest(String domain, RequestParam param,
			String endpoint, String contentType, RequestType requestType) {
		super(domain);
		this.param = param;
		this.endpoint = endpoint;
		this.contentType = contentType;
		this.requestType = requestType;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public GenericHttpRequest() {
		super();
	}

	public GenericHttpRequest(RequestParam param) {
		super();
		this.param = param;
	}

	public GenericHttpRequest(RequestParam param, String endpoint) {
		super();
		this.param = param;
		this.endpoint = endpoint;
	}

	public GenericHttpRequest(RequestParam param, String endpoint,
			Object reqestEntity) {
		super();
		this.param = param;
		this.endpoint = endpoint;
		this.requestEntity = reqestEntity;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public RequestParam getParam() {
		return param;
	}

	public void setParam(RequestParam param) {
		this.param = param;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Object getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(Object requestEntity) {
		this.requestEntity = requestEntity;
	}

	public String getEncoding() {
		return encoding;
	}

	private String getUri() {
		if (this.endpoint == null || "".equals(this.endpoint)) {
			return this.getUrl();
		} else {
			return this.getUrl() + "/" + this.endpoint;
		}
	}
	private String http(HttpMethod method) {
		return http(method,false);
	}
	private String http(HttpMethod method,boolean upload) {
		if(!upload){
			if (StringUtils.isNotBlank(contentType)) {
				method.setRequestHeader("content-type", contentType + ";charset="
						+ encoding);
			}else{
				method.setRequestHeader("Content-Type","text/html;charset="+encoding);
			}
		}
		try {
			if (this.getClient().executeMethod(method) == 200) {
				String json = method.getResponseBodyAsString();
				System.out.println("http response:" + json);
				if (json != null && !"".equals(json)) {
					return json;
				}
			} else {
				LogUtil.logDebug("请求错误:" + method.getResponseBodyAsString());
			}
		} catch (Exception e) {
			LogUtil.logError(e);
			throw new HttpClientException("HttpClient request exception" + e);
		}
		return null;
	}

	private String getQueryString() {
		if (this.param.size() > 0) {
			StringBuffer queryString = new StringBuffer();
			for (Entry<String, String> entry : param.entrySet()) {
				queryString.append(entry.getKey());
				queryString.append("=");
				queryString.append(entry.getValue());
				queryString.append("&");
			}
			return queryString.substring(0, queryString.length() - 1);
		}
		return "";
	}

	private NameValuePair[] getQueryPair() {
		NameValuePair[] pairs = new NameValuePair[param.entrySet().size()];
		int i = 0;
		for (Entry<String, String> entry : param.entrySet()) {
			pairs[i] = new NameValuePair(entry.getKey(), entry.getValue());
			i++;
		}
		return pairs;
	}

	private Map<String, String> getQueryMap() {
		Map<String, String> queryMap = null;
		if (this.param.size() > 0) {
			queryMap = new HashMap<String, String>();
			for (Entry<String, String> entry : param.entrySet()) {
				queryMap.put(entry.getKey(), entry.getValue());
			}
		}
		return queryMap;
	}

	public String get() {
		GetMethod get = new GetMethod(this.getUri());
		get.setQueryString(EncodingUtil.formUrlEncode(this.getQueryPair(), encoding));
		return this.http(get);
	}

	private RequestEntity requestBefore() {
		RequestEntity entity = null;
		try {
			if (this.getContentType().equals(
					GenericConstants.APPLICATION_FORM_URLENCODED_VALUE)) {
				entity = new StringRequestEntity(this.getQueryString(),
						contentType, encoding);
			} else if (this.getContentType().equals(
					GenericConstants.APPLICATION_FORM_JSON_VALUE)) {
				if (this.requestEntity != null) {
					String json = JsonMapper.getJsonMapperInstance().toJson(
							this.requestEntity);
					entity = new StringRequestEntity(json, contentType,
							encoding);
				} else {
					Map<String, String> queryMap = this.getQueryMap();
					if (queryMap != null) {
						String json = JsonMapper.getJsonMapperInstance()
								.toJson(this.getQueryMap());
						entity = new StringRequestEntity(json, contentType,
								encoding);
					}
				}
			} else if (this.requestEntity != null
					&& this.requestEntity instanceof RequestEntity) {
				entity = (RequestEntity) this.requestEntity;
			}
		} catch (UnsupportedEncodingException e) {
			throw new CommonRunException("encodeing unsupported exception");
		}
		return entity;
	}

	public String post() {
		PostMethod post = new PostMethod(this.getUri());
		if (this.getRequestType() == RequestType.PARAM_IN_BODY) {
			RequestEntity entity = this.requestBefore();
			if (entity != null) {
				post.setRequestEntity(entity);
			}
			if (!param.isEmpty()) {
				post.setQueryString(EncodingUtil.formUrlEncode(this.getQueryPair(), encoding));
			}
		} else {
			post.setQueryString(EncodingUtil.formUrlEncode(this.getQueryPair(), encoding));
		}
		return this.http(post);
	}

	public String put() {
		PutMethod put = new PutMethod(this.getUri());
		if (this.getRequestType() == RequestType.PARAM_IN_BODY) {
			RequestEntity entity = this.requestBefore();
			if (entity != null) {
				put.setRequestEntity(entity);
			}
			if (!param.isEmpty()) {
				put.setQueryString(this.getQueryPair());
			}
		} else {
			put.setQueryString(this.getQueryPair());
		}
		return this.http(put);
	}

	public String delete() {
		DeleteMethod delete = new DeleteMethod(this.getUri());
		delete.setQueryString(this.getQueryPair());
		return this.http(delete);
	}

	public <T> BaseResult<T> get(TypeReference<BaseResult<T>> type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		return JsonMapper.getJsonMapperInstance().readValue(this.get(), type);
	}

	public <T> BaseResult<T> post(TypeReference<BaseResult<T>> type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		return JsonMapper.getJsonMapperInstance().readValue(this.post(), type);
	}

	public <T> BaseResult<T> put(TypeReference<BaseResult<T>> type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		return JsonMapper.getJsonMapperInstance().readValue(this.put(), type);
	}

	public <T> BaseResult<T> delete(TypeReference<BaseResult<T>> type) {
		if (type == null) {
			throw new RuntimeException("type is null");
		}
		return JsonMapper.getJsonMapperInstance()
				.readValue(this.delete(), type);
	}

	public <T> T post(Class<T> cls, ResponseType type) {
		if (type == ResponseType.urlParam) {
			return this.post(cls);
		} else {
			return JsonMapper.getJsonMapperInstance().readValue(this.post(),
					cls);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hentre.all580.util.http.request.HttpRequest#postMutilPart(org.apache
	 * .commons.httpclient.methods.multipart.Part[])
	 */
	public String postMutilPart(Part[] parts) {
		PostMethod post = new PostMethod(this.getUri());
		setContentType("");
		RequestEntity entity = new MultipartRequestEntity(parts,
				post.getParams());
		post.setRequestEntity(entity);
		post.setQueryString(this.getQueryPair());
		return http(post,true);
	}

}
