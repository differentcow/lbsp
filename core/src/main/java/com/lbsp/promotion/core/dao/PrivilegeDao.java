package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.OperateResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Barry on 2015/2/17.
 */
public interface PrivilegeDao {

    /**
     * 根据过滤条件查询操作权限资源
     *
     * @param access
     * @param operate
     * @param master
     * @param masterValues
     * @return
     */
    List<OperateResource> findOperateByFilter(@Param("access")String access,
                                              @Param("operate")String operate,
                                              @Param("master")String master,
                                              @Param("masterValues")List<String> masterValues);
}
