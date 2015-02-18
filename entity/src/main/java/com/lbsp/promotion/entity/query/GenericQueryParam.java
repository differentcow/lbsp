package com.lbsp.promotion.entity.query;

import com.lbsp.promotion.entity.constants.GenericConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

	@Override
	public Integer getPage() {
		return this.page;
	}

	
	
	public boolean isNeedPaging() {
		return needPaging;
	}


	public void setNeedPaging(boolean needPaging) {
		this.needPaging = needPaging;
	}


	@Override
	public Integer getPageSize() {
		return this.pageSize;
	}

	@Override
	public void setPage(Integer page) {
		this.page = page == null
				? GenericConstants.DEFAULT_LIST_PAGE_INDEX
				: page;
	}

	@Override
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize  == null
				? GenericConstants.DEFAULT_LIST_PAGE_SIZE
				: pageSize;
	}

	@Override
	public List<SortCond> getSortCond() {
		return this.sortCond;
	}

	@Override
	public void addSortCond(SortCond sortCond) {
		this.sortCond.add(sortCond);
	}

	@Override
	public void addSortCond(List<SortCond> sortCondList) {
		this.sortCond.addAll(sortCondList);
	}

	@Override
	public QueryParam fill(QueryKey key, Object value) {
		put(key, value);
		return this;
	}
}
