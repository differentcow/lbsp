package com.lbsp.promotion.gen.template.controller;

import com.lbsp.promotion.gen.template.comment.CommentTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HLJ on 2015/3/22.
 */
public class ControllerTemplate {

    private String packageStr;
    private String className;
    private String subdir;
    private CommentTemplate comment;
    private String fileName;

    public ControllerTemplate(String table,String packageStr){
        StringBuffer sb = new StringBuffer("");
        if(table.indexOf("_") != -1){
            String[] parts = table.split("_");
            for (String part : parts){
                sb.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
            }
        }else{
            sb.append(table.trim().substring(0,1).toUpperCase()).append(table.trim().substring(1));
        }
        this.className = sb.toString();
        this.subdir = sb.toString().toLowerCase();
        sb.append("Controller");
        this.fileName = sb.toString();
        this.packageStr = packageStr + "." + this.subdir;
        comment = new CommentTemplate("Created on "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),"简易自动代码创建工具");

    }

    public String getSubdir() {
        return this.subdir;
    }

    public String getFileName(){
        return this.fileName;
    }


    public String genController(){
        StringBuffer sb = new StringBuffer("");
        //package
        sb.append("package ").append(packageStr).append(";\n\n");
        //import
        sb.append("import com.lbsp.promotion.core.service.").append(subdir).append(".").append(className).append("Service;\n");
        sb.append("import com.lbsp.promotion.coreplatform.controller.base.BaseController;\n");
        sb.append("import com.lbsp.promotion.entity.base.PageResultRsp;\n");
        sb.append("import com.lbsp.promotion.entity.constants.GenericConstants;\n");
        sb.append("import com.lbsp.promotion.entity.model.").append(className).append(";\n");
        sb.append("import com.lbsp.promotion.util.validation.Validation;\n");
        sb.append("import org.apache.commons.lang.StringUtils;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Controller;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n\n");
        sb.append("import javax.servlet.http.HttpServletRequest;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");
        //frame
        sb.append("\n");
        sb.append(comment.genComment());
        sb.append("@Controller\n");
        sb.append("@RequestMapping(\"/").append(subdir).append("\")\n");
        sb.append("public class ").append(className).append("Controller extends BaseController {\n\n");
        //body
        sb.append("\n\t@Autowired\n");
        sb.append("\tprivate ").append(className).append("Service<").append(className).append("> service;\n");
        sb.append("\n\n");
        sb.append(genDetail());
        sb.append("\n");
        sb.append(genPageList());
        sb.append("\n");
        sb.append(genSave());
        sb.append("\n");
        sb.append(genUpdate());
        sb.append("\n");
        sb.append(genDelete());
        //body
        sb.append("\n}\n");
        //frame
        return sb.toString();
    }

    private String genDetail(){
        comment = new CommentTemplate("通过ID获取信息",true);
        comment.addParam("id");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@RequestMapping(value = \"/{id}\", method = RequestMethod.GET)\n");
        sb.append("\t@ResponseBody\n");
        sb.append("\tpublic Object detail(@PathVariable(value = \"id\") Integer id) {\n");
        sb.append("\t\t").append(className).append(" rsp = service.getDetailById(id);\n");
        sb.append("\t\treturn this.createBaseResult(\"query success\", rsp);\n");
        sb.append("\t}\n");
        return  sb.toString();
    }

    private String genPageList(){
        comment = new CommentTemplate("获取信息集合(分页)",true);
        comment.addParam("startRecord");
        comment.addParam("maxRecords");
        comment.addParam("from");
        comment.addParam("to");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@RequestMapping(value = \"/lst\", method = RequestMethod.GET)\n");
        sb.append("\t@ResponseBody\n");
        sb.append("\tpublic Object list(@RequestParam(value = \"iDisplayStart\", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,\n");
        sb.append("\t\t\t\t\t\t@RequestParam(value = \"iDisplayLength\", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,\n");
        sb.append("\t\t\t\t\t\t@RequestParam(value = \"from\", required=false) Long from,\n");
        sb.append("\t\t\t\t\t\t@RequestParam(value = \"to\", required=false) Long to) {\n\n");
        sb.append("\t\tif (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)\n");
        sb.append("\t\t\tstartRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;\n");
        sb.append("\t\tif (Validation.isEmpty(maxRecords) || maxRecords < 1)\n");
        sb.append("\t\t\tmaxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;\n");
        sb.append("\t\tif (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)\n");
        sb.append("\t\t\tmaxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;\n\n");
        sb.append("\t\tPageResultRsp page = service.getPageList(from,to,startRecord,maxRecords);\n");
        sb.append("\t\treturn this.createBaseResult(\"query success\", page.getResult(),page.getPageInfo());\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genSave(){
        comment = new CommentTemplate("保存信息",true);
        comment.addParam("request");
        comment.addParam("obj");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@RequestMapping(value = \"/add\", method = RequestMethod.POST)\n");
        sb.append("\t@ResponseBody\n");
        sb.append("\tpublic Object save(HttpServletRequest request, @RequestBody ").append(className).append(" obj) {\n");
        sb.append("\t\tsetCommonInfo(obj,request);\n");
        sb.append("\t\tif(service.save").append(className).append("(obj)){\n");
        sb.append("\t\t\treturn this.createBaseResult(\"add success\", true);\n");
        sb.append("\t\t}else{\n");
        sb.append("\t\t\treturn this.createBaseResult(\"add failure\", false);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genUpdate(){
        comment = new CommentTemplate("更新信息",true);
        comment.addParam("request");
        comment.addParam("obj");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@RequestMapping(value = \"/upt\", method = RequestMethod.PUT)\n");
        sb.append("\t@ResponseBody\n");
        sb.append("\tpublic Object update(HttpServletRequest request, @RequestBody ").append(className).append(" obj) {\n");
        sb.append("\t\tsetCommonInfo(obj,request);\n");
        sb.append("\t\tif(service.update").append(className).append("(obj)){\n");
        sb.append("\t\t\treturn this.createBaseResult(\"update success\", true);\n");
        sb.append("\t\t}else{\n");
        sb.append("\t\t\treturn this.createBaseResult(\"update failure\", false);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genDelete(){
        comment = new CommentTemplate("删除信息",true);
        comment.addParam("ids");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@RequestMapping(value = \"/del\", method = RequestMethod.DELETE)\n");
        sb.append("\t@ResponseBody\n");
        sb.append("\tpublic Object delete(@RequestParam(\"ids\")String ids) {\n");
        sb.append("\t\tString[] idStr = ids.split(\",\");\n");
        sb.append("\t\tboolean flag = false;\n");
        sb.append("\t\tif(idStr.length == 1 && StringUtils.isNumeric(ids)){\n");
        sb.append("\t\t\tflag = service.delete").append(className).append("(Integer.parseInt(ids));\n");
        sb.append("\t\t}else{\n");
        sb.append("\t\t\tList<Integer> idAry = new ArrayList<Integer>();\n");
        sb.append("\t\t\tfor (String id : idStr){\n");
        sb.append("\t\t\t\tif(StringUtils.isNumeric(id)){\n");
        sb.append("\t\t\t\t\tidAry.add(Integer.valueOf(id));\n");
        sb.append("\t\t\t\t}\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t\tflag = service.batchDelete").append(className).append("(idAry);\n");
        sb.append("\t\t}\n");
        sb.append("\t\tif(flag){\n");
        sb.append("\t\t\treturn this.createBaseResult(\"delete success\", true);\n");
        sb.append("\t\t}else{\n");
        sb.append("\t\t\treturn this.createBaseResult(\"delete failure\", false);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        return sb.toString();
    }

}
