package com.lbsp.promotion.core.service.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbsp.promotion.core.dao.FeedbackDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Feedback;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.FeedbackRsp;

@Service
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback> implements
        FeedbackService<Feedback> {

    @Autowired
    private FeedbackDao feedbackDao;

    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    public FeedbackRsp getDetailById(Integer id){
        return feedbackDao.getDetailById(id);
    }

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
    public PageResultRsp getPageList(String name,String title,Integer type,Long from,Long to,Integer start,Integer size){
        int count = feedbackDao.getListCount(name, title, type, from, to);
        List<FeedbackRsp> list = feedbackDao.getList(name, title, type, from, to,start,size);
        PageResultRsp page = new PageResultRsp();
        page.loadPageInfo(count);
        page.setResult(list);
        return page;
    }


    /**
     * 保存信息
     *
     * @param fb
     * @return
     */
    @Transactional
    public boolean saveFeedback(Feedback fb){
        Long currentTime = System.currentTimeMillis();
        if (fb.getCreate_time() == null){
            fb.setCreate_time(currentTime);
        }
        if (fb.getUpdate_time() == null){
            fb.setUpdate_time(currentTime);
        }
        return this.insert(fb) > 0;
    }

    /**
     * 更新信息
     *
     * @param fb
     * @return
     */
    @Transactional
    public boolean updateFeedback(Feedback fb){
        Long currentTime = System.currentTimeMillis();
        if (fb.getUpdate_time() == null){
            fb.setUpdate_time(currentTime);
        }
        return this.update(fb) > 0;
    }

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteFeedback(Integer id){
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
    public boolean batchDeleteFeedback(List<Integer> id){
        return feedbackDao.batchDelete(id) > 0;
    }

}
