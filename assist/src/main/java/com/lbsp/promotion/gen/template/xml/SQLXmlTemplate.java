package com.lbsp.promotion.gen.template.xml;

/**
 * Created by HLJ on 2015/3/22.
 */
public class SQLXmlTemplate {

    private String xmlName;
    private String daoName;
    private String fileName;

    public SQLXmlTemplate(String table){
        this.xmlName = table;
        StringBuffer sb = new StringBuffer("");
        StringBuffer sub = new StringBuffer("");
        if (table.indexOf("_") != -1){
            String[] parts = table.split("_");
            int cnt = 0;
            for (String part : parts){
                if (cnt == 0){
                    sb.append(part.trim());
                    cnt++;
                }else{
                    sb.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
                }
                sub.append(part.trim().substring(0,1).toUpperCase()).append(part.trim().substring(1));
            }
            sub.append("Dao");
        }else{
            sub.append(table.trim().substring(0, 1).toUpperCase()).append(table.trim().substring(1)).append("Dao");
            sb.append(table.trim().substring(0, 1)).append(table.trim().substring(1));
        }
        this.daoName = sub.toString();
        this.fileName = sb.toString();
    }

    public String getFileName() {
        return fileName;
    }

    public String genXml(){
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<!DOCTYPE mapper\n");
        sb.append("PUBLIC \"-//ibatis.apache.org//DTD Mapper 3.0//EN\"\n");
        sb.append("\"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"com.lbsp.promotion.core.dao.").append(daoName).append("\">\n");
        //-------------------主体---------------------------------
        sb.append(genBatchDelete());
        sb.append("\n");
        //-------------------主体---------------------------------
        sb.append("</mapper>");
        return sb.toString();
    }

    private String genBatchDelete(){
        StringBuffer sb = new StringBuffer("\t<delete id=\"batchDelete\">\n");
        sb.append("\t\tdelete from ").append(xmlName).append("\n");
        sb.append("\t\twhere id in\n");
        sb.append("\t\t<foreach collection=\"ids\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n");
        sb.append("\t\t\t#{item}\n");
        sb.append("\t\t</foreach>\n");
        sb.append("\t</delete>\n");
        return sb.toString();
    }


}
