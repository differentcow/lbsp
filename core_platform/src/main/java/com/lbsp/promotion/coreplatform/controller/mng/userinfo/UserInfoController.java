package com.lbsp.promotion.coreplatform.controller.mng.userinfo;

import com.lbsp.promotion.core.service.user.UserService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.base.PageInfoRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.User;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import com.lbsp.promotion.entity.request.Password;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.DateTime;
import com.lbsp.promotion.util.MessageUtil;
import com.lbsp.promotion.util.Security;
import com.lbsp.promotion.util.SendMail;
import com.lbsp.promotion.util.session.SessionMap;
import com.lbsp.promotion.util.validation.Validation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author Summer
 *
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {
    @Autowired
    private UserService<User> userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${default.user.password}")
    private String defaultPassword;

    @Value("${mail.server.host}")
    private String host;

    @Value("${mail.server.username}")
    private String username;

    @Value("${mail.server.password}")
    private String password;

    @Value("${mail.server.address}")
    private String address;

    @Value("${mail.auth.content}")
    private String content;

    @Value("${mail.server.max.auth.time}")
    private Integer max;

    @Value("${mail.auth.on}")
    private String isEmailAuthOn;

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public Object me(HttpServletRequest request) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        return this.createBaseResult("查询成功", session);
    }

    private static String EMAIL_AUTH_ON = "Y";

    /**
     * 对比旧密码是否正确，并返回验证码
     *
     * @param request
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/old", method = RequestMethod.PUT)
    @ResponseBody
    public Object oldPassword(HttpServletRequest request,
                              @RequestBody Password pwd) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),userId);
        User user = userService.findOne(param);
        if(passwordEncoder.matches(pwd.getOldPassword(),user.getPassword())){
            if(EMAIL_AUTH_ON.equals(isEmailAuthOn)){
                String authCode = Security.getRandomString(12); //生成12位验证码
                boolean flag = userService.updateAuthCode(userId,authCode);
                if(flag){
                    SendMail sm = new SendMail(host,username,password);
                    sm.setAddress(address,user.getEmail(),"验证码");
                    sm.send(MessageUtil.getXmlUtilInstance().translateMsg(content,new String[]{authCode}));
                    return this.createBaseResult("验证码已发送", true);
                }
            }
            return this.createBaseResult("验证旧密码成功", true);
        }
        return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE, "密码不正确", false);
    }

    /**
     * 重发AuthCode
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/repeatAuth", method = RequestMethod.GET)
    @ResponseBody
    public Object reAuth(HttpServletRequest request) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),userId);
        User user = userService.findOne(param);
        String authCode = Security.getRandomString(12); //生成12位验证码
        boolean flag = userService.updateAuthCode(userId,authCode);
        if (flag){
            SendMail sm = new SendMail(host,username,password);
            sm.setAddress(address,user.getEmail(),"验证码");
            sm.send(MessageUtil.getXmlUtilInstance().translateMsg(content,new String[]{authCode}));
            return this.createBaseResult("验证码已发送", true);
        }
        return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE, "操作失败", false);
    }

    /**
     * 对比AuthCode
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ResponseBody
    public Object auth(HttpServletRequest request,
                       @RequestParam("code")String code) {
        if(!EMAIL_AUTH_ON.equals(isEmailAuthOn)){
            return this.createBaseResult("不需要验证", true);
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),userId);
        User user = userService.findOne(param);
        Date now = new Date();
        Long diff = DateTime.compareTo(now, user.getAuth_time());
        Integer min = DateTime.turnUnit(diff,DateTime.UNIT_MIN).intValue();
        if(min < max){
            if(StringUtils.isNotBlank(user.getAuth_code()) && user.getAuth_code().equals(code)){    //验证成功
                return this.createBaseResult("验证成功", true);
            }else{
                return this.createBaseResult("验证码不正确", false);
            }
        }else{
            return this.createBaseResult("验证码已过期", false);
        }
    }
    /**
     * 是否出入验证授权码中
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/isAuthing", method = RequestMethod.GET)
    @ResponseBody
    public Object isAuthing(HttpServletRequest request) {
        if(!EMAIL_AUTH_ON.equals(isEmailAuthOn)){
            return this.createBaseResult("不需要验证", "filter");
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),userId);
        User user = userService.findOne(param);
        if (StringUtils.isBlank(user.getAuth_code())){
            return this.createBaseResult("不在验证中", false);
        }
        Date now = new Date();
        Long diff = DateTime.compareTo(now,user.getAuth_time());
        Integer min = DateTime.turnUnit(diff,DateTime.UNIT_MIN).intValue();
        if(min < max){
            return this.createBaseResult("正在验证中", true);
        }else{  //如果验证码已经过期，将重新置为null
            User u = new User();
            u.setId(userId);
            u.setAuth_code("null");
            u.setUpdate_user(userId);
            userService.updateUser(u);
            return this.createBaseResult("不在验证中", false);
        }
    }


    /**
     * 更新密码
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.PUT)
    @ResponseBody
    public Object newPassword(HttpServletRequest request,
                              @RequestBody Password pwd) {
        if(StringUtils.isBlank(pwd.getSurePassword()) ||
                StringUtils.isBlank(pwd.getNewPassword()) ||
                    !pwd.getNewPassword().equals(pwd.getSurePassword())){
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"新密码设置错误", false);
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        User user = new User();
        user.setId(userId);
        user.setUpdate_user(userId);
        user.setPassword(passwordEncoder.encode(pwd.getNewPassword()));
        user.setAuth_code("null");
        boolean flag = userService.updateUser(user);
        if (flag){
            return this.createBaseResult("更新密码成功", true);
        }else{
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"更新密码失败", false);
        }
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object findUserById(@PathVariable(value = "id") String id) {
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        User user = userService.findOne(param);
        return this.createBaseResult("查询成功", user);
    }
    
    /**
     * 判断是否有已存在的account或username
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/ifRepeat", method = RequestMethod.GET)
    @ResponseBody
    public Object findUserByAccountOrName(
    		@RequestParam(value = "id",required = true)String id,
    		@RequestParam(value = "tag",required = true)String tag,
    		@RequestParam(value = "value",required = true)String value) {
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey(tag, QueryKey.Operators.EQ),value);
        User user = userService.findOne(param);
        int size=0;
        if(user!=null){
        	if(!user.getId().equals(id)){
        		size=1;
        	}
        }
        return this.createBaseResult("查询成功", size);
    }

    /**
     * 删除用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delUser(HttpServletRequest request) {
        String ids = request.getParameter("idAry");
        userService.delUser(ids);
        return this.createBaseResult("删除用户成功", true);
    }

    /**
     * 新增用户信息
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(
            HttpServletRequest request,
            @RequestBody User user) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        user.setCreate_user(userId);
        user.setUpdate_user(userId);
        String pwd = passwordEncoder.encode(defaultPassword); //加密密码，默认在core.properties设置，后续再完善
        user.setPassword(pwd);
        boolean r=userService.addUser(user);
        return this.createBaseResult("新增用户结果："+r, r);
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "/upt", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateUser(
            HttpServletRequest request,
            @RequestParam(value = "id",required = true)Integer id,
            @RequestBody User user) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        user.setId(id);
        user.setUpdate_user(userId);
        if (userService.updateUser(user)) {
            return this.createBaseResult("修改用户成功", true);
        }else{
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"修改用户失败", false);
        }
    }

    /**
     * 修改当前用户信息
     *
     * @param request
     * @param rsp
     * @return
     */
    @RequestMapping(value = "/upt/me", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateMe(HttpServletRequest request,
                           @RequestBody User rsp) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        User user = new User();
        user.setId(userId);
        user.setUpdate_user(userId);
        user.setUsername(rsp.getUsername());
        user.setEmail(rsp.getEmail());
        if (userService.updateUser(user)) {
            session.getUser().setUsername(user.getUsername());
            session.getUser().setEmail(user.getEmail());
            SessionMap.getSessionMapInstance().put(session.getUser().getSecurity_key(),session);
            return this.createBaseResult("修改用户成功", true);
        }else{
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"修改用户失败", false);
        }
    }

    /**
     * 分页查询用户信息
     *
     * @param startRecord
     * @param maxRecords
     * @param has_count
     * @param username
     * @param account
     * @return
     */
	@RequestMapping(value = "/lst", method = RequestMethod.GET)
	@ResponseBody
	public Object findUserList(
			@RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
            @RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
            @RequestParam(value = "has_count", required=false) Boolean has_count,
            @RequestParam(value = "username", required=false) String username,
            @RequestParam(value = "account") String account
			){
		if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
            startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
        if (Validation.isEmpty(maxRecords) || maxRecords < 1)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
        if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

        GenericQueryParam param = new GenericQueryParam();
        if (StringUtils.isNotEmpty(username)){
            param.put(new QueryKey("username", QueryKey.Operators.LIKE),username);
        }
        if (StringUtils.isNotEmpty(account)){
            param.put(new QueryKey("account", QueryKey.Operators.LIKE),account);
        }

        Integer count = userService.count(param);
        PageInfoRsp page = new PageInfoRsp(count);

        param.setNeedPaging(true);  //分页
        param.addSortCond(new SortCond("create_date", SortCond.Order.DESC));    //排序
        param.setOffset(startRecord);   //起始行
        param.setPageSize(maxRecords);  //相差行数
        List<User> list = userService.find(param);
		return this.createBaseResult("查询成功", list, page);
	}

}
