package com.lbsp.promotion.gen.template.model;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HLJ on 2015/3/22.
 */
public class ModelField {

    private String name;
    private String method;
    private String importStr;

    public ModelField(String name,String type){
        if (StringUtils.isBlank(name) || StringUtils.isBlank(type)){
            return;
        }
        int key = typeMap.get(type.toLowerCase());
        this.importStr = genImport(key);
        StringBuffer sb = new StringBuffer("\tprivate ");
        String typeStr = genFieldType(key);
        sb.append(typeStr).append(" ").append(name).append(";\n");
        this.name = sb.toString();
        sb.setLength(0);
        //set method
        sb.append("\tpublic void set").append(name.substring(0,1).toUpperCase()).append(name.substring(1));
        sb.append("(").append(typeStr).append(" ").append(name).append(") {\n");
        sb.append("\t\tthis.").append(name).append(" = ").append(name).append(";\n");
        sb.append("\t}\n\n");
        //get method
        sb.append("\tpublic ").append(typeStr).append(" get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
        sb.append("() {\n");
        sb.append("\t\treturn this.").append(name).append(";\n");
        sb.append("\t}\n\n");
        this.method = sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public String getImportStr() {
        return importStr;
    }

    private static Map<String,Integer> typeMap;

    static {
        typeMap = new HashMap<String, Integer>();
        typeMap.put("int",0);   //整型
        typeMap.put("tinyint",0);   //整型
        typeMap.put("bigint",1);    //长整型
        typeMap.put("timestamp",2); //时间
        typeMap.put("varchar",3); //字符串
        typeMap.put("char",3); //字符串
        typeMap.put("text",3); //字符串
        typeMap.put("double",4); //双浮点
    }

    private String genFieldType(int key){
        String temp = "";
        switch (key){
            case 0:
                temp = "Integer";
                break;
            case 1:
                temp = "Long";
                break;
            case 2:
                temp = "Date";
                break;
            case 3:
                temp = "String";
                break;
            case 4:
                temp = "Double";
                break;
            default:
                temp = "";
                break;
        }
        return temp;
    }

    private String genImport(int key){
        String temp = "";
        switch (key){
            case 2:
                temp = "import java.util.Date;\n";
                break;
            default:
                temp = "";
                break;
        }
        return temp;
    }


}
