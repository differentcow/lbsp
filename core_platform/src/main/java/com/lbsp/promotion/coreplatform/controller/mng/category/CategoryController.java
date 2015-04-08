package com.lbsp.promotion.coreplatform.controller.mng.category;

import com.lbsp.promotion.core.service.category.CategoryService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Category;
import com.lbsp.promotion.entity.request.CategoryOperate;
import com.lbsp.promotion.entity.response.CategoryRsp;
import com.lbsp.promotion.entity.response.TreeNode;
import com.lbsp.promotion.util.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * Created on 2015-04-01 09:21:24
 *
 * @author 简易自动代码创建工具
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {


	@Autowired
	private CategoryService<Category> service;



    /**
     * 获取分类信息结构
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public Object tree(HttpServletRequest request,
                       @RequestParam(value = "id",required = false)Integer id){
        TreeNode node = service.getTreeNodeByParent(id);
        BaseResult result = this.createBaseResult("query success", node);
        result.setOnlyResult(true);
        return result;
    }


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
		CategoryRsp rsp = service.getDetailById(id);
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
						@RequestParam(value = "from", required=false) Long from,
						@RequestParam(value = "to", required=false) Long to) {

		if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
			startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
		if (Validation.isEmpty(maxRecords) || maxRecords < 1)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
		if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

		PageResultRsp page = service.getPageList(from,to,startRecord,maxRecords);
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
	public Object save(HttpServletRequest request, @RequestBody CategoryOperate obj) {
		if(service.saveBatchCategory(obj.getSaveOperateList(),getSessionUserId(request))){
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
	public Object update(HttpServletRequest request, @RequestBody CategoryOperate obj) {
        List<Category> list = obj.getEditOperateList();
		if(service.updateBatchCategory(list,getSessionUserId(request))){
			return this.createBaseResult("update success", true);
		}else{
			return this.createBaseResult("update failure", false);
		}
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param obj
	 * @return 
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(HttpServletRequest request,@RequestBody CategoryOperate obj) {
		if(service.deleteBatchCategory(obj.getDeleteOperateList())){
			return this.createBaseResult("delete success", true);
		}else{
			return this.createBaseResult("delete failure", false);
		}
	}

}
