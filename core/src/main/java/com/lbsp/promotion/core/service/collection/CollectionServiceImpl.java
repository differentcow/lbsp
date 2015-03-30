package com.lbsp.promotion.core.service.collection;

import com.lbsp.promotion.core.dao.CollectionDao;;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.CollectionTable;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.CollectionRsp;
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
public class CollectionServiceImpl extends BaseServiceImpl<CollectionTable> implements
		CollectionService<CollectionTable> {

	@Autowired
	private CollectionDao collectionDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public CollectionTable getDetailById(Integer id){
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
	public PageResultRsp getPageList(Integer customerId,String name,String type,Long from,Long to,Integer start,Integer size){
        int count = collectionDao.getListCount(customerId,type, name, from, to);
        List<CollectionRsp> list = collectionDao.getList(customerId,type, name, from, to,start,size);
        PageResultRsp page = new PageResultRsp();
        page.loadPageInfo(count);
        page.setResult(list);
		return page;
	}

	@Transactional
	public boolean saveCollection(CollectionTable Collection ) {
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
	public boolean updateCollection(CollectionTable Collection ) {
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
		return collectionDao.batchDelete(ids) > 0;
	}

}