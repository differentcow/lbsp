package com.lbsp.promotion.core.service.comment;

import com.lbsp.promotion.core.dao.CommentDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Comment;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.CommentRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements
        CommentService<Comment> {


    @Autowired
    private CommentDao commentDao;

    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    public CommentRsp getDetailById(Integer id){
        return commentDao.getDetailById(id);
    }

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
    public PageResultRsp getPageList(Integer type,String name,String title,Long from,Long to,Integer start,Integer size){
        int count = commentDao.getListCount(type, name, title, from, to);
        List<CommentRsp> list = commentDao.getList(type, name, title, from, to,start,size);
        PageResultRsp page = new PageResultRsp();
        page.loadPageInfo(count);
        page.setResult(list);
        return page;
    }


    /**
     * 保存信息
     *
     * @param comment
     * @return
     */
    @Transactional
    public boolean saveComment(Comment comment){
        Long currentTime = System.currentTimeMillis();
        if (comment.getCreate_time() == null){
            comment.setCreate_time(currentTime);
        }
        if (comment.getUpdate_time() == null){
            comment.setUpdate_time(currentTime);
        }
        return this.insert(comment) > 0;
    }

    /**
     * 更新信息
     *
     * @param comment
     * @return
     */
    @Transactional
    public boolean updateComment(Comment comment){
        Long currentTime = System.currentTimeMillis();
        if (comment.getUpdate_time() == null){
            comment.setUpdate_time(currentTime);
        }
        return this.update(comment) > 0;
    }

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteComment(Integer id){
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        return this.delete(param) > 0;
    }

    /**
     * 批量删除信息
     *
     * @param id
     * @return
     */
    @Transactional
    public boolean batchDeleteComment(List<Integer> id){
        return commentDao.batchDelete(id) > 0;
    }

    /**
     * 批量更新信息状态
     *
     * @param id
     * @param status
     * @param updateTime
     * @param updateUser
     * @return
     */
    @Transactional
    public boolean batchUpdateCommentStatus(List<Integer> id,Integer status,Long updateTime,Integer updateUser){
        return commentDao.batchUpdateStatus(id,status,updateTime,updateUser) > 0;
    }

}
