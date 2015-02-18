package com.lbsp.promotion.core.base.dao;

import com.lbsp.promotion.entity.query.GenericQueryParam;

import java.util.List;

public interface BaseDao<T> {
	public String getTableName();
	public String getPrimaryKey();

	public int insert(T obj);

	public int update(T obj);
	
	public int update(T obj, GenericQueryParam param);

	public int delete(String id);

	public T findOne(String id);

	public T findOne(String id, String[] fields);

	public List<T> find(GenericQueryParam param);

	public List<T> find(GenericQueryParam param, String[] fields);

	public int count(GenericQueryParam param);

	public int delete(Object id);
	
	public int delete(GenericQueryParam param);

	public T findOne(Object id);

	public T findOne(Object id, String[] fields);
	
	public void insertAll(List<T> objs);

    public int update(T obj, List<String> filter);

    public int update(T obj, List<String> filter, GenericQueryParam param);
}
