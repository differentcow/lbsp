package com.lbsp.promotion.util;

import java.util.Map;

/**
 * Created by Barry on 2014/10/29.
 */
public class MessageUtil {

    private static MessageUtil messageUtil;

    private MessageUtil() {

    }

    private static class InnerMessageUtil {
        public static MessageUtil getMessageUtil() {
            if (messageUtil == null) {
                messageUtil = new MessageUtil();
            }
            return messageUtil;
        }
    }

    public static MessageUtil getXmlUtilInstance() {
        return InnerMessageUtil.getMessageUtil();
    }

    /**
     * 根据msg所带的{param}参数，替换map的value
     *
     * @param content
     * @param map
     * @return
     */
    public String translateMsg(String content,Map<String,String> map){
        if (map == null){
            return content;
        }
        for (Map.Entry<String,String> m : map.entrySet()){
            content = content.replaceAll("\\{"+m.getKey()+"\\}",m.getValue());
        }
        return content;
    }

    /**
     * 根据msg所带的{param}参数，替换字符串数组的value
     *
     * @param content
     * @param args
     * @return
     */
    public String translateMsg(String content,String[] args){
        if (args == null){
            return content;
        }
        for (int i = 0; i < args.length; i++) {
            int start = content.indexOf("{");
            int end = content.indexOf("}");
            content = content.substring(0,start) + args[i] + content.substring(end+1);
        }
        return content;
    }

/*
    public static void main(String[] args){
        String content = "验证码为：{authCode}\n请于30分钟内使用验证码，过期无效.\r\n\r\n广州市景瑞贸易有限公司.\r\n联系电话：{phone}";
        Map<String,String> map = new HashMap<String, String>();
        map.put("authCode","Texodao2");
        map.put("phone","020-87654321");
//        String msg = MessageUtil.getXmlUtilInstance().translateMsg(content,new String[]{"asdasdasdsa","020-87654321"});
        String msg = MessageUtil.getXmlUtilInstance().translateMsg(content,map);
        System.out.print(msg);
    }
*/

}
