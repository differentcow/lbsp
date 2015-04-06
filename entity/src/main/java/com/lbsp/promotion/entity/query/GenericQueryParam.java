package com.lbsp.promotion.entity.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.lbsp.promotion.entity.constants.GenericConstants;

public class GenericQueryParam extends LinkedHashMap<QueryKey, Object>
		implements ListQueryParam {
	private static final long serialVersionUID = -5647772735262130086L;
	
	public static final int MAX_PAGE_SIZE = 100;
	
	private int pageSize;

	private int page;

    private int offset;
	
	private Boolean needPaging;
	
	private List<SortCond> sortCond = new ArrayList<SortCond>();

	public GenericQueryParam() {
		this(GenericConstants.DEFAULT_LIST_PAGE_INDEX, GenericConstants.DEFAULT_LIST_PAGE_SIZE);
	}

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void initDefaults() {
		if(needPaging == null){
			needPaging = true;
		}
	}

	public GenericQueryParam(Integer page, Integer pageSize) {
		this.page = page;
		this.pageSize = pageSize;
		initDefaults();
	}

	public Integer getPage() {
		return this.page;
	}

	
	
	public boolean isNeedPaging() {
		return needPaging;
	}


	public void setNeedPaging(boolean needPaging) {
		this.needPaging = needPaging;
	}


	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPage(Integer page) {
		this.page = page == null
				? GenericConstants.DEFAULT_LIST_PAGE_INDEX
				: page;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize  == null
				? GenericConstants.DEFAULT_LIST_PAGE_SIZE
				: pageSize;
	}

	public List<SortCond> getSortCond() {
		return this.sortCond;
	}

	public void addSortCond(SortCond sortCond) {
		this.sortCond.add(sortCond);
	}

	public void addSortCond(List<SortCond> sortCondList) {
		this.sortCond.addAll(sortCondList);
	}

	public QueryParam fill(QueryKey key, Object value) {
		put(key, value);
		return this;
	}
}
