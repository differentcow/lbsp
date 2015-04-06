package com.lbsp.promotion.core.service.function;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.FunctionOperate;

public interface FunctionOperateService<T> extends BaseService<T> {

    /**
     * 查询所有的页面资源
     *
     * @return
     */
    List<FunctionOperate> allFunctions();
}
