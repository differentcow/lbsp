package com.lbsp.promotion.core.service.function;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.model.FunctionOperate;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.SortCond;

@Service
public class FunctionOperateServiceImpl extends BaseServiceImpl<FunctionOperate> implements
        FunctionOperateService<FunctionOperate> {


    /**
     * 查询所有的页面资源
     *
     * @return
     */
    public List<FunctionOperate> allFunctions(){
        GenericQueryParam param = new GenericQueryParam();
        param.addSortCond(new SortCond("update_time", SortCond.Order.DESC));
        return this.findAll(param);
    }

}
