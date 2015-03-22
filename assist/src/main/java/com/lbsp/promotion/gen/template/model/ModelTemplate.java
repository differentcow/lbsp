package com.lbsp.promotion.gen.template.model;

import com.lbsp.promotion.gen.template.comment.CommentTemplate;
import org.apache.commons.lang.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HLJ on 2015/3/22.
 */
public class ModelTemplate {

    private String packagePath;
    private String modelName;
    private String className;
    private boolean isAnnoation;
    private List<ModelField> fields;
    private StringBuffer importPart;
    private StringBuffer fieldPart;
    private StringBuffer methodPart;
    private CommentTemplate comment;

    public ModelTemplate(String table,String path){
        this.modelName = table;
        this.packagePath = path;
        if (table.indexOf("_") != -1){
            isAnnoation = true;
            String[] tableParts = table.trim().split("_");
            StringBuffer sb = new StringBuffer("");
            for (String part:tableParts){
                sb.append(part.substring(0,1).toUpperCase()).append(part.substring(1));
            }
            if (StringUtils.isNotBlank(sb.toString())){
                this.className = sb.toString();
            }
        }else{
            isAnnoation = false;
            this.className = table.trim().substring(0,1).toUpperCase()+table.trim().substring(1);
        }
        comment = new CommentTemplate("Created on "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),"简易自动代码创建工具");
    }

    public String getFileName(){
        return this.className;
    }

    public void addFields(ModelField mf){
        if (fields == null){
            fields = new ArrayList<ModelField>();
        }
        fields.add(mf);
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }

    public String genModel(){
        //解析字段属性
        if (fields != null && !fields.isEmpty()){
            importPart = new StringBuffer("");
            fieldPart = new StringBuffer("");
            methodPart = new StringBuffer("");
            for (ModelField mf : fields){
                importPart.append(mf.getImportStr());
                fieldPart.append(mf.getName());
                methodPart.append(mf.getMethod());
            }
        }
        //框架组件
        String model = genFrame();
        return model;
    }

    private String genFrame(){
        StringBuffer sb = new StringBuffer("package ");
        sb.append(packagePath).append(";");
        sb.append("\n\n");
        if (importPart != null && StringUtils.isNotBlank(importPart.toString())){
            sb.append(importPart.toString());
        }
        //导入注释类
        if (isAnnoation){
            sb.append("import com.lbsp.promotion.entity.table.annotation.MyTable;\n");
        }
        sb.append("\n");
        sb.append(comment.genComment());
        if (isAnnoation){
            sb.append("@MyTable(value = \"").append(modelName).append("\")\n");
        }
        sb.append("public class ").append(className).append(" extends BaseModel {");
        //------------------------主体-------------------------------------------------
        if (fieldPart != null && StringUtils.isNotBlank(fieldPart.toString())){
            sb.append("\n");
            sb.append(fieldPart.toString());
            sb.append("\n");
        }
        if (methodPart != null && StringUtils.isNotBlank(methodPart.toString())){
            sb.append("\n");
            sb.append(methodPart.toString());
            sb.append("\n");
        }
        //------------------------主体-------------------------------------------------
        sb.append("}");
        if (StringUtils.isNotBlank(sb.toString())){
            return sb.toString();
        }
        return "";
    }

    public static void main(String[] args){
        ModelTemplate mt = new ModelTemplate("testuser","com.lbsp.promotion.entity.model");
        mt.addFields(new ModelField("user_name","varchar"));
        mt.addFields(new ModelField("time","bigint"));
        mt.addFields(new ModelField("user_id","int"));
        mt.addFields(new ModelField("status","char"));
        mt.addFields(new ModelField("height","double"));
        mt.addFields(new ModelField("description","text"));
        mt.addFields(new ModelField("birthday","timestamp"));
        mt.addFields(new ModelField("gender","tinyint"));
        String model = mt.genModel();
        System.out.print(model);
        try{
            /*FileWriter fw = new FileWriter("E:/Eproject/lbsp/entity/src/main/java/com/lbsp/promotion/entity/model/"+mt.getFileName()+".java");
            fw.write(model);
            fw.close();*/
            String fileName = "E:/Eproject/lbsp/entity/src/main/java/com/lbsp/promotion/entity/model/"+mt.getFileName()+".java";
            String encoding = "UTF-8";
            OutputStreamWriter osw = null;
            try {
                osw = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
                osw.write(model);
                osw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(osw!=null)
                    try {
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
