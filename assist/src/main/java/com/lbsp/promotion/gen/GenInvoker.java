package com.lbsp.promotion.gen;

/**
 * Created by HLJ on 2015/3/22.
 */
public class GenInvoker {


    public static void main(String[] args){
        GenCommonManager manager = new GenCommonManager();
        try {
            manager.build("collection", true, "lbsp", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
