package com.lbsp.promotion.entity.base;

public class PageInfoRsp {

	private Integer page;
	private Integer pageSize;
	private Integer total;
	
	
	public PageInfoRsp() {
		super();
	}

	public PageInfoRsp(Integer total) {
		super();
		this.total = total;
	}

	public PageInfoRsp(Integer page, Integer pageSize, Integer total) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
	}

	
	
	public PageInfoRsp(Integer page, Integer pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "PageInfo [page=" + page + ", pageSize=" + pageSize + ", total="
				+ total + "]";
	}
}
