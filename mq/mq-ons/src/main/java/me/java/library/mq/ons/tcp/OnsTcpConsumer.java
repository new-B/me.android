package me.java.library.mq.ons.tcp;


import com.aliyun.openservices.ons.api.*;
import com.google.common.base.Charsets;
import me.java.library.mq.base.Message;
import me.java.library.mq.base.MessageListener;
import me.java.library.mq.base.MqProperties;
import me.java.library.mq.ons.AbstractOnsConsumer;
import me.java.library.mq.ons.Utils;

import java.util.Properties;

/**
 * Created by sylar on 2017/1/6.
 */
public class OnsTcpConsumer extends AbstractOnsConsumer {

    Consumer consumer;

    public OnsTcpConsumer(MqProperties mqProperties, String groupId, String clientId) {
        super(mqProperties, groupId, clientId);
    }

    @Override
    protected void onSubscribe(String topic, MessageListener messageListener, String... tags) throws Exception {
        initConsumer();

        String subExpression = Utils.tagsFromArray(tags);

        consumer.subscribe(
                topic,
                subExpression,
                (message, context) -> {
                    String content = new String(message.getBody(), Charsets.UTF_8);
                    try {
                        Message msg = new Message(topic, content);
                        msg.setKey(message.getKey());
                        msg.setTag(message.getTag());
                        messageListener.onSuccess(msg);
                        return Action.CommitMessage;
                    } catch (Exception e) {
                        String err = String.format("处理消息发生异常. msgId:%s\ncontent:%s\n%s", message.getMsgID(), content, e
                                .getMessage());
                        System.out.println(err);
                        e.printStackTrace();

                        return Action.ReconsumeLater;
                    }
                });

        consumer.start();
    }

    @Override
    protected void onUnsubscribe() throws Exception {
        consumer.shutdown();
        consumer = null;
    }

    private void initConsumer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, mqProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, mqProperties.getSecretKey());
        properties.put(PropertyKeyConst.NAMESRV_ADDR, mqProperties.getBrokers());
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        //集群订阅方式（默认）
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        consumer = ONSFactory.createConsumer(properties);
    }

}
