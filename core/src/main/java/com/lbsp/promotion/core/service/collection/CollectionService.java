package com.lbsp.promotion.core.service.collection;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.CollectionTable;

import java.util.List;

/**
 *
 * Created on 2015-03-22 09:48:51
 *
 * @author 简易自动代码创建工具
 */
public interface CollectionService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	CollectionTable getDetailById(Integer id);

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
	PageResultRsp getPageList(String name,String type,Long from,Long to,Integer start,Integer size);

	boolean saveCollection(CollectionTable Collection );

	boolean updateCollection(CollectionTable Collection );

	boolean deleteCollection(Integer id);

	boolean batchDeleteCollection(List<Integer> ids);

}