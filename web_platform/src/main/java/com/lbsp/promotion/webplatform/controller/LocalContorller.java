package com.lbsp.promotion.webplatform.controller;

import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.util.http.request.HttpRequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;


/**
 * @author Barry
 * 
 */
@Controller
@RequestMapping("/locale")
public class LocalContorller {

    @Autowired
    private SessionLocaleResolver localeResolver;

    /**
     * 切换语言跳转
     *
     * @param request
     * @param locale
     * @return
     */
    @RequestMapping("/{locale}")
    public @ResponseBody Object changeLocale(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable("locale")String locale) {

        localeResolver.setLocale(request, response, new Locale(locale));
        BaseResult result = new BaseResult(GenericConstants.LBSP_STATUS_SUCCESS,"ok",true);
        return result;
    }

    /**
     * 获取语言跳转
     *
     * @param request
     * @return
     */
    @RequestMapping("/one")
    public @ResponseBody Object getLocale(HttpServletRequest request) {

        String locale = localeResolver.resolveLocale(request).toString();
        String[] locales = locale.split("_");
        if(locales.length >= 2){
            locale = locales[0].toLowerCase()+"_"+locales[1].toUpperCase();
        }
        BaseResult result = new BaseResult(GenericConstants.LBSP_STATUS_SUCCESS,"ok",locale);
        return result;
    }
}
