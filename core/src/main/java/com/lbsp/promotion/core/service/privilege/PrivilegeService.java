package com.lbsp.promotion.core.service.privilege;

import java.util.List;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.response.OperateResource;

public interface PrivilegeService<T> extends BaseService<T> {



    /**
     * 根据条件查询所有能访问的页面资源
     *
     * @param master
     * @param masterValues
     * @return
     */
    List<OperateResource> findEnabledPageByMasterandAccess(String master,List<Integer> masterValues);

    /**
     * 根据条件查询所有能访问的功能操作资源
     *
     * @param master
     * @param masterValues
     * @return
     */
    List<OperateResource> findEnabledFuncByMasterandAccess(String master,List<Integer> masterValues);
}
