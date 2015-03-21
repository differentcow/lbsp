package com.lbsp.promotion.core.service.feedback;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Feedback;
import com.lbsp.promotion.entity.response.FeedbackRsp;

import java.util.List;

public interface FeedbackService<T> extends BaseService<T> {

    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    FeedbackRsp getDetailById(Integer id);

    /**
     * 获取信集合(分页)
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
    PageResultRsp getPageList(String name,String title,Integer type,Long from,Long to,Integer start,Integer size);


    /**
     * 保存信息
     *
     * @param fb
     * @return
     */
    boolean saveFeedback(Feedback fb);
    /**
     * 更新信息
     *
     * @param fb
     * @return
     */
    boolean updateFeedback(Feedback fb);

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    boolean deleteFeedback(Integer id);

    /**
     * 批量删除信息
     *
     * @param id
     * @return
     */
    boolean batchDeleteFeedback(List<Integer> id);

}
