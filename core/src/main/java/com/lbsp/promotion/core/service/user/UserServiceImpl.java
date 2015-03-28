package com.lbsp.promotion.core.service.user;

import com.lbsp.promotion.core.dao.RoleDao;
import com.lbsp.promotion.core.dao.UserDao;
import com.lbsp.promotion.core.key.KeyGenerate;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.core.service.function.FunctionOperateService;
import com.lbsp.promotion.core.service.page.PageOperateService;
import com.lbsp.promotion.core.service.privilege.PrivilegeService;
import com.lbsp.promotion.core.service.role.RoleService;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.enumeration.Params;
import com.lbsp.promotion.entity.exception.security.PasswordErrorException;
import com.lbsp.promotion.entity.model.*;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.OperateResource;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService<User> {
	@Autowired
	private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private KeyGenerate userSecurityKeyGenerator;
    @Autowired
    private RoleService<Role> roleService;
    @Autowired
    private PrivilegeService<Privilege> privilegeService;
    @Autowired
    private PageOperateService<PageOperate> pageOperateService;
    @Autowired
    private FunctionOperateService<FunctionOperate> functionOperateService;
	@Autowired
	private RoleDao roleDao;

	public UserRsp getUserByAccountandPassword(String account,String password) {
		
		/** 查询出用户基本信息 */
		User tUser = userDao.getUserByAccount(account, Params.GenericStatus.ENABLED);
		if(!passwordEncoder.matches(password, tUser.getPassword())){
			throw new PasswordErrorException("密码错误");
		}
		tUser.setPassword(null);
		return getUserRspForLogin(tUser);
	}
	
	private UserRsp getUserRspForLogin(User tUser){
		UserRsp result = new UserRsp();
		if(tUser !=null ){
			tUser.setSecurity_key(userSecurityKeyGenerator.generate());
			setUserRsp(result,tUser);
			result.setUser(tUser);
		}
		return result;
	}

    /**
     * 更新auth_code
     *
     * @param userId
     * @param authCode
     * @return
     */
    @Transactional
    public boolean updateAuthCode(Integer userId,String authCode){
        try{
            User user = new User();
            user.setId(userId);
            user.setAuth_code(authCode);
            Date date = new Date();
            user.setUpdate_time(System.currentTimeMillis());
            user.setAuth_time(date);
            user.setUpdate_user(userId);
            userDao.updateUserInfo(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private List<Role> getParentRole(List<Role> roles){
        if(roles == null){
            return new ArrayList<Role>();
        }
        List<Role> parents = new ArrayList<Role>();
        for(Role r:roles){
            List<Role> list = roleDao.getParentRoles(r.getId());
            if(!list.isEmpty()){
                getParentRole(list);
            }
            parents.addAll(list);
        }
        return parents;
    }

    /**
     * 初始化User
     * @param rsp
     * @param user
     */
    private void setUserRsp(UserRsp rsp,User user){
        //查询用户拥有的角色
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        rsp.setCurrentRole(roles);
        //获取用户拥有的父角色
        rsp.setParentRole(getParentRole(roles));
        //临时存储所有的角色
        if(!rsp.getParentRole().isEmpty()){
            roles.addAll(rsp.getParentRole());
            rsp.setRoles(roles);
        };

        //准备过滤条件
        List<Integer> userIdParams = new ArrayList<Integer>();
        userIdParams.add(user.getId());
        List<Integer> roleIdParams = new ArrayList<Integer>();
        for (Role r : roles){
            roleIdParams.add(r.getId());
        }
        //查询用户可访问的页面
        List<String> pageStrs = new ArrayList<String>();
        List<OperateResource> pageOpers = new ArrayList<OperateResource>();
        //如果用户是超级管理员(系统自带生成的唯一的根用户)，允许访问所有页面
        if (GenericConstants.LBSP_ADMINISTRATOR_ID == user.getId()){
            List<PageOperate> pages = pageOperateService.allPages();
            for (PageOperate or : pages){
                pageStrs.add(or.getCode());
                OperateResource po = new OperateResource();
                po.setName(or.getName());
                po.setCode(or.getCode());
                po.setId(or.getId());
                po.setParent_code(or.getParent_code());
                po.setParent_id(or.getParent_id());
                po.setSort_index(or.getSort_index());
                po.setCreate_user(or.getCreate_user());
                po.setUpdate_user(or.getUpdate_user());
                po.setUpdate_time(or.getUpdate_time());
                po.setCreate_time(or.getCreate_time());
                pageOpers.add(po);
            }
        }else{
            //如果非超级管理员，查询其所拥有的可访问的页面
            List<OperateResource> pages = new ArrayList<OperateResource>();
            List<OperateResource> userPage = privilegeService.findEnabledPageByMasterandAccess(
                    GenericConstants.LBSP_PRIVILEGE_MASTER_TYPE_USER,userIdParams);
            List<OperateResource> rolePage = privilegeService.findEnabledPageByMasterandAccess(
                    GenericConstants.LBSP_PRIVILEGE_MASTER_TYPE_ROLE,roleIdParams);
            if(userPage != null && !userPage.isEmpty()){
                pages.addAll(userPage);
            }
            if(rolePage != null && !rolePage.isEmpty()){
                pages.addAll(rolePage);
            }
            Map<Integer,String> map = new HashMap<Integer, String>();
            for (OperateResource or : pages){
                if(StringUtils.isBlank(map.get(or.getId()))){
                    map.put(or.getId(),"Y");
                    pageStrs.add(or.getCode());
                    pageOpers.add(or);
                }
            }
        }
        rsp.setPageList(pageOpers);
        rsp.setPages(pageStrs);

        //查询用户所有的功能操作权限
        List<OperateResource> functions = new ArrayList<OperateResource>();
        //如果用户是超级管理员(系统自带生成的唯一的根用户)，自动装载所有操作权限
        if (GenericConstants.LBSP_ADMINISTRATOR_ID == user.getId()){
            List<FunctionOperate> funcList = functionOperateService.allFunctions();
            List<String> list = new ArrayList<String>();
            for (FunctionOperate func : funcList){
                OperateResource or = new OperateResource();
                or.setId(func.getId());
                or.setName(func.getName());
                or.setCode(func.getCode());
                or.setUrl(func.getUrl());
                or.setPath_param(func.getPath_param());
                or.setBase_url(func.getBase_url());
                or.setMethod(func.getMethod());
                or.setCreate_time(func.getCreate_time());
                or.setCreate_user(func.getCreate_user());
                or.setUpdate_time(func.getUpdate_time());
                or.setPage_id(func.getPage_id());
                or.setUpdate_user(func.getUpdate_user());
                functions.add(or);
                if(func.getUrl().indexOf("{") == -1 && func.getUrl().indexOf("}") == -1){
                    list.add(func.getUrl());
                }
            }
            rsp.setNoParamUrl(list);
        }else {
            List<FunctionOperate> funcList = functionOperateService.allFunctions();
            List<String> list = new ArrayList<String>();
            for (FunctionOperate func : funcList){
                if(func.getUrl().indexOf("{") == -1 && func.getUrl().indexOf("}") == -1){
                    list.add(func.getUrl());
                }
            }
            rsp.setNoParamUrl(list);
            //如果非超级管理员，查询其所拥有的功能操作权限
            List<OperateResource> tmpFunc = new ArrayList<OperateResource>();
            List<OperateResource> userFunc = privilegeService.findEnabledFuncByMasterandAccess(
                    GenericConstants.LBSP_PRIVILEGE_MASTER_TYPE_USER,userIdParams);
            List<OperateResource> roleFunc = privilegeService.findEnabledFuncByMasterandAccess(
                    GenericConstants.LBSP_PRIVILEGE_MASTER_TYPE_ROLE,roleIdParams);
            if(userFunc != null && !userFunc.isEmpty()){
                tmpFunc.addAll(userFunc);
            }
            if(roleFunc != null && !roleFunc.isEmpty()){
                tmpFunc.addAll(roleFunc);
            }
            Map<Integer,String> map = new HashMap<Integer, String>();
            for (OperateResource or : tmpFunc){
                if(StringUtils.isBlank(map.get(or.getId()))){
                    map.put(or.getId(),"Y");
                    functions.add(or);
                }
            }
        }
        rsp.setFuncsList(functions);
/*
        //保存用户与角色映射名称
        List<Operate> mapOper = operateDao.getOperateRoleMapName(roles);
        Map<String,String> mapOpers = new LinkedHashMap<String, String>();
        for (Operate o : mapOper){
            String tmp = null;
           if(mapOpers.get(o.getCode()) == null){
               tmp = o.getName();
           }else{
               tmp = mapOpers.get(o.getCode());
               tmp += ","+o.getName();
           }
            mapOpers.put(o.getCode(),tmp);
        }
        rsp.setMapRoleOperName(mapOpers);
        //以String的形式保存操作权限
        List<String> operStrList = new ArrayList<String>();
        for (Operate o : operList){
            operStrList.add(o.getCode());
        }
        rsp.setOperates(operStrList);
        rsp.setOperatesAll(operList);
        //加载权限前缀
        rsp.setOperPrefix(operateDao.getOperPrefix());
        rsp.setOperOtherPrefix(operateDao.getOtherOperPrefix());

        //查询用户所有的接口访问路径
        List<String> interUrls = new ArrayList<String>();
        List<Interface> operInters = new ArrayList<Interface>();
        Map<String,Integer> paramInter = new HashMap<String, Integer>();
        //如果用户是超级管理员(系统自带生成的唯一的根用户)，自动装载所有接口服务URL
        if (GenericConstants.LBSP_ADMINISTRATOR_ID.equals(user.getId())){
            operInters = interfaceService.getResourceUrlByOperate(null);
        }else {
            //如果非超级管理员，查询其所拥有的接口服务URL
            operInters = interfaceService.getResourceUrlByOperate(rsp.getOperatesAll());
        }
        for(Interface interfaces : operInters){
            if(interfaces.getUrl().indexOf("{") != -1 && interfaces.getUrl().indexOf("}") != -1){
                String url = interfaces.getUrl();
                int start = url.indexOf("{");
                int len = url.substring(start).split("/").length;
                paramInter.put(url.substring(0,start-1),len);
            }else{
                interUrls.add(interfaces.getUrl());
            }
        }
        rsp.setInterList(interUrls);
        rsp.setMapParamInter(paramInter);*/
    }

    /**
     * 获取用户信息By安全验证码
     *
     * @param authKey
     * @return
     */
	public UserRsp getUserBySecurityKey(String authKey){
		//查询出用户基本信息
		User tUser = userDao.getUserBySecurityKey(authKey);
		if(tUser == null){
			return new UserRsp();
		}
		tUser.setPassword(null);
		return getUserRspForReload(tUser);
	}
	
	private UserRsp getUserRspForReload(User tUser){
		UserRsp result = new UserRsp();
		if(tUser !=null ){
			setUserRsp(result,tUser);
			result.setUser(tUser);
		}
		return result;
	}

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Transactional
    public boolean delUser(String ids){
        //解析ID
        String[] idArray = ids.split(",");
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.IN),idArray);
        //查找用户使用的角色，修改role记录、删user_role、删user
        for(String id:idArray){
        	id=id.replace("'", "");//去掉'号。
        	List<Role> roles=roleDao.getRolesByUserId(Integer.parseInt(id));
        	for(Role r:roles){
        		int uc=r.getUser_count()-1;
        		r.setUser_count(uc);
//        		roleRightService.update(r);
        	}
        	roleDao.delUserRole(Integer.parseInt(id));
           }
        return this.delete(param) != 0; //批量删除
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Transactional
    public boolean addUser(User user){
        //填充新增记录信息
        long now = System.currentTimeMillis();
        user.setCreate_time(now);
        user.setUpdate_time(now);
        
        //先判断数据库中是否已有相同账号和名称的人
        GenericQueryParam param = new GenericQueryParam();
        GenericQueryParam param2 = new GenericQueryParam();
        param.put(new QueryKey("account", QueryKey.Operators.EQ),user.getAccount());
        param2.put(new QueryKey("username", QueryKey.Operators.EQ),user.getUsername());
        List<User> ulst=this.find(param);
        List<User> ulst2=this.find(param2);
        
        if(ulst.size()<=0&&ulst2.size()<=0){
        	
        //添加全体共有角色、修改角色用户数
//        roleDao.addUserRole(user.getId(), "000000000002"/*UserServiceImpl.ELS_WEB_ALL_ACCOUNT_ID*/, now, user.getCreate_user());
        Role role=roleService.findOne("000000000002"/*UserServiceImpl.ELS_WEB_ALL_ACCOUNT_ID*/);
        role.setUser_count(role.getUser_count()+1);
        roleService.update(role);
        
        int result = this.insert(user);
        return result != 1;
        
        }else{
        	return false;
        }
    }

    /**
     *修改用户
     *
     * @param user
     * @return
     */
    @Transactional
    public boolean updateUser(User user){
        try{
        	//先判断数据库中是否已有相同账号和名称的人
            user.setUpdate_time(System.currentTimeMillis());
            this.update(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过权限查找电子邮件
     *
     * @param codes
     * @return
     */
    public List<String> getEmailByOperate(String[] codes){
        return userDao.getEmailByOperate(codes);
    }

}
