package com.lbsp.promotion.gen;

import com.lbsp.promotion.gen.assist.AssistTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by HLJ on 2015/3/22.
 */
public class GenUtil {

    /**
     * 批量生产文件
     *
     * @param list
     */
    public static void batchGenFile(List<AssistTemplate> list){
        for (AssistTemplate at : list){
            genFile(at.getRealPath(),at.getContent());
        }
    }

    /**
     * 检查是否已有文件夹，如果没有创建
     *
     * @param dir
     */
    public static void genDir(String dir){
        File file = new File(dir);
        if (file.exists() && file.isDirectory()){
            return;
        }
        file.mkdir();
    }

    /**
     * 生成文件
     *
     * @param path
     * @param content
     */
    public static void genFile(String path,String content,boolean isAppend,String encoding){
        String fileName = path;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileName,isAppend), encoding);
            osw.write(content);
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
    }

    /**
     * 生成文件
     *
     * @param path
     * @param content
     */
    public static void genFile(String path,String content){
        String fileName = path;
        String encoding = "UTF-8";
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
            osw.write(content);
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
    }

}
