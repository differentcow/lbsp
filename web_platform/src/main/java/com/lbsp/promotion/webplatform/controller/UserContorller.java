/**
 * 
 */
package com.lbsp.promotion.webplatform.controller;

import com.lbsp.promotion.entity.model.User;
import com.lbsp.promotion.entity.response.OperateResource;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.http.request.HttpRequestFactory;
import com.lbsp.promotion.webplatform.security.access.CustomMetadataSource;
import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import com.lbsp.promotion.webplatform.vo.UserDTO;
import com.lbsp.promotion.webplatform.vo.UserFunctionDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author Barry
 * 
 */
@Controller
@RequestMapping("/user")
public class UserContorller {
	private final static String RESOURCES_FIELD_FUNID = "funcId";
	private final static String RESOURCES_FIELD_PARENTID = "parentId";
	private final static String RESOURCES_FIELD_TYPE = "type";
	private final static String RESOURCES_FIELD_NAME = "name";
	private final static String RESOURCES_FIELD_URL = "url";
    private final static String RESOURCES_FIELD_OPER = "oper";
	private final static String RESOURCES_FIELD_INDEX = "index";
	private final static String RESOURCES_FIELD_ICON = "icon";
	private final static String RESOURCES_FIELD_TARGET = "target";
	private final static String RESOURCES_FIELD_PARENT_ROOT="root";
	private final static String RESOURCES_FIELD_TYPE_MENU="menu";

    @Autowired
    private SessionLocaleResolver localeResolver;

    /**
     * 加载菜单资源
     *
     * @param resources
     * @param operlist
     * @return
     */
	private List<UserFunctionDTO> loadMenu(List<Map<String, String>> resources,List<String> operlist){
		List<UserFunctionDTO> menurec = new ArrayList<UserFunctionDTO>();
        Map<String,UserFunctionDTO> parent = new HashMap<String,UserFunctionDTO>();
        Map<String,List<UserFunctionDTO>> sub = new HashMap<String,List<UserFunctionDTO>>();
		for (Map<String, String> map : resources) {
			if (RESOURCES_FIELD_TYPE_MENU.equals(map.get(RESOURCES_FIELD_TYPE))) {	//如果资源是菜单资源
                if(RESOURCES_FIELD_PARENT_ROOT.equals(map.get(RESOURCES_FIELD_PARENTID))){  //如果是第一级菜单，先存储起来
                    UserFunctionDTO dto = new UserFunctionDTO(
                            map.get(RESOURCES_FIELD_FUNID),
                            map.get(RESOURCES_FIELD_NAME),
                            map.get(RESOURCES_FIELD_URL),
                            map.get(RESOURCES_FIELD_TYPE),
                            map.get(RESOURCES_FIELD_INDEX),
                            map.get(RESOURCES_FIELD_ICON),
                            map.get(RESOURCES_FIELD_TARGET),
                            map.get(RESOURCES_FIELD_PARENTID)
                    );
                    if(sub.get(map.get(RESOURCES_FIELD_FUNID)) != null){    //如果存在该第一级菜单属下子菜单，设置到第一级菜单中
                        dto.setSubfunctions(sub.get(map.get(RESOURCES_FIELD_FUNID)));
                    }else{
                        dto.setSubfunctions(new ArrayList<UserFunctionDTO>());
                    }
                    parent.put(map.get(RESOURCES_FIELD_FUNID),dto);
                }else{
                    boolean flag = false;  //是否组将子菜单
                    if(StringUtils.isBlank(map.get(RESOURCES_FIELD_OPER))){ //判断操作权限是否为空（为空代表任何人都能访问）
                        flag = true;
                    }else{
                        if(operlist.contains(map.get(RESOURCES_FIELD_FUNID))){   //判断用户是否有权限访问页面
                            flag = true;
                        }
                    }
                    if(flag){
                        UserFunctionDTO subDto = new UserFunctionDTO(
                                map.get(RESOURCES_FIELD_FUNID),
                                map.get(RESOURCES_FIELD_NAME),
                                map.get(RESOURCES_FIELD_URL),
                                map.get(RESOURCES_FIELD_TYPE),
                                map.get(RESOURCES_FIELD_INDEX),
                                map.get(RESOURCES_FIELD_ICON),
                                map.get(RESOURCES_FIELD_TARGET),
                                map.get(RESOURCES_FIELD_PARENTID)
                        );
                        if(parent.get(map.get(RESOURCES_FIELD_PARENTID)) != null){  //如果第一级菜单存在，将第二季菜单设置到第一级菜单中
                            parent.get(map.get(RESOURCES_FIELD_PARENTID)).getSubfunctions().add(subDto);
                        }else{  //如果不存在，先暂时存储第二级菜单
                            List<UserFunctionDTO> list = null;
                            if(sub.get(map.get(RESOURCES_FIELD_PARENTID)) == null){ //判断是否之前已经存储过相同父级菜单key的子菜单集合
                                list = new ArrayList<UserFunctionDTO>();    //如没有，创建该集合
                            }else{
                                list = sub.get(map.get(RESOURCES_FIELD_PARENTID));  //如存在，直接取出
                            }
                            list.add(subDto);
                            sub.put(map.get(RESOURCES_FIELD_PARENTID),list);    //存储集合
                        }
                    }
                }
			}
		}
        for(Map.Entry<String,UserFunctionDTO> m : parent.entrySet()){
            if(m.getValue().getSubfunctions() != null && !m.getValue().getSubfunctions().isEmpty()){
                menurec.add(m.getValue());   //存储到菜单集合
            }
        }
		sortList(menurec);	//父级菜单排序
		//子菜单排序
		for (UserFunctionDTO userFunctionDTO : menurec) {
			if (userFunctionDTO.getSubfunctions() != null && !userFunctionDTO.getSubfunctions().isEmpty()) {
				sortList(userFunctionDTO.getSubfunctions());
			}
		}
		return menurec;
	}

