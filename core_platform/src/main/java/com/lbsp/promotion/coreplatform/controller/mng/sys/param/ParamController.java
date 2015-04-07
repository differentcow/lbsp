package com.lbsp.promotion.coreplatform.controller.mng.sys.param;

import com.lbsp.promotion.core.service.param.ParameterService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.base.PageInfoRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Parameter;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.validation.Validation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Barry
 *
 */
@Controller
@RequestMapping("/param")
public class ParamController extends BaseController {
	@Autowired
	private ParameterService<Parameter> paramService;

    @RequestMapping(value = "/paramType/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object findParamCodeTypes(@PathVariable("type") String type) {
        GenericQueryParam param = new GenericQueryParam();
       param.put(new QueryKey("type", QueryKey.Operators.EQ),type);
        List<Parameter> list = paramService.findAll(param);
        return this.createBaseResult("查询成功", list);
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ResponseBody
    public Object findParamTypes(@RequestParam(value = "type", required=false) String type) {
        GenericQueryParam param = new GenericQueryParam();
        if (StringUtils.isNotEmpty(type)){
            param.put(new QueryKey("type", QueryKey.Operators.EQ),type);
        }
        List<Parameter> list = paramService.findAll(param,new String[]{"distinct type","type_meaning"});
        return this.createBaseResult("查询成功", list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object findParamById(@PathVariable(value = "id") String id) {
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        Parameter parameter = paramService.findOne(param);
        return this.createBaseResult("查询成功", parameter);
    }

    /**
     * 新增参数信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delParam(HttpServletRequest request) {
        String ids = request.getParameter("idAry");
        paramService.delParam(ids);
        return this.createBaseResult("删除参数成功", true);
    }

    /**
     * 新增参数信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addParam(
            HttpServletRequest request,
            @RequestBody Parameter param) {
        //判断参数的code与type是否为唯一性
        if(paramService.isExistParameter(param.getType(),param.getCode())){
            return this.createBaseResult(GenericConstants.LBSP_STATUS_SUCCESS,"参数:[type:"+param.getType()+"|code:"+param.getCode()+"]已存在.", false);
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        param.setCreate_user(userId);
        param.setUpdate_user(userId);
        paramService.addParam(param);
        return this.createBaseResult("新增参数成功", true);
    }


    /**
     * 修改参数信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/upt", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateParam(
            HttpServletRequest request,
            @RequestParam(value = "id",required = true)Integer id,
            @RequestBody Parameter param) {
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        param.setId(id);
        param.setUpdate_user(userId);
        if (paramService.updateParam(param)) {
            return this.createBaseResult("修改参数成功", true);
        }else{
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"修改参数失败", false);
        }
    }

	@RequestMapping(value = "/lst", method = RequestMethod.GET)
	@ResponseBody
	public Object findParamList(
            @RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
            @RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
			@RequestParam(value = "name", required=false) String name,
            @RequestParam(value = "code", required=false) String code,
            @RequestParam(value = "type", required=false) String type,
            @RequestParam(value = "has_count", required=false) Boolean has_count) {

        if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
            startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
        if (Validation.isEmpty(maxRecords) || maxRecords < 1)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
        if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

        GenericQueryParam param = new GenericQueryParam();
        if (StringUtils.isNotEmpty(name)){
            param.put(new QueryKey("name", QueryKey.Operators.LIKE),name);
        }
        if (StringUtils.isNotEmpty(code)){
            param.put(new QueryKey("code", QueryKey.Operators.LIKE),code);
        }
        if (StringUtils.isNotEmpty(type)){
            param.put(new QueryKey("type", QueryKey.Operators.EQ),type);
        }

        Integer count = paramService.count(param);
        PageInfoRsp page = new PageInfoRsp(count);

        param.setNeedPaging(true);  //分页
        param.addSortCond(new SortCond("update_time", SortCond.Order.DESC));    //排序
        param.setOffset(startRecord);   //起始行
        param.setPageSize(maxRecords);  //相差行数
        List<Parameter> list = paramService.find(param);
		return this.createBaseResult("查询成功", list, page);
	}
	
	
	
}
