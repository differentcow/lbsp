package com.lbsp.promotion.gen.template.comment;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HLJ on 2015/3/22.
 */
public class CommentTemplate {

    private String text;
    private List<String> param;
    private String author;
    private boolean isMethod;
    private boolean isReturn;

    public CommentTemplate(String text,String author){
        this.text = text;
        this.author = author;
        this.isMethod = false;
        this.isReturn = false;
    }

    public CommentTemplate(String text,boolean isReturn){
        this.text = text;
        this.isMethod = true;
        this.isReturn = isReturn;
    }

    public void addParam(String param){
        if(this.param == null){
            this.param = new ArrayList<String>();
        }
        this.param.add(param);
    }

    public String genComment(){
        StringBuffer sb = new StringBuffer("");
        sb.append(turnNextHead()).append("/**\n");
        sb.append(turnNext()).append("*\n");
        if (StringUtils.isNotBlank(text)){
            sb.append(turnNext()).append("* ");
            sb.append(text).append("\n");
            sb.append(turnNext()).append("*\n");
        }
        if (isMethod && param != null && !param.isEmpty()){
            for (String p : param){
                sb.append(turnNext()).append("* @param ");
                sb.append(p).append("\n");
            }
        }
        if (StringUtils.isNotBlank(author)){
            sb.append(turnNext()).append("* @author ");
            sb.append(author).append("\n");
        }
        if(isMethod && isReturn){
            sb.append(turnNext()).append("* @return ");
            sb.append("\n");
        }
        sb.append(turnNextHead()).append(" */\n");
        return sb.toString();
    }

    private String turnNextHead(){
        if (isMethod){
            return "\t";
        }
        return "";
    }

    private String turnNext(){
        if (isMethod){
            return "\t ";
        }
        return " ";
    }

}
