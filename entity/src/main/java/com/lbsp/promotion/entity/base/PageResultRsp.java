package com.lbsp.promotion.entity.base;

import java.util.List;

public class PageResultRsp {
    private PageInfoRsp pageInfo;
	private List result;

    public void loadPageInfo(Integer total){
        if(pageInfo == null){
            pageInfo = new PageInfoRsp();
        }
        pageInfo.setTotal(total);
    }

    public void loadPageInfo(Integer page,Integer pageSize,Integer total){
        if(pageInfo == null){
            pageInfo = new PageInfoRsp();
        }
        pageInfo.setPage(page);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
    }

    public PageInfoRsp getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoRsp pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }
}
