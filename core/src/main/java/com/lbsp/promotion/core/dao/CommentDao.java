/**
 * 
 */
package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.model.Comment;
import com.lbsp.promotion.entity.response.CommentRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barry
 *
 */
public interface CommentDao {


    /**
     * 获取所有信息总记录数
     *
     * @param type
     * @param name
     * @param title
     * @param from
     * @param to
     * @return
     */
    int getListCount(@Param("type")Integer type,
                     @Param("name")String name,
                     @Param("title")String title,
                     @Param("from")Long from,
                     @Param("to")Long to);

    /**
     * 获取信息集合(分页)
     *
     * @param type
     * @param name
     * @param title
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
     */
    List<CommentRsp> getList(@Param("type")Integer type,
                             @Param("name")String name,
                             @Param("title")String title,
                             @Param("from")Long from,
                             @Param("to")Long to,
                             @Param("start")Integer start,
                             @Param("size")Integer size);

    /**
     * 通过ID获取信息
     *
     * @param id
     * @return
     */
    CommentRsp getDetailById(@Param("id")Integer id);


    /**
     * 批量删除信息
     *
     * @param id
     * @return
     */
    int batchDelete(@Param("id")List<Integer> id);

    /**
     * 批量更新信息状态
     *
     * @param id
     * @param status
     * @return
     */
    int batchUpdateStatus(@Param("id")List<Integer> id,@Param("status")Integer status,
                          @Param("update_time")Long updateTime,@Param("update_user")Integer updateUser);
}
