package com.lbsp.promotion.gen.template.dao;


import com.lbsp.promotion.gen.template.comment.CommentTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HLJ on 2015/3/22.
 */
public class DaoTemplate {

    private String packageStr;
    private String daoName;
    private CommentTemplate comment;

    public String getFileName(){
        return this.daoName;
    }

    public DaoTemplate(String table,String packageStr){
        this.packageStr = packageStr;
        StringBuffer sb = new StringBuffer("");
        if (table.indexOf("_") != -1){
            String[] parts = table.split("_");
            for (String part : parts){
                sb.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
            }
            sb.append("Dao");
        }else{
            sb.append(table.trim().substring(0,1).toUpperCase()).append(table.trim().substring(1)).append("Dao");
        }
        this.daoName = sb.toString();
        comment = new CommentTemplate("Created on "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),"简易自动代码创建工具");
    }

    public String genDao(){
        //package
        StringBuffer sb = new StringBuffer("package ");
        sb.append(packageStr).append(";\n");
        sb.append("\n");
        //import
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import java.util.List;\n");
        //doc
        sb.append(comment.genComment());
        //frame
        sb.append("public interface ").append(daoName).append(" {\n");
        sb.append("\n");
        //body
        sb.append(genBatchDelete());
        sb.append("\n");
        //body
        sb.append("}");
        return sb.toString();
    }

    private String genBatchDelete(){
        StringBuffer sb = new StringBuffer("");
        comment = new CommentTemplate("批量删除信息",true);
        comment.addParam("ids");
        sb.append(comment.genComment());
        sb.append("\tint batchDelete(@Param(\"ids\")List<Integer> ids);\n");
        return sb.toString();
    }
}
