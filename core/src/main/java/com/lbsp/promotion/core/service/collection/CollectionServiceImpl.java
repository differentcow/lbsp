package com.lbsp.promotion.core.service.collection;

import com.lbsp.promotion.core.dao.CollectionDao;;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Collection;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created on 2015-03-22 09:48:51
 *
 * @author 简易自动代码创建工具
 */
@Service
public class CollectionServiceImpl extends BaseServiceImpl<Collection> implements
		CollectionService<Collection> {

	@Autowired
	private CollectionDao CollectionDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public Collection getDetailById(Integer id){
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
		return this.findOne(param);
	}

	/**
	 *
	 * 获取信集合(分页)
	 *
	 * @param from
	 * @param to
	 * @param start
	 * @param size
	 * @return 
	 */
	public PageResultRsp getPageList(Long from,Long to,Integer start,Integer size){
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("create_time", QueryKey.Operators.GTE),from);
		param.put(new QueryKey("create_time", QueryKey.Operators.LTE),to);
		int count = this.count(param);
		param.setNeedPaging(true);
		param.setOffset(start);
		param.setPageSize(size);
		List list = this.find(param);
		PageResultRsp page = new PageResultRsp();
		page.loadPageInfo(count);
		page.setResult(list);
		return page;
	}

	@Transactional
	public boolean saveCollection(Collection Collection ) {
		Long currentTime = System.currentTimeMillis();
		if (Collection.getCreate_time() == null){
			Collection.setCreate_time(currentTime);
		}
		if (Collection.getUpdate_time() == null){
			Collection.setUpdate_time(currentTime);
		}
		return this.insert(Collection) > 0;
	}

	@Transactional
	public boolean updateCollection(Collection Collection ) {
		Long currentTime = System.currentTimeMillis();
		if (Collection.getUpdate_time() == null){
			Collection.setUpdate_time(currentTime);
		}
		return this.update(Collection) > 0;
	}

	@Transactional
	public boolean deleteCollection(Integer id) {
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
		return this.delete(param) > 0;
	}

	@Transactional
	public boolean batchDeleteCollection(List<Integer> ids){
		return CollectionDao.batchDelete(ids) > 0;
	}

}