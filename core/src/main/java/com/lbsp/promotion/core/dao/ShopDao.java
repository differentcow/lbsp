package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.ShopRsp;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-04-01 09:20:42
 *
 * @author 简易自动代码创建工具
 */
public interface ShopDao {

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	int batchDelete(@Param("ids")List<Integer> ids);

    /**
     * 通过ID获取信息
     *
     * @param id
     * @return
     */
    ShopRsp getDetailById(@Param("id")Integer id);

    /**
     * 获取所有信息总记录数
     *
     * @param user
     * @param sell
     * @param name
     * @param address
     * @param status
     * @param from
     * @param to
     * @return
     */
    int getListCount(@Param("user")String user,
                     @Param("sell")String sell,
                     @Param("name")String name,
                     @Param("address")String address,
                     @Param("status")Integer status,
                     @Param("from")Long from,
                     @Param("to")Long to);

    /**
     * 获取信息集合(分页)
     *
     * @param user
     * @param sell
     * @param name
     * @param address
     * @param status
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
    List<ShopRsp> getList(@Param("user")String user,
                          @Param("sell")String sell,
                          @Param("name")String name,
                          @Param("address")String address,
                          @Param("status")Integer status,
                          @Param("from")Long from,
                          @Param("to")Long to,
                          @Param("start")Integer start,
                          @Param("size")Integer size);

}