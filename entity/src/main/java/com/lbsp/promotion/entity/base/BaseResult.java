package com.lbsp.promotion.entity.base;

import com.lbsp.promotion.entity.constants.GenericConstants;

import java.util.HashMap;
import java.util.Map;

public class BaseResult<T> {
    private long id;
    private Map<String,Object> state;
	private T result;
    private PageInfoRsp pageInfo;
    private boolean isOnlyResult;

    public boolean isOnlyResult() {
        return isOnlyResult;
    }

    public void setOnlyResult(boolean isOnlyResult) {
        this.isOnlyResult = isOnlyResult;
    }

    public BaseResult() {
		super();
	}

    public BaseResult(PageInfoRsp pageInfoRsp){
        super();
        this.id = System.currentTimeMillis();
        this.state = new HashMap<String, Object>();
        this.state.put("code", GenericConstants.LBSP_STATUS_SUCCESS);
        this.state.put("msg","get page list ok.");
        this.pageInfo = pageInfoRsp;
    }

    public BaseResult(int code,String msg,T result,PageInfoRsp pageInfoRsp){
        super();
        this.id = System.currentTimeMillis();
        this.state = new HashMap<String, Object>();
        this.state.put("code",code);
        this.state.put("msg",msg);
        this.result = result;
        this.pageInfo = pageInfoRsp;
    }

    public BaseResult(int code,String msg,T result){
        super();
        this.id = System.currentTimeMillis();
        this.state = new HashMap<String, Object>();
        this.state.put("code",code);
        this.state.put("msg",msg);
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public PageInfoRsp getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoRsp pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
	public String toString() {
		return "BaseResult [id=" + id + ", state=(code:" + state.get("code") + "),state=(msg:" + state.get("msg") + "), data="
				+ result + ", pageInfo=" + pageInfo + "]";
	}
}
