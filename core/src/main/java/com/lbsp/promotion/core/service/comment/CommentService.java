package com.lbsp.promotion.core.service.comment;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Comment;
import com.lbsp.promotion.entity.response.CommentRsp;

import java.util.List;

public interface CommentService<T> extends BaseService<T> {

    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    CommentRsp getDetailById(Integer id);


    /**
     * 获取信集合(分页)
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
    PageResultRsp getPageList(Integer type,String name,String title,Long from,Long to,Integer start,Integer size);


    /**
     * 保存信息
     *
     * @param comment
     * @return
     */
    boolean saveComment(Comment comment);

    /**
     * 更新信息
     *
     * @param comment
     * @return
     */
    boolean updateComment(Comment comment);

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    boolean deleteComment(Integer id);

    /**
     * 批量删除信息
     *
     * @param id
     * @return
     */
    boolean batchDeleteComment(List<Integer> id);

    /**
     * 批量更新信息状态
     *
     * @param id
     * @param status
     * @param updateTime
     * @param updateUser
     * @return
     */
    boolean batchUpdateCommentStatus(List<Integer> id,Integer status,Long updateTime,Integer updateUser);

}
