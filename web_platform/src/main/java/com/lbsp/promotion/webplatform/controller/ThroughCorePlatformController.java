/**
 * 
 */
package com.lbsp.promotion.webplatform.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lbsp.promotion.entity.base.BaseResult;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.enumeration.ExceptionCode;
import com.lbsp.promotion.util.http.request.*;
import com.lbsp.promotion.util.json.JsonMapper;
import com.lbsp.promotion.webplatform.security.token.UsernamePasswordSecurityKeyToken;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Barry
 * 
 */
@Controller
@RequestMapping("/core")
public class ThroughCorePlatformController {
	@Autowired
	private HttpRequestFactory factory;
	@Autowired
	private SecurityContextLogoutHandler securityContextLogoutHandler;
    @Autowired
    private SessionLocaleResolver localeResolver;
    @Value("${web.md5}")
    private String md5;

	@RequestMapping(value = "/{url}", method = RequestMethod.GET)
	public @ResponseBody
	Object getFromCore(@PathVariable String url, HttpServletRequest request,HttpServletResponse response) {
		url = url.replace("|", "/");
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		Enumeration queryNames = request.getParameterNames();
		while (queryNames.hasMoreElements()) {
			String name = (String) queryNames.nextElement();
			param.fill(name, request.getParameter(name));
		}
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder
				.getContext().getAuthentication();
		if (token != null) {
			param.fill(GenericConstants.AUTHKEY, token.getCredentials()
					.toString());
		}
		HttpRequest http = factory.createInstance(param, url);

		BaseResult<Object> result = http.get(new TypeReference<BaseResult<Object>>() {
		});
		if((Integer)result.getState().get("code") == ExceptionCode.AuthKeyNotExistException){
			securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		}
		return result;
	}

	@RequestMapping(value = "/{url}", method = RequestMethod.POST)
	public @ResponseBody
	Object postToCore(@PathVariable String url, @RequestBody(required=false) Map values,
			HttpServletRequest request,HttpServletResponse response) {
		url = url.replace("|", "/");
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		Enumeration queryNames = request.getParameterNames();
		while (queryNames.hasMoreElements()) {
			String name = (String) queryNames.nextElement();
			param.fill(name, request.getParameter(name));
		}
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
		
		Map<String,Map> result = JsonMapper.getJsonMapperInstance().readValue(http.post(), type);
		if((Integer)result.get("state").get("code") == ExceptionCode.AuthKeyNotExistException){
			securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		}
		return result;
	}

	@RequestMapping(value = "/{url}", method = RequestMethod.PUT)
	public @ResponseBody
	Object putToCore(@PathVariable String url, @RequestBody(required=false) Map values,
			HttpServletRequest request,HttpServletResponse response) {
		url = url.replace("|", "/");
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		Enumeration queryNames = request.getParameterNames();
		while (queryNames.hasMoreElements()) {
			String name = (String) queryNames.nextElement();
			param.fill(name, request.getParameter(name));
		}
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
		
		Map<String,Map> result = JsonMapper.getJsonMapperInstance().readValue(http.put(), type);
		if((Integer)result.get("state").get("code") == ExceptionCode.AuthKeyNotExistException){
			securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		}
		return result;
	}

