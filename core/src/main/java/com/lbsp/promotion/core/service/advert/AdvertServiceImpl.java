package com.lbsp.promotion.core.service.advert;

import com.lbsp.promotion.core.dao.AdvertDao;;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Advert;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created on 2015-04-01 09:21:47
 *
 * @author 简易自动代码创建工具
 */
@Service
public class AdvertServiceImpl extends BaseServiceImpl<Advert> implements
		AdvertService<Advert> {

	@Autowired
	private AdvertDao advertDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public Advert getDetailById(Integer id){
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
	public PageResultRsp getPageList(String title,String customer,Integer status,String type, Long from,Long to,Integer start,Integer size){
		GenericQueryParam param = new GenericQueryParam();
		if(StringUtils.isNotBlank(title))
            param.put(new QueryKey("title", QueryKey.Operators.LIKE),title);
        if(StringUtils.isNotBlank(customer))
            param.put(new QueryKey("customer", QueryKey.Operators.LIKE),customer);
        if(status != null)
            param.put(new QueryKey("status", QueryKey.Operators.EQ),status);
        if(StringUtils.isNotBlank(type))
            param.put(new QueryKey("type", QueryKey.Operators.EQ),type);
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
	 * @param advert
	 * @return 
	 */
	@Transactional
	public boolean saveAdvert(Advert advert ) {
		Long currentTime = System.currentTimeMillis();
		if (advert.getCreate_time() == null){
			advert.setCreate_time(currentTime);
		}
		if (advert.getUpdate_time() == null){
			advert.setUpdate_time(currentTime);
		}
		return this.insert(advert) > 0;
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param advert
	 * @return 
	 */
	@Transactional
	public boolean updateAdvert(Advert advert ) {
		Long currentTime = System.currentTimeMillis();
		if (advert.getUpdate_time() == null){
			advert.setUpdate_time(currentTime);
		}
		return this.update(advert) > 0;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deleteAdvert(Integer id) {
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
	public boolean batchDeleteAdvert(List<Integer> ids){
		return advertDao.batchDelete(ids) > 0;
	}

}