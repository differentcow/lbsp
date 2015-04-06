package com.lbsp.promotion.core.service.page;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.PageOperate;

public interface PageOperateService<T> extends BaseService<T> {

    /**
     * 查询所有的页面资源
     *
     * @return
     */
    List<PageOperate> allPages();
}