	@RequestMapping(value = "/{url}", method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteFromCore(@PathVariable String url, HttpServletRequest request,HttpServletResponse response) {
		url = url.replace("|", "/");
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		Enumeration queryNames = request.getParameterNames();
		while (queryNames.hasMoreElements()) {
			String name = (String) queryNames.nextElement();
			param.fill(name, request.getParameter(name));
		}
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder
				.getContext().getAuthentication();
		if (token != null) {
			param.fill(GenericConstants.AUTHKEY, token.getCredentials()
					.toString());
		}
		HttpRequest http = factory.createInstance(param, url);
		TypeReference<Map> type = new TypeReference<Map>() {
		};
		
		Map<String,Map> result = JsonMapper.getJsonMapperInstance().readValue(http.delete(), type);
		if((Integer)result.get("state").get("code") == ExceptionCode.AuthKeyNotExistException){
			securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		}
		return result;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	Object postToCore(@RequestParam("uploadFile") MultipartFile file,
			HttpServletRequest request) throws IOException {
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder.getContext().getAuthentication();
		if (token != null) {
			param.fill(GenericConstants.AUTHKEY, token.getCredentials()
					.toString());
		}

        /**
         * 处理上传的图片
         */

		Part[] parts = { new CustomFilePart(file.getName(),
				new ByteArrayPartSource(file.getOriginalFilename(),
						file.getBytes()), null, "UTF-8") };
		GenericHttpRequest http = (GenericHttpRequest) factory.createInstance(
				param, "file/add");
		return http.postMutilPart(parts);
	}

    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public @ResponseBody
    Object uploadPicToCore(@RequestParam("uploadFile") MultipartFile file,
                           @RequestParam(value = "type",required = true)String type,
                           @RequestParam(value = "filename",required = false)String name,
                           @RequestParam(value = "code",required = false)String code,
                           @RequestParam(value = "id",required = false)String id,
                           HttpServletRequest request,HttpServletResponse response) throws IOException {
        GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
        UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder.getContext().getAuthentication();
        if (token != null) {
            param.fill(GenericConstants.AUTHKEY, token.getCredentials().toString());
        }

        /**
         * 处理上传的图片
         */
        String path = request.getSession().getServletContext().getRealPath("/");
        String path1 = "E:/Eproject/elsweb/els_platform/target/els_platform-1.0/";
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String relativePath = "";
        String filename = "";
        String backPath = "";
        if("MAP".equals(type)){
            backPath = "esl/equip/img/map";
            filename = "map_" + System.currentTimeMillis();
            relativePath = "img/map/"+filename;
        }
        if("TEMPLATE".equals(type)){
            backPath = "esl/equip/img/template";
            filename = "template_" + System.currentTimeMillis();
            relativePath = "img/template/"+filename;
        }
        if("SHELF".equals(type)){
            backPath = "esl/equip/img/shelf";
            filename = "shelf_" + System.currentTimeMillis();
            relativePath = "img/shelf/"+filename;
        }
        if("GATEWAY".equals(type)){
            backPath = "esl/gateway/img";
            filename = "gateway_" + System.currentTimeMillis();
            relativePath = "img/"+filename;
        }

        boolean isAdd = true;
        try{
            saveFileFromInputStream(file.getInputStream(),path+backPath,filename+suffix);
            saveFileFromInputStream(file.getInputStream(),path1+backPath,filename+suffix);
            if (StringUtils.isNotBlank(name)){
                isAdd = false;
                File f = new File(path+"/"+name);
                if(f.exists()){
                    f.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Map<String,String> values = new HashMap<String, String>();
        values.put("type", type);
        values.put("image_path", relativePath+suffix);
        values.put("code", code);
        values.put("name", filename);
        if(StringUtils.isNotBlank(id)){
            values.put("id",id);
        }
        HttpRequest http = factory.createInstance(param, "image/esl", values);
        http.setRequestType(GenericHttpRequest.RequestType.PARAM_IN_BODY);
        TypeReference<Map> objType = new TypeReference<Map>() {
        };
        Map<String,Map> result = JsonMapper.getJsonMapperInstance().readValue(isAdd?http.post():http.put(), objType);
        if((Integer)result.get("state").get("code") == ExceptionCode.AuthKeyNotExistException){
            securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        }
//        result.put("imgPath",relativePath);
        return result;
    }

    public void saveFileFromInputStream(InputStream stream,String path,String filename) throws IOException
    {
        FileOutputStream fs = new FileOutputStream(path + "/"+ filename);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1){
            bytesum+=byteread;
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }


    @RequestMapping(value = "/upload4editor", method = RequestMethod.POST)
	public void upload4Editor(@RequestParam("upload") MultipartFile file,
			@RequestParam("CKEditorFuncNum")int CKEditorFuncNum,
			HttpServletRequest request,HttpServletResponse response) throws IOException {
		GenericHttpParam param = new GenericHttpParam();
        param.fill("locale",localeResolver.resolveLocale(request).toString());
        param.fill("md5",md5);
		UsernamePasswordSecurityKeyToken token = (UsernamePasswordSecurityKeyToken) SecurityContextHolder
				.getContext().getAuthentication();
		if (token != null) {
			param.fill(GenericConstants.AUTHKEY, token.getCredentials()
					.toString());
		}
		Part[] parts = { new CustomFilePart("uploadFile",
				new ByteArrayPartSource(file.getOriginalFilename(),
						file.getBytes()), null, "UTF-8") };
		GenericHttpRequest http = (GenericHttpRequest) factory.createInstance(
				param, "file/add");
		TypeReference<Map> type = new TypeReference<Map>() {
		};
		
		Map result = JsonMapper.getJsonMapperInstance().readValue(http.postMutilPart(parts), type);
		response.getWriter().print("<script>window.parent.CKEDITOR.tools.callFunction("+CKEditorFuncNum+", parent.All580.getResUrl('"+result.get("data")+"'), '');</script>");
	}
}
