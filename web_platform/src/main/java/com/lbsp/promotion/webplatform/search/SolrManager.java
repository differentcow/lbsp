package com.lbsp.promotion.webplatform.search;

import com.lbsp.promotion.util.json.JsonMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.*;

/**
 * Created by Barry on 2015/2/24.
 */
public class SolrManager {

    private String url;

    public SolrManager(String url){
        this.url = url;

    }

    /**
     * 创建Solr服务连接
     *
     * @param url
     * @return
     */
    private HttpSolrServer genSolrServer(String url){
//        String url = "http://10.13.17.38:8983/solr";
        HttpSolrServer solrServer = new HttpSolrServer(url);
        solrServer.setSoTimeout(3000); // socket read timeout
        solrServer.setConnectionTimeout(1000);
        solrServer.setDefaultMaxConnectionsPerHost(1000);
        solrServer.setMaxTotalConnections(10);
        solrServer.setFollowRedirects(false); // defaults to false
        solrServer.setAllowCompression(true);
        solrServer.setMaxRetries(1);
        return solrServer;
    }

    /**
     * 添加索引
     *
     * @param index
     * @throws IOException
     * @throws SolrServerException
     */
    public void addIndex(List<SolrIndex> index) throws IOException, SolrServerException {
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        for(SolrIndex i : index){
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField(i.getField(),i.getVal());
            docs.add(doc);
        }
        if (!docs.isEmpty()){
            HttpSolrServer solrServer = genSolrServer(url);
            solrServer.add(docs);
            solrServer.optimize();
            solrServer.commit();
        }
    }

    /**
     * 添加索引
     *
     * @param index
     * @throws IOException
     * @throws SolrServerException
     */
    public void addIndex(SolrIndex index) throws IOException, SolrServerException{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(index.getField(),index.getVal());
        HttpSolrServer solrServer = genSolrServer(url);
        solrServer.add(doc);
        solrServer.optimize();
        solrServer.commit();
    }

    /**
     * 删除索引
     *
     * @param ids
     * @throws IOException
     * @throws SolrServerException
     */
    public void deleteIndex(List<String> ids) throws IOException, SolrServerException{
        HttpSolrServer solrServer = genSolrServer(url);
        solrServer.deleteById(ids);
        solrServer.commit();
    }

    /**
     * 删除索引
     *
     * @param id
     * @throws IOException
     * @throws SolrServerException
     */
    public void deleteIndex(String id) throws IOException, SolrServerException{
        HttpSolrServer solrServer = genSolrServer(url);
        solrServer.deleteById(id);
        solrServer.commit();
    }

    /**
     * 删除索引
     *
     * @param qry
     * @throws IOException
     * @throws SolrServerException
     */
    public void deleteIndexByQuery(String qry) throws IOException, SolrServerException{
        HttpSolrServer solrServer = genSolrServer(url);
        solrServer.deleteByQuery(qry);
        solrServer.commit();
    }



    /**
     * 查询
     *
     * @param param
     * @return
     * @throws SolrServerException
     */
    public String queryToJson(SolrQueryParam param) throws SolrServerException {
        JsonMapper json = JsonMapper.getJsonMapperInstance();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("status",0);
        if(param == null)
            return json.toJson(map);
        if (param.getQueryKeys().isEmpty())
            return json.toJson(map);

        SolrQuery query = new SolrQuery();
        List<SolrQueryKey> queryKeys = param.getQueryKeys();
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < queryKeys.size();i++){
            sb.append(queryKeys.get(i).getName()).append(":").append(queryKeys.get(i).getKey());
            if (i < queryKeys.size() - 1){
                if (queryKeys.get(i).getOperate().equals(SolrQueryKey.Operators.AND))
                    sb.append(" AND ");
                if (queryKeys.get(i).getOperate().equals(SolrQueryKey.Operators.OR))
                    sb.append(" OR ");
            }
        }
        query.setQuery(sb.toString().trim());

        if (!param.getResultQueryKeys().isEmpty()){
            for (SolrQueryKey key : param.getResultQueryKeys()){
                query.addFilterQuery(key.getName()+":"+key.getKey());
            }
        }

        if (!param.getSorts().isEmpty()){
            for (SolrQuerySort sort : param.getSorts()){
                if (sort.getOrder().equals(SolrQuerySort.Order.ASC))
                    query.addSort(sort.getColumn(), SolrQuery.ORDER.asc);
                if (sort.getOrder().equals(SolrQuerySort.Order.DESC))
                    query.addSort(sort.getColumn(), SolrQuery.ORDER.desc);
            }
        }

        if (null != param.getStart()){
            query.setStart(param.getStart());
        }

        if (null != param.getRows()){
            query.setRows(param.getRows());
        }

        if (StringUtils.isNotBlank(param.getFl())){
            query.setParam("fl",param.getFl());
        }

        if (param.getHightLight()){
            query.setHighlight(true);
            StringBuffer lightSb = new StringBuffer("");
            for (SolrQueryKey key : param.getQueryKeys()){
                lightSb.append(",").append(key.getName());
            }
            if (StringUtils.isNotBlank(lightSb.toString())){
                query.setParam("hl.fl", lightSb.toString().substring(1));
                query.setHighlightSimplePre("<font color='red'>");
                query.setHighlightSimplePost("</font>");
                query.setHighlightFragsize(200);
            }
        }
        HttpSolrServer server = genSolrServer(url);
        QueryResponse result = server.query(query);

        map.put("status",1);
        map.put("response",result);

        return json.toJson(map);
    }

    public static void main(String[] args) throws SolrServerException {
        String url = "http://localhost:8080/solr/collection1";
        SolrManager solr = new SolrManager(url);
        SolrQueryParam param = new SolrQueryParam();
        param.addParam(new SolrQueryKey("question1_name","ESL"));
        String json = solr.queryToJson(param);
        System.out.print(json);
    }

}
