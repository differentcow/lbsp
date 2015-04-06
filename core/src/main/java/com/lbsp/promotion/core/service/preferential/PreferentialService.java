package com.lbsp.promotion.core.service.preferential;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Preferential;
import com.lbsp.promotion.entity.response.PreferentialRsp;

/**
 *
 * Created on 2015-03-31 09:41:11
 *
 * @author 简易自动代码创建工具
 */
public interface PreferentialService<T> extends BaseService<T> {


	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
    PreferentialRsp getDetailById(Integer id);

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
    PageResultRsp getPageList(String type,String title,Integer status,String shop,Long from,Long to,Integer start,Integer size);

	/**
	 *
	 * 保存信息
	 *
	 * @param preferential
	 * @return 
	 */
	boolean savePreferential(Preferential preferential );

	/**
	 *
	 * 更新信息
	 *
	 * @param preferential
	 * @return 
	 */
	boolean updatePreferential(Preferential preferential );

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	boolean deletePreferential(Integer id);

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	boolean batchDeletePreferential(List<Integer> ids);

}