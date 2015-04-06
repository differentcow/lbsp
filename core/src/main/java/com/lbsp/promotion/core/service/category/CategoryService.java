package com.lbsp.promotion.core.service.category;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Category;

/**
 *
 * Created on 2015-04-01 09:21:24
 *
 * @author 简易自动代码创建工具
 */
public interface CategoryService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	Category getDetailById(Integer id);

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
	PageResultRsp getPageList(Long from,Long to,Integer start,Integer size);

	/**
	 *
	 * 保存信息
	 *
	 * @param category
	 * @return 
	 */
	boolean saveCategory(Category category );

	/**
	 *
	 * 更新信息
	 *
	 * @param category
	 * @return 
	 */
	boolean updateCategory(Category category );

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	boolean deleteCategory(Integer id);

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	boolean batchDeleteCategory(List<Integer> ids);

}