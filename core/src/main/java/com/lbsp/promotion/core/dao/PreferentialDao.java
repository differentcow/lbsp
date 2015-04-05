package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.PreferentialRsp;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-03-31 09:41:11
 *
 * @author 简易自动代码创建工具
 */
public interface PreferentialDao {

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	int batchDelete(@Param("ids")List<Integer> ids);

    /**
     * 获取信息集合总记录数
     *
     * @param type
     * @param title
     * @param shop
     * @param status
     * @param from
     * @param to
     * @return
     */
    int getListCount(@Param("type")String type,
                     @Param("title")String title,
                     @Param("shop")String shop,
                     @Param("status")Integer status,
                     @Param("startTime")Long from,
                     @Param("endTime")Long to);

    /**
     * 获取信息集合(分页)
     *
     * @param type
     * @param title
     * @param shop
     * @param status
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
    List<PreferentialRsp> getList(@Param("type")String type,
                                  @Param("title")String title,
                                  @Param("shop")String shop,
                                  @Param("status")Integer status,
                                  @Param("startTime")Long from,
                                  @Param("endTime")Long to,
                                  @Param("start")Integer start,
                                  @Param("size")Integer size);

    /**
     * 通过ID获取信息
     *
     * @param id
     * @return
     */
    PreferentialRsp getDetailById(@Param("id")Integer id);

}