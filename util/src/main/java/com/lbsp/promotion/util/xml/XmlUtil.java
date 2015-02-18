package com.lbsp.promotion.util.xml;

import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.xml.XmlNode;
import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;

public class XmlUtil {
	private static XmlUtil xmlUtil;

	private XmlUtil() {

	}

	private static class InnerXmlUtil {
		public static XmlUtil getXmlUtil() {
			if (xmlUtil == null) {
				xmlUtil = new XmlUtil();
			}
			return xmlUtil;
		}
	}

	public static XmlUtil getXmlUtilInstance() {
		return InnerXmlUtil.getXmlUtil();
	}

    public List<Map<String, String>> parseFile(String path) {
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            InputStream is = new FileInputStream(file);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element connections = document.getRootElement();
            Iterator<Element> root = connections.elementIterator();
            while (root.hasNext()) {
                Element resource = root.next();
                getElementList(list, resource);
            }
        } catch (Exception e) {
            throw new RuntimeException("读取" + path + "文件流错误" + e);
        }
        return list;
    }

	public List<Map<String, String>> parseConfigFile(String configName) {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(configName);
		SAXReader saxReader = new SAXReader();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			Document document = saxReader.read(is);
			Element connections = document.getRootElement();
			Iterator<Element> root = connections.elementIterator();
			while (root.hasNext()) {
				Element resource = root.next();
				getElementList(list, resource);
			}
		} catch (DocumentException e) {
			throw new RuntimeException("读取" + configName + "文件流错误" + e);
		}
		return list;
	}

	private void getElementList(List<Map<String, String>> list, Element element) {
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			List<Attribute> attrs = element.attributes();
			if (attrs != null) {
				Map<String, String> map = new HashMap<String, String>();
				for (Attribute attr : attrs) {
					map.put(attr.getName(), attr.getValue());
				}
				list.add(map);
			}
		} else {
			// 有子元素
			for (Iterator it = elements.iterator(); it.hasNext();) {
				Element elem = (Element) it.next();
				// 递归遍历
				getElementList(list, elem);
			}
		}
	}

    //创建菜单xml文件
    private Document genXml(Element xmlNode,List<XmlNode> nodes) throws Exception{
        Document document = null;
        if(xmlNode == null){
            document = DocumentHelper.createDocument();
        }
        for (XmlNode node : nodes){
            Element newNode = null;
            //创建节点
            if (xmlNode == null){
                newNode = document.addElement(node.getName());
            }else{
                newNode = xmlNode.addElement(node.getName());
            }
            //写入文本
            if (StringUtils.isNotBlank(node.getText())){
               newNode.addText(node.getText());
            }
            //写入节点所有属性
            if (node.getAttributes() != null && !node.getAttributes().isEmpty()){
                for (Map.Entry<String,String> map : node.getAttributes().entrySet()){
                    newNode.addAttribute(map.getKey(),map.getValue());
                }
            }
            //判断是否有子节点，如果有，递归处理
            if (node.getProps() != null && !node.getProps().isEmpty()){
                genXml(newNode,node.getProps());
            }
        }

        return document;
    }

    public void createXMLFile(XmlNode node,String path) throws Exception{
        createXMLFile(node,new File(path));
    }

    public void createXMLFile(XmlNode node,File file) throws Exception{
        List<XmlNode> nodes = new ArrayList<XmlNode>();
        nodes.add(node);
        Document document= genXml(null,nodes);
        //输出全部原始数据，在编译器中显示
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(GenericConstants.TEXT_CODE_UTF_8);//根据需要设置编码
        document.normalize();
        // 输出全部原始数据，并用它生成新的我们需要的XML文件
        XMLWriter writer = new XMLWriter(new FileWriter(file), format);
        writer.write(document); //输出到文件
        if(writer != null){writer.close();}
    }

    /**
     * 生成XML字符串
     *
     * @param node
     * @return
     * @throws Exception
     */
    public String createXMLString(XmlNode node) throws Exception{

        List<XmlNode> nodes = new ArrayList<XmlNode>();
        nodes.add(node);
        Document document= genXml(null,nodes);
        //输出全部原始数据，在编译器中显示
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(GenericConstants.TEXT_CODE_UTF_8);//根据需要设置编码
        document.normalize();
        // 输出全部原始数据，并用它生成新的我们需要的XML文件
        StringWriter out = new StringWriter(1024);
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document); //输出到文件
        String xml=out.toString();
        if(writer != null){writer.close();}
        if(out != null){out.close();}

        return xml;
    }

}
