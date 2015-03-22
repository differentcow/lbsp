package com.lbsp.promotion.gen.template.test;

import com.lbsp.promotion.gen.GenUtil;
import com.lbsp.promotion.gen.template.controller.ControllerTemplate;
import com.lbsp.promotion.gen.template.dao.DaoTemplate;
import com.lbsp.promotion.gen.template.model.ModelField;
import com.lbsp.promotion.gen.template.model.ModelTemplate;
import com.lbsp.promotion.gen.template.service.ServiceTemplate;
import com.lbsp.promotion.gen.template.xml.SQLXmlTemplate;

/**
 * Created by HLJ on 2015/3/22.
 */
public class GenTest {

    public static void main(String[] args){
        try{
            String table = "testdemo";
            String path = "";
            String basePath = "E:/Eproject/lbsp/";
            String module = "";
            String resourcePath = "/src/main/resources/mapper/";
            String packagePath = "/src/main/java/com/lbsp/promotion/";
            String customerPath = "";
            String customerPackage = "entity.model";
            String suffix = ".java";
            String xmlSuffix = ".xml";
            //1，创建Model
            ModelTemplate mt = new ModelTemplate(table,"com.lbsp.promotion."+customerPackage);
            mt.addFields(new ModelField("user_name","varchar"));
            mt.addFields(new ModelField("time","bigint"));
            mt.addFields(new ModelField("user_id","int"));
            mt.addFields(new ModelField("status","char"));
            mt.addFields(new ModelField("height","double"));
            mt.addFields(new ModelField("description","text"));
            mt.addFields(new ModelField("birthday","timestamp"));
            mt.addFields(new ModelField("gender","tinyint"));
            String model = mt.genModel();
            module = "entity";
            customerPath = "entity/model/";
            path = basePath + module + packagePath + customerPath + mt.getFileName() + suffix;
            GenUtil.genFile(path, model);
            //2，创建Xml
            SQLXmlTemplate xml = new SQLXmlTemplate(table);
            module = "core";
            path = basePath + module + resourcePath + xml.getFileName() + xmlSuffix;
            GenUtil.genFile(path,xml.genXml());
            //3，创建Dao
            customerPackage = "core.dao";
            DaoTemplate dao = new DaoTemplate(table,"com.lbsp.promotion."+customerPackage);
            module = "core";
            customerPath = "core/dao/";
            path = basePath + module + packagePath + customerPath + dao.getFileName() + suffix;
            GenUtil.genFile(path,dao.genDao());
            //4，创建Service
            customerPackage = "core.service";
            ServiceTemplate srv = new ServiceTemplate(table,"com.lbsp.promotion."+customerPackage);
            module = "core";
            customerPath = "core/service/";
            path = basePath + module + packagePath + customerPath + srv.getSubDir();
            GenUtil.genDir(path);
            String srvPath = path + "/" + srv.getSrvFilePath() + suffix;
            GenUtil.genFile(srvPath,srv.genService());
            srvPath = path + "/" + srv.getSrvImplFilePath() + suffix;
            GenUtil.genFile(srvPath,srv.genServiceImpl());
            //5，创建Controller
            customerPackage = "coreplatform.controller";
            ControllerTemplate controller = new ControllerTemplate(table,"com.lbsp.promotion."+customerPackage);
            module = "core_platform";
            customerPath = "coreplatform/controller/";
            path = basePath + module + packagePath + customerPath + controller.getSubdir();
            GenUtil.genDir(path);
            String filePath = path + "/" + controller.getFileName() + suffix;
            GenUtil.genFile(filePath,controller.genController());
            //6，生成对应的功能权限

            //7，是否编译
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }
}
