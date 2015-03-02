package com.lbsp.promotion.webplatform.search;

import com.lbsp.promotion.entity.constants.GenericConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2015/2/24.
 */
public class SolrQueryParam {

    private List<SolrQueryKey> queryKeys;

    private Integer start;

    private Integer rows;

    private List<SolrQuerySort> sorts;

    private String returType;

    private String fl;

    private List<SolrQueryKey> resultQueryKeys;

    private Boolean hightLight;

    public static final String SOLR_RETURN_TYPE_JSON = "json";
    public static final String SOLR_RETURN_TYPE_XML = "xml";

    public SolrQueryParam(){
        queryKeys = new ArrayList<SolrQueryKey>();
        resultQueryKeys = new ArrayList<SolrQueryKey>();
        sorts = new ArrayList<SolrQuerySort>();
        hightLight = false;
        returType = SOLR_RETURN_TYPE_JSON;
    }

    public List<SolrQueryKey> getQueryKeys() {
        return queryKeys;
    }

    public List<SolrQuerySort> getSorts() {
        return sorts;
    }

    public void addParam(String name,String key) {
        queryKeys.add(new SolrQueryKey(name,key));
    }

    public void addParam(String name,String key,SolrQueryKey.Operators operate) {
        queryKeys.add(new SolrQueryKey(name,key,operate));
    }

    public void addParam(SolrQueryKey queryKey) {
        queryKeys.add(queryKey);
    }

    public void addResultParam(String name,String key) {
        resultQueryKeys.add(new SolrQueryKey(name,key));
    }

    public void addResultParam(String name,String key,SolrQueryKey.Operators operate) {
        resultQueryKeys.add(new SolrQueryKey(name,key,operate));
    }

    public void addResultParam(SolrQueryKey queryKey) {
        resultQueryKeys.add(queryKey);
    }

    public void addSort(String column){
        sorts.add(new SolrQuerySort(column));
    }

    public void addSort(SolrQuerySort sort){
        sorts.add(sort);
    }

    public void addSort(String column,SolrQuerySort.Order order){
        sorts.add(new SolrQuerySort(column,order));
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getReturType() {
        return returType;
    }

    public void setReturType(String returType) {
        this.returType = returType;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public List<SolrQueryKey> getResultQueryKeys() {
        return resultQueryKeys;
    }

    public Boolean getHightLight() {
        return hightLight;
    }

    public void setHightLight(Boolean hightLight) {
        this.hightLight = hightLight;
    }
}
