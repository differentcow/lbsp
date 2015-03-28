package com.lbsp.promotion.gen.template.service;

import com.lbsp.promotion.gen.template.comment.CommentTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HLJ on 2015/3/22.
 */
public class ServiceTemplate {

    private String packageStr;
    private String className;
    private String subClassName;
    private String serviceName;
    private String serviceImplName;
    private CommentTemplate comment;

    public ServiceTemplate(String table,String packageStr){
        StringBuffer sb = new StringBuffer("");
        StringBuffer sub = new StringBuffer("");
        if (table.indexOf("_") != -1){
            String[] parts = table.split("_");
            int cnt = 0;
            for (String part : parts){
                if (cnt == 0){
                    sub.append(part.trim());
                    cnt++;
                }else{
                    sub.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
                }
                sb.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
            }
        }else{
            sub.append(table.trim());
            sb.append(table.trim().substring(0,1).toUpperCase()).append(table.trim().substring(1));
        }
        this.className = sb.toString();
        this.subClassName = sub.toString();
        sb.append("Service");
        this.serviceName = sb.toString();
        sb.append("Impl");
        this.serviceImplName = sb.toString();
        this.packageStr = packageStr + "." + subClassName.toLowerCase();
    }

    public String getSubDir(){
        return this.subClassName.toLowerCase();
    }

    public String getSrvFilePath(){
        return this.serviceName;
    }

    public String getSrvImplFilePath(){
        return this.serviceImplName;
    }

    public String genService(){
        comment = new CommentTemplate("Created on "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),"简易自动代码创建工具");
        StringBuffer sb = new StringBuffer("");
        //package
        sb.append("package ").append(packageStr).append(";\n");
        sb.append("\n");
        //import
        sb.append("import com.lbsp.promotion.core.service.BaseService;\n");
        sb.append("import com.lbsp.promotion.entity.base.PageResultRsp;\n");
        sb.append("import com.lbsp.promotion.entity.model.").append(className).append(";\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        //frame
        sb.append(comment.genComment());
        sb.append("public interface ").append(serviceName).append("<T> extends BaseService<T> {\n");
        sb.append("\n");
        //body
        sb.append("\n");
        sb.append(genDetailSrv()); //View By Id
        sb.append("\n");
        sb.append(genPageListSrv()); //View With Page
        sb.append("\n");
        sb.append(genSaveSrv());   //Save
        sb.append("\n");
        sb.append(genUpdateSrv()); //Update
        sb.append("\n");
        sb.append(genDeleteSrv()); //Delete
        sb.append("\n");
        sb.append(genBatchDeleteSrv());    //Batch Delete
        //body
        //frame
        sb.append("\n}");
        return sb.toString();
    }

    public String genServiceImpl(){
        comment = new CommentTemplate("Created on "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),"简易自动代码创建工具");
        StringBuffer sb = new StringBuffer("");
        //package
        sb.append("package ").append(packageStr).append(";\n");
        sb.append("\n");
        //import
        sb.append("import com.lbsp.promotion.core.dao.").append(className).append("Dao;").append(";\n");
        sb.append("import com.lbsp.promotion.core.service.BaseServiceImpl;\n");
        sb.append("import com.lbsp.promotion.entity.base.PageResultRsp;\n");
        sb.append("import com.lbsp.promotion.entity.model.").append(className).append(";\n");
        sb.append("import com.lbsp.promotion.entity.query.GenericQueryParam;\n");
        sb.append("import com.lbsp.promotion.entity.query.QueryKey;\n");
        sb.append("import com.lbsp.promotion.entity.query.SortCond;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        //frame
        sb.append(comment.genComment());
        sb.append("@Service\n");
        sb.append("public class ").append(serviceImplName).append(" extends BaseServiceImpl").append("<").append(className).append("> implements\n");
        sb.append("\t\t").append(serviceName).append("<").append(className).append("> {\n\n");
        //body
        sb.append("\t@Autowired\n");
        sb.append("\tprivate ").append(className).append("Dao ").append(subClassName).append("Dao;\n");
        sb.append("\n");
        sb.append(genDetail()); //View By Id
        sb.append("\n");
        sb.append(genPageList()); //View With Page
        sb.append("\n");
        sb.append(genSave());   //Save
        sb.append("\n");
        sb.append(genUpdate()); //Update
        sb.append("\n");
        sb.append(genDelete()); //Delete
        sb.append("\n");
        sb.append(genBatchDelete());    //Batch Delete
        //body
        //frame
        sb.append("\n}");
        return sb.toString();
    }

    private String genSaveSrv(){
        comment = new CommentTemplate("保存信息",true);
        comment.addParam(subClassName);
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tboolean save").append(className).append("(").append(className).append(" ").append(subClassName).append(" );\n");
        return sb.toString();
    }

