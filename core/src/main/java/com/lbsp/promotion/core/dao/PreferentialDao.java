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

    /**
     * 保存映射关系
     *
     * @param pre
     * @return
     */
    int savePreferentialWithCategory(@Param("pre")PreferentialRsp pre);

    /**
     * 更新映射关系
     *
     * @param org_id
     * @param id
     * @param category_id
     * @param org_category_id
     * @param user
     * @param time
     * @return
     */
    int updatePreferentialWithCategory(@Param("org_id")Integer org_id,
                                       @Param("id")Integer id,
                                       @Param("category_id")Integer category_id,
                                       @Param("org_category_id")Integer org_category_id,
                                       @Param("user")Integer user,
                                       @Param("time")Long time);

    /**
     * 删除映射关系
     *
     * @param id
     * @return
     */
    int deletePreferentialWithCategory(@Param("id")Integer id);


    /**
     *
     * 批量删除映射关系
     *
     * @param ids
     * @return
     */
    int deleteBatchPreferentialWithCategory(@Param("ids")List<Integer> ids);

    /**
     * 获取最近插入数据的自增ID
     *
     * @return
     */
    int getLastId();
}