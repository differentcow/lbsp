package com.lbsp.promotion.coreplatform.push.model;

/**
 * Created by HLJ on 2015/4/13.
 */
public class PushMessage {

    private String title;

    private String description;

    private String palload;

    private Integer translateWay = 1;

    private Integer voiceType = 1;

    private String regId;

    private String alias;

    private String topic;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPalload() {
        return palload;
    }

    public void setPalload(String palload) {
        this.palload = palload;
    }

    public Integer getTranslateWay() {
        return translateWay;
    }

    public void setTranslateWay(Integer translateWay) {
        this.translateWay = translateWay;
    }

    public Integer getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(Integer voiceType) {
        this.voiceType = voiceType;
    }
}
