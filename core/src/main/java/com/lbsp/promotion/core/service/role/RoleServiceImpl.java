package com.lbsp.promotion.core.service.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbsp.promotion.core.dao.RoleDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.model.Role;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
        RoleService<Role> {
	@Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getRolesByUserId(@Param("userId") Integer userId) {
        return roleDao.getRolesByUserId(userId);
    }
}
