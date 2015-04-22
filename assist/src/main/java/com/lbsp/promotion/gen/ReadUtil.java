package com.lbsp.promotion.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 2015/4/21.
 */
public class ReadUtil {

    public static void main(String[] args){
        String path = "D:\\NEWProject\\lbsp\\assist\\src\\main\\resources\\webtemplate\\HttpJs_template.txt";
        Map<String,String> map = new HashMap<String, String>();
        map.put("date",new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map.put("module","advert");
        map.put("upperModule","Advert");
        String content = readContentReplaceWithParam(path, map);
        System.out.println(content);
//        GenUtil.genFile(path,content,false,"utf-8");
    }


    public static String readContentReplaceWithParam(String filename,String module){
        Map<String,String> map = new HashMap<String, String>();
        map.put("date",new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        map.put("module",module);
        map.put("upperModule",module.substring(0,1).toUpperCase()+module.substring(1));
        return readContentReplaceWithParam(filename,map);
    }

    public static String readContentReplaceWithParam(String filename,Map<String,String> map){
        File file = new File(filename);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            while ((tempString = reader.readLine()) != null) {
                if(tempString.indexOf("${") != -1 && tempString.indexOf("}") != -1){
                    int start = tempString.indexOf("${")+2;
                    int end = tempString.indexOf("}");
                    String temp = tempString.substring(start,end);
                    tempString = tempString.replaceAll("\\$\\{"+temp+"\\}",map.get(temp));
                    sb.append(tempString).append("\n");
                }else{
                    sb.append(tempString).append("\n");
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    public static String readContent(String fileName){
        return readContent(fileName,0,"","");
    }

    public static String readContentBeforeAppend(String fileName,String before,String content){
        return readContent(fileName,1,before,content);
    }

    public static String readContentAfterAppend(String fileName,String after,String content){
        return readContent(fileName,2,after,content);
    }

    public static String readContent(String fileName,int posAppend,String before,String content){
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            while ((tempString = reader.readLine()) != null) {
                // posAppend == 1 指定对比字符串行的前面添加插入内容
                if(posAppend == 1 && before.equals(tempString.trim())){
                    sb.append("\t").append(content).append("\n");
                }
                sb.append(tempString).append("\n");
                // posAppend == 2 指定对比字符串行的后面添加插入内容
                if(posAppend == 2 && before.equals(tempString.trim())){
                    sb.append("\t").append(content).append("\n");
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

}
