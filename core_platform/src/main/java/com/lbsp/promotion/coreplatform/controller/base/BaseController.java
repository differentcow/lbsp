package com.lbsp.promotion.coreplatform.controller.base;

import com.lbsp.promotion.coreplatform.editor.LongDataEditor;
import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.base.PageInfoRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.BaseModel;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.validation.Validation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public abstract class BaseController {

    @Autowired
    private MessageSource messageSource;

    protected UserRsp getSessionUser(HttpServletRequest request){
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        return session;
    }

    protected Integer getSessionUserId(HttpServletRequest request){
        return getSessionUser(request).getUser().getId();
    }

    protected void setCommonInfo(BaseModel model,HttpServletRequest request){
        if(model == null)
            return;
        Long now = System.currentTimeMillis();
        Integer userId = getSessionUserId(request);
        if (model.getId() == null){
            model.setCreate_user(userId);
            model.setCreate_time(now);
        }
        model.setUpdate_time(now);
        model.setUpdate_user(userId);
    }

    private Locale getLocale(String localStr){
        Locale locale = null;
        if(StringUtils.isNotBlank(localStr)){
            String[] locals = localStr.split("_");
        if(locals.length == 3){
            locale = new Locale(locals[0],locals[1],locals[2]);
        }else if(locals.length == 2){
            locale = new Locale(locals[0],locals[1]);
        }else
            locale = new Locale(localStr);
        }
        return locale;
    }

    protected String getMessage(HttpServletRequest request,String key){
        return messageSource.getMessage(key,null,getLocale(request.getParameter("locale")));
    }

    protected String getMessage(HttpServletRequest request,String key,Object[] objs){
        return messageSource.getMessage(key,objs,getLocale(request.getParameter("locale")));
    }

    protected String getMessage(String key,Object[] objs,String locale){
        return messageSource.getMessage(key,objs,getLocale(locale));
    }

    protected String getMessage(String key,Object[] objs,Locale locale){
        return messageSource.getMessage(key,objs,locale);
    }

	protected final static String DEFAULT_LIST_PAGE_INDEX = "0";
	public final static String DEFAULT_LIST_PAGE_SIZE = "20";

	protected <T> BaseResult<T> createBaseResult(String msg, T object) {
		return createBaseResult(GenericConstants.LBSP_STATUS_SUCCESS, msg,object);
	}

    protected <T> BaseResult<T> createBaseResult(Integer statusCode,String msg, T object) {
        return new BaseResult<T>(statusCode, msg,object);
    }

	protected <T> BaseResult<T> createBaseResult(String msg, T object,
			PageInfoRsp pageInfo) {
		return new BaseResult<T>(GenericConstants.LBSP_STATUS_SUCCESS, msg, object, pageInfo);
	}

	protected void throwException(String msg) {
		throw new RuntimeException(msg);
	}
	
	public void checkPageParam(Integer startRecord,Integer maxRecords){
		if (Validation.isEmpty(startRecord)
				|| startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
			startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
		if (Validation.isEmpty(maxRecords) || maxRecords < 1)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
		if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
			maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;
	}

    @InitBinder
    public void init(WebDataBinder binder){
        binder.registerCustomEditor(Long.class,new LongDataEditor());
    }
}
