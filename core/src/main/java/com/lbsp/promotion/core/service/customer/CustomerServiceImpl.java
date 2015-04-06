package com.lbsp.promotion.core.service.customer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbsp.promotion.core.dao.CustomerDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Customer;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;

/**
 *
 * Created on 2015-03-24 12:01:27
 *
 * @author 简易自动代码创建工具
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements
		CustomerService<Customer> {

	@Autowired
	private CustomerDao customerDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public Customer getDetailById(Integer id){
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
		return this.findOne(param);
	}

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
	public PageResultRsp getPageList(String type,String account,String name,Integer status,Integer gender,Long from,Long to,Integer start,Integer size){
		GenericQueryParam param = new GenericQueryParam();
        if (from != null)
		    param.put(new QueryKey("create_time", QueryKey.Operators.GTE),from);
        if (to != null)
            param.put(new QueryKey("create_time", QueryKey.Operators.LTE),to);
        if (StringUtils.isNotBlank(type))
            param.put(new QueryKey("type", QueryKey.Operators.EQ),type);
        if (StringUtils.isNotBlank(account))
            param.put(new QueryKey("account", QueryKey.Operators.LIKE),account);
        if (StringUtils.isNotBlank(name))
            param.put(new QueryKey("name", QueryKey.Operators.LIKE),name);
        if(status != null)
            param.put(new QueryKey("status", QueryKey.Operators.EQ),status);
        if(gender != null)
            param.put(new QueryKey("gender", QueryKey.Operators.EQ),gender);
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
	 * @param customer
	 * @return 
	 */
	@Transactional
	public boolean saveCustomer(Customer customer ) {
		Long currentTime = System.currentTimeMillis();
		if (customer.getCreate_time() == null){
			customer.setCreate_time(currentTime);
		}
		if (customer.getUpdate_time() == null){
			customer.setUpdate_time(currentTime);
		}
		return this.insert(customer) > 0;
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param customer
	 * @return 
	 */
	@Transactional
	public boolean updateCustomer(Customer customer ) {
		Long currentTime = System.currentTimeMillis();
		if (customer.getUpdate_time() == null){
			customer.setUpdate_time(currentTime);
		}
		return this.update(customer) > 0;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deleteCustomer(Integer id) {
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
	public boolean batchDeleteCustomer(List<Integer> ids){
		return customerDao.batchDelete(ids) > 0;
	}

}