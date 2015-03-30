package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.CollectionRsp;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-03-22 09:48:51
 *
 * @author 简易自动代码创建工具
 */
public interface CollectionDao {

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	int batchDelete(@Param("ids")List<Integer> ids);


    /**
     * 分页获取信息
     *
     * @param name
     * @param type
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
    List<CollectionRsp> getList(@Param("customerId")Integer customerId,
                                @Param("name")String name,
                                @Param("type")String type,
                                @Param("from")Long from,
                                @Param("to")Long to,
                                @Param("start")Integer start,
                                @Param("size")Integer size);

    /**
     * 获取信息总记录数
     *
     * @param name
     * @param type
     * @param from
     * @param to
     * @return
     */
    int getListCount(@Param("customerId")Integer customerId,
                     @Param("name")String name,
                     @Param("type")String type,
                     @Param("from")Long from,
                     @Param("to")Long to);

}