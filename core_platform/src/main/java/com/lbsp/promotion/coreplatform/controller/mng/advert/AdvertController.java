package com.lbsp.promotion.coreplatform.controller.mng.advert;

import com.lbsp.promotion.core.service.advert.AdvertService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Advert;
import com.lbsp.promotion.util.validation.Validation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created on 2015-04-01 09:21:47
 *
 * @author 简易自动代码创建工具
 */
@Controller
@RequestMapping("/advert")
public class AdvertController extends BaseController {


	@Autowired
	private AdvertService<Advert> service;


	/**
	 *
	 * 通过ID获取信息
	 *
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object detail(@PathVariable(value = "id") Integer id) {
		Advert rsp = service.getDetailById(id);
		return this.createBaseResult("query success", rsp);
	}

	/**
	 *
	 * 获取信息集合(分页)
	 *
	 * @param startRecord
	 * @param maxRecords
	 * @param from
	 * @param to
	 * @return 
	 */
	@RequestMapping(value = "/lst", method = RequestMethod.GET)
	@ResponseBody
	public Object list(@RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
						@RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
                        @RequestParam(value = "title", required=false) String title,
                        @RequestParam(value = "customer", required=false) String customer,
                        @RequestParam(value = "type", required=false) String type,
                        @RequestParam(value = "status", required=false) Integer status,
                        @RequestParam(value = "from", required=false) Long from,
						@RequestParam(value = "to", required=false) Long to) {

		if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
			startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
		if (Validation.isEmpty(maxRecords) || maxRecords < 1)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
		if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

		PageResultRsp page = service.getPageList(title,customer,status,type,from,to,startRecord,maxRecords);
		return this.createBaseResult("query success", page.getResult(),page.getPageInfo());
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param request
	 * @param obj
	 * @return 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object save(HttpServletRequest request, @RequestBody Advert obj) {
		setCommonInfo(obj,request);
		if(service.saveAdvert(obj)){
			return this.createBaseResult("add success", true);
		}else{
			return this.createBaseResult("add failure", false);
		}
	}

	/**
	 *
	 * 更新信息
	 *
	 * @param request
	 * @param obj
	 * @return 
	 */
	@RequestMapping(value = "/upt", method = RequestMethod.PUT)
	@ResponseBody
	public Object update(HttpServletRequest request, @RequestBody Advert obj) {
		setCommonInfo(obj,request);
		if(service.updateAdvert(obj)){
			return this.createBaseResult("update success", true);
		}else{
			return this.createBaseResult("update failure", false);
		}
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param ids
	 * @return 
	 */
	@RequestMapping(value = "/del", method = RequestMethod.DELETE)
	@ResponseBody
	public Object delete(@RequestParam("ids")String ids) {
		String[] idStr = ids.split(",");
		boolean flag = false;
		if(idStr.length == 1 && StringUtils.isNumeric(ids)){
			flag = service.deleteAdvert(Integer.parseInt(ids));
		}else{
			List<Integer> idAry = new ArrayList<Integer>();
			for (String id : idStr){
				if(StringUtils.isNumeric(id)){
					idAry.add(Integer.valueOf(id));
				}
			}
			flag = service.batchDeleteAdvert(idAry);
		}
		if(flag){
			return this.createBaseResult("delete success", true);
		}else{
			return this.createBaseResult("delete failure", false);
		}
	}

}
