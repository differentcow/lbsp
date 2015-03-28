package com.lbsp.promotion.core.service.customer;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Customer;

import java.util.List;

/**
 *
 * Created on 2015-03-24 12:01:27
 *
 * @author 简易自动代码创建工具
 */
public interface CustomerService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	Customer getDetailById(Integer id);

    /**
     * 获取信集合(分页)
     *
     * @param type
     * @param account
     * @param name
     * @param status
     * @param gender
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
	PageResultRsp getPageList(String type,String account,String name,Integer status,Integer gender,Long from,Long to,Integer start,Integer size);

	/**
	 *
	 * 保存信息
	 *
	 * @param customer
	 * @return 
	 */
	boolean saveCustomer(Customer customer );

	/**
	 *
	 * 更新信息
	 *
	 * @param customer
	 * @return 
	 */
	boolean updateCustomer(Customer customer );

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	boolean deleteCustomer(Integer id);

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	boolean batchDeleteCustomer(List<Integer> ids);

}