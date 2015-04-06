package com.lbsp.promotion.core.service.page;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.model.PageOperate;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.SortCond;

@Service
public class PageOperateServiceImpl extends BaseServiceImpl<PageOperate> implements
        PageOperateService<PageOperate> {


    /**
     * 查询所有的页面资源
     *
     * @return
     */
    public List<PageOperate> allPages(){
        GenericQueryParam param = new GenericQueryParam();
        param.addSortCond(new SortCond("update_time", SortCond.Order.DESC));
        return this.findAll(param);
    }



}
