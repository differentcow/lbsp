package com.lbsp.promotion.core.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbsp.promotion.core.base.dao.BaseDao;
import com.lbsp.promotion.core.base.dao.BaseDaoImpl;
import com.lbsp.promotion.core.dao.GenericDao;
import com.lbsp.promotion.entity.query.GenericQueryParam;

public abstract class BaseServiceImpl<T> implements BaseService<T>,
		InitializingBean {
	private BaseDao<T> baseDao;
	private Class<T> cls;
	@Autowired
	private GenericDao genericDao;

	public BaseServiceImpl() {
		this.cls = ((Class) ((java.lang.reflect.ParameterizedType) super
				.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public int insert(T obj) {
		return this.baseDao.insert(obj);
	}

	public int update(T obj) {
		return this.baseDao.update(obj);
	}

	public int delete(String id) {
		return this.baseDao.delete(id);
	}

	public T findOne(String id) {
		return this.baseDao.findOne(id);
	}

	public T findOne(String id, String[] fields) {
		return this.baseDao.findOne(id, fields);
	}

	public List<T> find(GenericQueryParam param) {
		return this.baseDao.find(param);
	}

	public List<T> find(GenericQueryParam param, String[] fields) {
		return this.baseDao.find(param, fields);
	}

	public int count(GenericQueryParam param) {
		return this.baseDao.count(param);
	}

	public List<T> findAll(GenericQueryParam param) {
		param.setNeedPaging(false);
		return find(param);
	}

	public List<T> findAll(GenericQueryParam param, String[] fields) {
		param.setNeedPaging(false);
		return find(param, fields);
	}

	public void afterPropertiesSet() throws Exception {
		baseDao = new BaseDaoImpl<T>(this.cls, this.genericDao);
	}

	public int insertAll(List<T> objs) {
		this.baseDao.insertAll(objs);
		return objs.size();
	}

	public int updateAll(List<T> objs) {
		int resultCount = 0;
		for (T obj : objs) {
			this.update(obj);
			resultCount++;

		}
		return resultCount;
	}

	public int delete(GenericQueryParam param) {
		return this.baseDao.delete(param);
	}

	public T findOne(Object id) {
		return this.findOne(String.valueOf(id));
	}

	public int update(T obj, GenericQueryParam param) {
		return this.baseDao.update(obj, param);
	}

	public T findOne(GenericQueryParam param) {
		List<T> list = this.baseDao.find(param);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public T findOne(GenericQueryParam param, String[] fields) {
		List<T> list = this.baseDao.find(param, fields);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public int update(T obj, List<String> filter, GenericQueryParam param) {
		return this.baseDao.update(obj, filter, param);
	}

	public int update(T obj, List<String> filter) {
		return this.baseDao.update(obj, filter);
	}
}
