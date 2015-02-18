/**
 * 
 */
package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.model.Role;
import com.lbsp.promotion.entity.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Barry
 *
 */
public interface RoleDao {

    /**
     * 通过用户ID查询用户所拥有的所有角色
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(@Param("userId") String userId);

    /**
     * 通过角色id获得角色**/
    Role getRoleById(@Param("id") String id);
    
    /**
     * 查询所有role**/
    List<Role> getAllRoles();
    
    /**
     * 查询所有user**/
    List<User> getAllUsers();
    
    /**
     * 通过角色id获得使用用户**/
    List<User> getUserByRoleId(@Param("roleId") String roleId);
    
    /**
     * 根据userid删除user_role表**/
    void delUserRole(@Param("userId") String userId);
    
    /**
     * 根据userid删除user_role表**/
    void delUserRoleOfRole(@Param("roleId") String roleId);
    
    /**
     * 根据userid和roleid删除user_role表**/
    void delUserRoleByURId(@Param("userId") String userId, @Param("roleId") String roleId);
    
    /**
     * 插入user_role表**/
    void addUserRole(@Param("userId") String userId, @Param("roleId") String roleId, @Param("now") Date now, @Param("operatorId") String operatorId);
    
    /**根据roleid获得所有父亲角色**/
    List<Role> getParentRoles(@Param("childId") String childId);
    
    /**
     * 删除roleid对应的所有role_parent表记录**/
    void delRoleParentOfRole(@Param("roleId") String roleId);
    
    /**通过roleid添加role_parent表**/
    void addRoleParent(@Param("childId") String childId, @Param("parentId") String parentId, @Param("now") Date now, @Param("operatorId") String operatorId);
    
    /**通过roleid删除role_parent表**/
    void delRoleParent(@Param("childId") String childId, @Param("parentId") String parentId);
    
    /**
     * 删除roleid对应的所有role_oper表记录**/
    void delRoleOperOfRole(@Param("roleId") String roleId);
    
    /**通过roleid添加role_oper表**/
    void addRoleOper(@Param("roleId") String roleId, @Param("operId") String operId, @Param("now") Date now, @Param("operatorId") String operatorId);
    
    /**通过roleid删除role_oper表**/
    void delRoleOper(@Param("roleId") String roleId, @Param("operId") String operId);
    
    /**
     * 通过roleid和操作者id删除role_oper表**/
    void delRoleOperOfOperator(@Param("roleId") String roleId, @Param("operatorId") String operatorId);
    
    /**查找role是不是被继承**/
    List<Role> ifRoleIsParent(@Param("roleId") String roleId);
}
