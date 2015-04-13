package com.lbsp.promotion.coreplatform.push;

import com.lbsp.promotion.coreplatform.push.model.PushMessage;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import com.xiaomi.xmpush.server.TargetedMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送管理类
 *
 * Created by HLJ on 2015/4/13.
 */
public class PushManager {

    private String packageName;
    private String secretKey;
    private Integer retimes = 0;

    public PushManager(String packageName,String secretKey){
        this.packageName = packageName;
        this.secretKey = secretKey;
    }

    public PushManager(String packageName,String secretKey,Integer retimes){
        this.packageName = packageName;
        this.secretKey = secretKey;
        this.retimes = retimes;
    }


    private Message buildMessage(String title,String payload,String description,
                                 Integer way,Integer voice) throws Exception {
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(payload)
                .restrictedPackageName(packageName)
                .passThrough(way)
                .notifyType(voice)
                .build();
        return message;
    }

    private TargetedMessage buildTargetMessageByAlias(String title,String payload,String description,String alias,
                                                      Integer way,Integer voice) throws Exception {
        TargetedMessage message = new TargetedMessage();
        message.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, alias);
        message.setMessage(buildMessage(title, payload, description,way,voice));
        return message;
    }

    /**
     * 根据Alias推送消息
     *
     * @param msgs
     * @throws Exception
     */
    public void sendMessageByAlias(List<PushMessage> msgs) throws Exception{
        Constants.useOfficial();
        Sender sender = new Sender(secretKey);
        List<TargetedMessage> messages = new ArrayList<TargetedMessage>();
        for (PushMessage m : msgs){
            TargetedMessage targetedMessage = buildTargetMessageByAlias(m.getTitle(),m.getPalload(),
                    m.getDescription(),m.getAlias(),m.getTranslateWay(),m.getVoiceType());
            messages.add(targetedMessage);
        }
        sender.send(messages, retimes); //根据alias，发送消息到指定设备上，重试retimes。
    }

    /**
     * 全推送(单条)
     *
     * @param title
     * @param payload
     * @param description
     * @param way
     * @param voice
     * @throws Exception
     */
    public void sendBroadcast(String title,String payload,String description,Integer way,Integer voice) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(secretKey);
        Message message = buildMessage(title,payload,description,way,voice);
        sender.broadcastAll(message, retimes); //全推送，重试重试retimes。
    }

    /**
     * 全推送(单条)
     *
     * @param msg
     * @throws Exception
     */
    public void sendBroadcast(PushMessage msg) throws Exception {
        Constants.useOfficial();
        sendBroadcast(msg.getTitle(), msg.getPalload(), msg.getDescription(),msg.getTranslateWay(),msg.getVoiceType());
    }

    /**
     * 全推送(集合)
     *
     * @param msgs
     * @throws Exception
     */
    public void sendBroadcastList(List<PushMessage> msgs) throws Exception {
        Constants.useOfficial();
        for (PushMessage m : msgs){
            sendBroadcast(m);
        }
    }


}
