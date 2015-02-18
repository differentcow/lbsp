package com.lbsp.promotion.core.service.privilege;

import com.lbsp.promotion.core.dao.PrivilegeDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Privilege;
import com.lbsp.promotion.entity.response.OperateResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege> implements
        PrivilegeService<Privilege> {


    @Autowired
    private PrivilegeDao privilegeDao;

    /**
     * 根据条件查询所有能访问的页面资源
     *
     * @param master
     * @param masterValues
     * @return
     */
    public List<OperateResource> findEnabledPageByMasterandAccess(String master,List<String> masterValues){
        return privilegeDao.findOperateByFilter(GenericConstants.LBSP_PRIVILEGE_ACCESS_TYPE_PAGE,
                                                GenericConstants.LBSP_PRIVILEGE_OPERATION_ENABLED,
                                                master,masterValues);
    }

    /**
     * 根据条件查询所有能访问的功能操作资源
     *
     * @param master
     * @param masterValues
     * @return
     */
    public List<OperateResource> findEnabledFuncByMasterandAccess(String master,List<String> masterValues){
        return privilegeDao.findOperateByFilter(GenericConstants.LBSP_PRIVILEGE_ACCESS_TYPE_FUNC,
                GenericConstants.LBSP_PRIVILEGE_OPERATION_ENABLED,
                master,masterValues);
    }


}
