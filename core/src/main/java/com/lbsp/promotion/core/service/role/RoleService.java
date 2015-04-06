package com.lbsp.promotion.core.service.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.Role;

public interface RoleService<T> extends BaseService<T> {


    /**
     * 通过用户ID查询用户所拥有的所有角色
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(@Param("userId") Integer userId);

}
