package com.lbsp.promotion.core.service.shop;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Shop;
import com.lbsp.promotion.entity.response.ShopRsp;

/**
 *
 * Created on 2015-04-01 09:20:42
 *
 * @author 简易自动代码创建工具
 */
public interface ShopService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
    ShopRsp getDetailById(Integer id);

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
	PageResultRsp getPageList(String user,String sell,String name,String address,Integer status,Long from,Long to,Integer start,Integer size);

	/**
	 *
	 * 保存信息
	 *
	 * @param shop
	 * @return 
	 */
	boolean saveShop(Shop shop );

	/**
	 *
	 * 更新信息
	 *
	 * @param shop
	 * @return 
	 */
	boolean updateShop(Shop shop );

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	boolean deleteShop(Integer id);

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	boolean batchDeleteShop(List<Integer> ids);

}