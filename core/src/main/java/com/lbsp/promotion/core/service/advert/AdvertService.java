package com.lbsp.promotion.core.service.advert;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Advert;

import java.util.List;

/**
 *
 * Created on 2015-04-01 09:21:47
 *
 * @author 简易自动代码创建工具
 */
public interface AdvertService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	Advert getDetailById(Integer id);

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
	 * @param advert
	 * @return 
	 */
	boolean saveAdvert(Advert advert );

	/**
	 *
	 * 更新信息
	 *
	 * @param advert
	 * @return 
	 */
	boolean updateAdvert(Advert advert );

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	boolean deleteAdvert(Integer id);

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	boolean batchDeleteAdvert(List<Integer> ids);

}