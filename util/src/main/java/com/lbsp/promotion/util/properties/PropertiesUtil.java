package com.lbsp.promotion.util.properties;

import com.lbsp.promotion.util.cache.CacheMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Barry on 2014/10/27.
 */
public class PropertiesUtil {

    private Properties prop;

    public PropertiesUtil() {

    }

    /**
     * 根据路径生成Properties实例
     *
     * @param path
     * @return
     */
    public void parsePath(String path){
        try {
            InputStream in = new FileInputStream(path);
            prop = new Properties();
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据Properties文件路径以及Key，获取值
     *
     * @param path
     * @param key
     * @return
     */
    public String getValue(String path,String key){
        if(prop == null){
            parsePath(path);
        }
        return getValue(key);
    }

    /**
     * 根据Key获取值
     *
     * @param key
     * @return
     */
    public String getValue(String key){
        if(CacheMapping.get(key) != null){
            return (String) CacheMapping.get(key);
        }
        if(prop != null){
            return prop.getProperty(key,"");
        }
        return "";
    }

}
