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
    List<Role> getRolesByUserId(@Param("userId") Integer userId);

    /**
     * 通过角色id获得角色**/
    Role getRoleById(@Param("id") Integer id);
    
    /**
     * 查询所有role**/
    List<Role> getAllRoles();
    
    /**
     * 查询所有user**/
    List<User> getAllUsers();
    
    /**
     * 通过角色id获得使用用户**/
    List<User> getUserByRoleId(@Param("roleId") Integer roleId);
    
    /**
     * 根据userid删除user_role表**/
    void delUserRole(@Param("userId") Integer userId);
    
    /**
     * 根据userid删除user_role表**/
    void delUserRoleOfRole(@Param("roleId") Integer roleId);
    
    /**
     * 根据userid和roleid删除user_role表**/
    void delUserRoleByURId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
    
    /**
     * 插入user_role表**/
    void addUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId, @Param("now") Date now, @Param("operatorId") String operatorId);
    
    /**根据roleid获得所有父亲角色**/
    List<Role> getParentRoles(@Param("childId") Integer childId);
    
    /**
     * 删除roleid对应的所有role_parent表记录**/
    void delRoleParentOfRole(@Param("roleId") Integer roleId);
    
    /**通过roleid添加role_parent表**/
    void addRoleParent(@Param("childId") Integer childId, @Param("parentId") Integer parentId, @Param("now") Date now, @Param("operatorId") String operatorId);
    
    /**通过roleid删除role_parent表**/
    void delRoleParent(@Param("childId") Integer childId, @Param("parentId") Integer parentId);

    /**查找role是不是被继承**/
    List<Role> ifRoleIsParent(@Param("roleId") Integer roleId);
}
