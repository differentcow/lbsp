package com.lbsp.promotion.core.service.function;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.FunctionOperate;

import java.util.List;

public interface FunctionOperateService<T> extends BaseService<T> {

    /**
     * 查询所有的页面资源
     *
     * @return
     */
    List<FunctionOperate> allFunctions();
}
