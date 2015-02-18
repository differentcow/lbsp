package com.lbsp.promotion.entity.xml;

import java.util.List;
import java.util.Map;

/**
 * Created Barry on 2014/10/30.
 */
public class XmlNode {

    private String name;

    private String text;

    private Map<String,String> attributes;

    private List<XmlNode> props;

    public XmlNode(){}

    public XmlNode(String name, String text){
        this.name = name;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<XmlNode> getProps() {
        return props;
    }

    public void setProps(List<XmlNode> props) {
        this.props = props;
    }
}
