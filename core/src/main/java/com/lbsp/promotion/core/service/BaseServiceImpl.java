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

	@Override
	public int insert(T obj) {
		return this.baseDao.insert(obj);
	}

	@Override
	public int update(T obj) {
		return this.baseDao.update(obj);
	}

	@Override
	public int delete(String id) {
		return this.baseDao.delete(id);
	}

	@Override
	public T findOne(String id) {
		return this.baseDao.findOne(id);
	}

	@Override
	public T findOne(String id, String[] fields) {
		return this.baseDao.findOne(id, fields);
	}

	@Override
	public List<T> find(GenericQueryParam param) {
		return this.baseDao.find(param);
	}

	@Override
	public List<T> find(GenericQueryParam param, String[] fields) {
		return this.baseDao.find(param, fields);
	}

	@Override
	public int count(GenericQueryParam param) {
		return this.baseDao.count(param);
	}

	@Override
	public List<T> findAll(GenericQueryParam param) {
		param.setNeedPaging(false);
		return find(param);
	}

	@Override
	public List<T> findAll(GenericQueryParam param, String[] fields) {
		param.setNeedPaging(false);
		return find(param, fields);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		baseDao = new BaseDaoImpl<T>(this.cls, this.genericDao);
	}

	@Override
	public int insertAll(List<T> objs) {
		this.baseDao.insertAll(objs);
		return objs.size();
	}

	@Override
	public int updateAll(List<T> objs) {
		int resultCount = 0;
		for (T obj : objs) {
			this.update(obj);
			resultCount++;

		}
		return resultCount;
	}

	@Override
	public int delete(GenericQueryParam param) {
		return this.baseDao.delete(param);
	}

	@Override
	public T findOne(Object id) {
		return this.findOne(String.valueOf(id));
	}

	@Override
	public int update(T obj, GenericQueryParam param) {
		return this.baseDao.update(obj, param);
	}

	@Override
	public T findOne(GenericQueryParam param) {
		List<T> list = this.baseDao.find(param);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public T findOne(GenericQueryParam param, String[] fields) {
		List<T> list = this.baseDao.find(param, fields);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int update(T obj, List<String> filter, GenericQueryParam param) {
		return this.baseDao.update(obj, filter, param);
	}

	@Override
	public int update(T obj, List<String> filter) {
		return this.baseDao.update(obj, filter);
	}
}
