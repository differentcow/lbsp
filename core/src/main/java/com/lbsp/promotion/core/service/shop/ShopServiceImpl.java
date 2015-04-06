package com.lbsp.promotion.core.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbsp.promotion.core.dao.ShopDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Shop;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.ShopRsp;

/**
 *
 * Created on 2015-04-01 09:20:42
 *
 * @author 简易自动代码创建工具
 */
@Service
public class ShopServiceImpl extends BaseServiceImpl<Shop> implements
		ShopService<Shop> {

	@Autowired
	private ShopDao shopDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public ShopRsp getDetailById(Integer id){
        return shopDao.getDetailById(id);
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
	public PageResultRsp getPageList(String user,String sell,String name,String address,Integer status,Long from,Long to,Integer start,Integer size){
        int count = shopDao.getListCount(user, sell, name, address, status, from, to);
        List<ShopRsp> list = shopDao.getList(user, sell, name, address, status, from, to,start,size);
        PageResultRsp page = new PageResultRsp();
        page.loadPageInfo(count);
        page.setResult(list);
        return page;
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param shop
	 * @return 
	 */
	@Transactional
	public boolean saveShop(Shop shop ) {
		Long currentTime = System.currentTimeMillis();
		if (shop.getCreate_time() == null){
			shop.setCreate_time(currentTime);
		}
		if (shop.getUpdate_time() == null){
			shop.setUpdate_time(currentTime);
		}
		return this.insert(shop) > 0;
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param shop
	 * @return 
	 */
	@Transactional
	public boolean updateShop(Shop shop ) {
		Long currentTime = System.currentTimeMillis();
		if (shop.getUpdate_time() == null){
			shop.setUpdate_time(currentTime);
		}
		return this.update(shop) > 0;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deleteShop(Integer id) {
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
	public boolean batchDeleteShop(List<Integer> ids){
		return shopDao.batchDelete(ids) > 0;
	}

}