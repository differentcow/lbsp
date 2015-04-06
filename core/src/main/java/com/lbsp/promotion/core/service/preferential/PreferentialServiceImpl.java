package com.lbsp.promotion.core.service.preferential;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbsp.promotion.core.dao.PreferentialDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Preferential;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.PreferentialRsp;

/**
 *
 * Created on 2015-03-31 09:41:11
 *
 * @author 简易自动代码创建工具
 */
@Service
public class PreferentialServiceImpl extends BaseServiceImpl<Preferential> implements
		PreferentialService<Preferential> {

	@Autowired
	private PreferentialDao preferentialDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public PreferentialRsp getDetailById(Integer id){
		return preferentialDao.getDetailById(id);
	}

	/**
	 *
	 * 获取信集合(分页)
	 *
     * @param type
     * @param title
     * @param status
     * @param shop
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
	 */
	public PageResultRsp getPageList(String type,String title,Integer status,String shop,Long from,Long to,Integer start,Integer size){
		int count = preferentialDao.getListCount(type,title,shop,status,from,to);
		List list = preferentialDao.getList(type,title,shop,status,from,to,start,size);
		PageResultRsp page = new PageResultRsp();
		page.loadPageInfo(count);
		page.setResult(list);
		return page;
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param preferential
	 * @return 
	 */
	@Transactional
	public boolean savePreferential(Preferential preferential ) {
		Long currentTime = System.currentTimeMillis();
		if (preferential.getCreate_time() == null){
			preferential.setCreate_time(currentTime);
		}
		if (preferential.getUpdate_time() == null){
			preferential.setUpdate_time(currentTime);
		}
		return this.insert(preferential) > 0;
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param preferential
	 * @return 
	 */
	@Transactional
	public boolean updatePreferential(Preferential preferential ) {
		Long currentTime = System.currentTimeMillis();
		if (preferential.getUpdate_time() == null){
			preferential.setUpdate_time(currentTime);
		}
		return this.update(preferential) > 0;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deletePreferential(Integer id) {
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
	public boolean batchDeletePreferential(List<Integer> ids){
		return preferentialDao.batchDelete(ids) > 0;
	}

}