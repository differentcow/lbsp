/**
 * 
 */
package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.FeedbackRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barry
 *
 */
public interface FeedbackDao {

    /**
     * 通过ID获取信息
     *
     * @param id
     * @return
     */
    FeedbackRsp getDetailById(@Param("id")Integer id);

    /**
     * 获取信息集合总记录数
     *
     * @param name
     * @param title
     * @param type
     * @param from
     * @param to
     * @return
     */
    int getListCount(@Param("name")String name,
                     @Param("title")String title,
                     @Param("type")Integer type,
                     @Param("from")Long from,
                     @Param("to")Long to);

    /**
     * 获取信息集合(分页)
     *
     * @param name
     * @param title
     * @param type
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
    List<FeedbackRsp> getList(@Param("name")String name,
                              @Param("title")String title,
                              @Param("type")Integer type,
                              @Param("from")Long from,
                              @Param("to")Long to,
                              @Param("start")Integer start,
                              @Param("size")Integer size);

    /**
     * 批量删除信息
     *
     * @param id
     * @return
     */
    int batchDelete(@Param("id")List<Integer> id);
}