    /**
     * 排序
     *
     * @param dtos
     */
	private void sortList(List<UserFunctionDTO> dtos){
		Collections.sort(dtos, new Comparator<UserFunctionDTO>() {
					@Override
					public int compare(UserFunctionDTO o1, UserFunctionDTO o2) {
						if(o1.getIndex()==null&&o2.getIndex()==null){return 0;}
						if(o1.getIndex()==null&&o2.getIndex()!=null){	return -1;}
						if(o1.getIndex()!=null&&o2.getIndex()==null){	return 1;}
						return o1.getIndex().compareTo(o2.getIndex());
					}
		});
	}

    private String url = "userInfo/upt/me";

    @Autowired
    private HttpRequestFactory factory;

    @Autowired
    private SecurityContextLogoutHandler securityContextLogoutHandler;

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/me", method = RequestMethod.PUT)
    public @ResponseBody Object updateMeInfo(HttpServletRequest request,@RequestBody(required=false) Map values,HttpServletResponse response) {
        GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder
                .getContext().getAuthentication();
        if (token != null) {
            param.fill(GenericConstants.AUTHKEY, token.getCredentials()
                    .toString());
        }
        HttpRequest http = factory.createInstance(param, url, values);
        http.setRequestType(GenericHttpRequest.RequestType.PARAM_IN_BODY);
        TypeReference<Map> type = new TypeReference<Map>() {
        };

        Map result = JsonMapper.getJsonMapperInstance().readValue(http.put(), type);
        if((Integer)result.get("code") == ExceptionCode.AuthKeyNotExistException){
            securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        }

        if((Boolean)result.get("result")){
            String username = (String)values.get("username");
            String email = (String)values.get("email");

            token.getUserResult().getUser().setUsername(username);
            token.getUserResult().getUser().setEmail(email);
        }

        return result;

    }*/

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
	@RequestMapping("/info")
	public @ResponseBody Object getFunctions(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			UserDTO dto = new UserDTO();		//载体
			//从session中获取用户信息
			UserRsp userRsp = ((UsernamePasswordSecurityKeyToken)authentication).getUserResult();
			if (userRsp != null) {
				//获取所有菜单资源（从xml中）
				List<Map<String, String>> resources = CustomMetadataSource.getResources(localeResolver.resolveLocale(request).toString());
				if (resources != null) {
					//装载菜单
					List<UserFunctionDTO> menuList = loadMenu(resources, userRsp.getPages());
					dto.setFuncs(menuList);
				}
				
				//复制用户信息（用来载入页面的数据对象中）
				UserRsp copyUserRsp = new UserRsp();
				BeanUtils.copyProperties(userRsp, copyUserRsp);

				//加载用户操作权限
				if (copyUserRsp.getFuncsList() != null && !copyUserRsp.getFuncsList().isEmpty()) {
                    List<String> auths = new ArrayList<String>();
                    Map<String,String> map = new HashMap<String, String>();
                    for (OperateResource or : copyUserRsp.getFuncsList()){
                        if(StringUtils.isBlank(map.get(or.getCode()))){
                            map.put(or.getCode(),"Y");
                            auths.add(or.getCode());
                        }
                    }
					dto.setAuths(auths);
					//清除用户操作权限
					copyUserRsp.setFuncsList(null);
				}

				//加载用户信息
				if(copyUserRsp.getUser()!=null){
					User copyUser = new User();
					BeanUtils.copyProperties(copyUserRsp.getUser(), copyUser);
					copyUser.setPassword(null);
					copyUserRsp.setUser(copyUser);
				}
				
				dto.setUserRsp(copyUserRsp);
				
				return dto;
			}
		}
		
		return null;
	}

}
