package com.lbsp.promotion.core.service.category;

import com.lbsp.promotion.core.dao.CategoryDao;;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Category;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created on 2015-04-01 09:21:24
 *
 * @author 简易自动代码创建工具
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements
		CategoryService<Category> {

	@Autowired
	private CategoryDao categoryDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public Category getDetailById(Integer id){
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
		if(from != null)
			param.put(new QueryKey("create_time", QueryKey.Operators.GTE),from);
		if(to != null)
			param.put(new QueryKey("create_time", QueryKey.Operators.LTE),to);
		param.addSortCond(new SortCond("update_time", SortCond.Order.DESC));
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

	/**
	 *
	 * 保存信息
	 *
	 * @param category
	 * @return 
	 */
	@Transactional
	public boolean saveCategory(Category category ) {
		Long currentTime = System.currentTimeMillis();
		if (category.getCreate_time() == null){
			category.setCreate_time(currentTime);
		}
		if (category.getUpdate_time() == null){
			category.setUpdate_time(currentTime);
		}
		return this.insert(category) > 0;
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param category
	 * @return 
	 */
	@Transactional
	public boolean updateCategory(Category category ) {
		Long currentTime = System.currentTimeMillis();
		if (category.getUpdate_time() == null){
			category.setUpdate_time(currentTime);
		}
		return this.update(category) > 0;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deleteCategory(Integer id) {
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
		return this.delete(param) > 0;
	}

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	@Transactional
	public boolean batchDeleteCategory(List<Integer> ids){
		return categoryDao.batchDelete(ids) > 0;
	}

}