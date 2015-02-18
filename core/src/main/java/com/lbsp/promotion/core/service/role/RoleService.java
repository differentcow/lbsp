package com.lbsp.promotion.core.service.role;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleService<T> extends BaseService<T> {


    /**
     * 通过用户ID查询用户所拥有的所有角色
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(@Param("userId") String userId);

}
