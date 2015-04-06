package com.lbsp.promotion.coreplatform.controller.mng.customer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lbsp.promotion.core.service.customer.CustomerService;
import com.lbsp.promotion.coreplatform.controller.base.BaseUploadController;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Customer;
import com.lbsp.promotion.util.validation.Validation;

/**
 *
 * Created on 2015-03-24 12:01:27
 *
 * @author 简易自动代码创建工具
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseUploadController {


	@Autowired
	private CustomerService<Customer> service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${fileupload.path}")
    private String resourceRootPath;
    @Value("${fileupload.dir}")
    private String resourceRootDir;
    @Value("${fileupload.src.path}")
    private String resourceSrcPath;
    @Value("${default.user.password}")
    private String defaultPassword;

    /**
     * 上传用户图像
     *
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadCustomerPic(HttpServletRequest request,
                                    @RequestParam(value = "uploadFile",required = false) MultipartFile file,
                                    @RequestParam(value = "id",required = false) Integer id){
        String filename = "";
        try {
            filename = upload(file,resourceRootPath,resourceRootDir,resourceSrcPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id == null){
            return this.createBaseResult("query success", filename);
        }else{
            Customer c = new Customer();
            c.setId(id);
            c.setPath(filename);
            setCommonInfo(c,request);
            service.update(c);
            return this.createBaseResult("query success", filename);
        }
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
		Customer rsp = service.getDetailById(id);
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
                        @RequestParam(value = "name", required=false) String name,
                        @RequestParam(value = "account", required=false) String account,
                        @RequestParam(value = "type", required=false) String type,
                        @RequestParam(value = "status", required=false) Integer status,
                        @RequestParam(value = "gender", required=false) Integer gender,
                        @RequestParam(value = "from", required=false) Long from,
						@RequestParam(value = "to", required=false) Long to) {

		if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
			startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
		if (Validation.isEmpty(maxRecords) || maxRecords < 1)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
		if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

		PageResultRsp page = service.getPageList(type,account,name,status,gender,from,to,startRecord,maxRecords);
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
	public Object save(HttpServletRequest request, @RequestBody Customer obj) {
		setCommonInfo(obj,request);
        obj.setPassword(passwordEncoder.encode(defaultPassword));
		if(service.saveCustomer(obj)){
			return this.createBaseResult("add success", true);
		}else{
			return this.createBaseResult("add failure", false);
		}
	}

    /**
     *
     * 保存信息
     *
     * @param request
     * @param obj
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @ResponseBody
    public Object reset(HttpServletRequest request, @RequestBody Customer obj){
        setCommonInfo(obj,request);
        obj.setPassword(passwordEncoder.encode(defaultPassword));
        if(service.updateCustomer(obj)){
            return this.createBaseResult("reset success", true);
        }else{
            return this.createBaseResult("reset failure", false);
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
	public Object update(HttpServletRequest request, @RequestBody Customer obj) {
		setCommonInfo(obj,request);
		if(service.updateCustomer(obj)){
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
			flag = service.deleteCustomer(Integer.parseInt(ids));
		}else{
			List<Integer> idAry = new ArrayList<Integer>();
			for (String id : idStr){
				if(StringUtils.isNumeric(id)){
					idAry.add(Integer.valueOf(id));
				}
			}
			flag = service.batchDeleteCustomer(idAry);
		}
		if(flag){
			return this.createBaseResult("delete success", true);
		}else{
			return this.createBaseResult("delete failure", false);
		}
	}

}
