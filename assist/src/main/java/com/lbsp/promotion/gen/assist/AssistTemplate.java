package com.lbsp.promotion.gen.assist;

/**
 * Created by HLJ on 2015/3/22.
 */
public class AssistTemplate {

    private String name;
    private String content;
    private String path;
    private String suffix;
    private String realPath;

    public AssistTemplate(){

    }

    public AssistTemplate(String name, String content, String path, String suffix){
        this.name = name;
        this.content = content;
        this.path = path;
        this.suffix = suffix;
        this.realPath = path + "." + suffix;
    }

    public String getName() {
        return name;
    }

    public String getRealPath() {
        return realPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
