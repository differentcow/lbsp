package com.lbsp.promotion.util.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by barry on 2014/10/26.
 */
public class CacheMapping {

    private  static Map<String,Object> map;

    public static void put(String QName,Object obj){
        genInstance();
        map.put(QName,obj);
    }

    public static Object get(String QName){
        genInstance();
        return map.get(QName);
    }

    private static void genInstance(){
        if(map == null){
            map = new HashMap<String, Object>();
        }
    }


}
