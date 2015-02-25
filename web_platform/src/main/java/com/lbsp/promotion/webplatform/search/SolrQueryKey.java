package com.lbsp.promotion.webplatform.search;

/**
 * Created by Barry on 2015/2/24.
 */
public class SolrQueryKey {

    private String name;

    private String key;

    private Operators operate;

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOperate(Operators operate) {
        this.operate = operate;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Operators getOperate() {
        return operate;
    }

    public SolrQueryKey(String name,String key){
        this.name = name;
        this.key = key;
        this.operate = Operators.AND;
    }

    public SolrQueryKey(String name,String key,Operators operate){
        this.name = name;
        this.key = key;
        this.operate = operate;
    }
    public enum Operators {
        AND {
            public String value() {
                return "AND";
            }
        },
        OR {
            public String value() {
                return "OR";
            }
        }
    }
}