    private String genSave(){
        comment = new CommentTemplate("保存信息",true);
        comment.addParam(subClassName);
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@Transactional\n");
        sb.append("\tpublic boolean save").append(className).append("(").append(className).append(" ").append(subClassName).append(" ) {\n");
        sb.append("\t\tLong currentTime = System.currentTimeMillis();\n");
        sb.append("\t\tif (").append(subClassName).append(".getCreate_time() == null){\n");
        sb.append("\t\t\t").append(subClassName).append(".setCreate_time(currentTime);\n");
        sb.append("\t\t}\n");
        sb.append("\t\tif (").append(subClassName).append(".getUpdate_time() == null){\n");
        sb.append("\t\t\t").append(subClassName).append(".setUpdate_time(currentTime);\n");
        sb.append("\t\t}\n");
        sb.append("\t\treturn this.insert(").append(subClassName).append(") > 0;\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genDeleteSrv(){
        comment = new CommentTemplate("删除信息",true);
        comment.addParam("id");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tboolean delete").append(className).append("(Integer id);\n");
        return sb.toString();
    }

    private String genDelete(){
        comment = new CommentTemplate("删除信息",true);
        comment.addParam("id");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@Transactional\n");
        sb.append("\tpublic boolean delete").append(className).append("(Integer id) {\n");
        sb.append("\t\tGenericQueryParam param = new GenericQueryParam();\n");
        sb.append("\t\tparam.put(new QueryKey(\"id\", QueryKey.Operators.EQ),id);\n");
        sb.append("\t\treturn this.delete(param) > 0;\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genUpdateSrv(){
        comment = new CommentTemplate("更新信息",true);
        comment.addParam(subClassName);
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tboolean update").append(className).append("(").append(className).append(" ").append(subClassName).append(" );\n");
        return sb.toString();
    }

    private String genUpdate(){
        comment = new CommentTemplate("更新信息",true);
        comment.addParam(subClassName);
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@Transactional\n");
        sb.append("\tpublic boolean update").append(className).append("(").append(className).append(" ").append(subClassName).append(" ) {\n");
        sb.append("\t\tLong currentTime = System.currentTimeMillis();\n");
        sb.append("\t\tif (").append(subClassName).append(".getUpdate_time() == null){\n");
        sb.append("\t\t\t").append(subClassName).append(".setUpdate_time(currentTime);\n");
        sb.append("\t\t}\n");
        sb.append("\t\treturn this.update(").append(subClassName).append(") > 0;\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genBatchDeleteSrv(){
        comment = new CommentTemplate("批量删除信息",true);
        comment.addParam("ids");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tboolean batchDelete").append(className).append("(List<Integer> ids);\n");
        return sb.toString();
    }

    private String genBatchDelete(){
        comment = new CommentTemplate("批量删除信息",true);
        comment.addParam("ids");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t@Transactional\n");
        sb.append("\tpublic boolean batchDelete").append(className).append("(List<Integer> ids){\n");
        sb.append("\t\treturn ").append(subClassName).append("Dao.batchDelete(ids) > 0;\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genPageListSrv(){
        comment = new CommentTemplate("获取信集合(分页)",true);
        comment.addParam("from");
        comment.addParam("to");
        comment.addParam("start");
        comment.addParam("size");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tPageResultRsp getPageList(Long from,Long to,Integer start,Integer size);\n");
        return sb.toString();
    }

    private String genPageList(){
        comment = new CommentTemplate("获取信集合(分页)",true);
        comment.addParam("from");
        comment.addParam("to");
        comment.addParam("start");
        comment.addParam("size");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tpublic PageResultRsp getPageList(Long from,Long to,Integer start,Integer size){\n");
        sb.append("\t\tGenericQueryParam param = new GenericQueryParam();\n");
        sb.append("\t\tif(from != null)\n");
        sb.append("\t\t\tparam.put(new QueryKey(\"create_time\", QueryKey.Operators.GTE),from);\n");
        sb.append("\t\tif(to != null)\n");
        sb.append("\t\t\tparam.put(new QueryKey(\"create_time\", QueryKey.Operators.LTE),to);\n");
        sb.append("		param.addSortCond(new SortCond(\"update_time\", SortCond.Order.DESC));\n");
        sb.append("\t\tint count = this.count(param);\n");
        sb.append("\t\tparam.setNeedPaging(true);\n");
        sb.append("\t\tparam.setOffset(start);\n");
        sb.append("\t\tparam.setPageSize(size);\n");
        sb.append("\t\tList list = this.find(param);\n");
        sb.append("\t\tPageResultRsp page = new PageResultRsp();\n");
        sb.append("\t\tpage.loadPageInfo(count);\n");
        sb.append("\t\tpage.setResult(list);\n");
        sb.append("\t\treturn page;\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String genDetailSrv(){
        comment = new CommentTemplate("根据ID查询详细信息",true);
        comment.addParam("id");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\t").append(className).append(" getDetailById(Integer id);\n");
        return sb.toString();
    }

    private String genDetail(){
        comment = new CommentTemplate("根据ID查询详细信息",true);
        comment.addParam("id");
        StringBuffer sb = new StringBuffer("");
        sb.append(comment.genComment());
        sb.append("\tpublic ").append(className).append(" getDetailById(Integer id){\n");
        sb.append("\t\tGenericQueryParam param = new GenericQueryParam();\n");
        sb.append("\t\tparam.put(new QueryKey(\"id\", QueryKey.Operators.EQ),id);\n");
        sb.append("\t\treturn this.findOne(param);\n");
        sb.append("\t}\n");
        return sb.toString();
    }
}
