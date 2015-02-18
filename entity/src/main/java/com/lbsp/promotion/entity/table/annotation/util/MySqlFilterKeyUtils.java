package com.lbsp.promotion.entity.table.annotation.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Barry on 2014/10/20.
 */
public class MySqlFilterKeyUtils {

    private static Map<String,String> map;

    private static Map<String,String> initFilterKeyMap(){
        if(map == null) {
            map = new HashMap<String, String>();
            map.put("desc","`desc`");
            map.put("index","`index`");
            map.put("code","`code`");
            map.put("column","`column`");
            map.put("table","`table`");
            map.put("value","`value`");
            map.put("interface","`interface`");
            map.put("to","`to`");
            return map;
        }else{
            return map;
        }


    }

    public static String getFilterKey(String field){
        Map<String,String> mapTmp = initFilterKeyMap();
        String key = mapTmp.get(field.trim().toLowerCase());
        if(key != null && !"".equals(key.trim())){
            return key;
        }else{
            return field;
        }
    }

}
