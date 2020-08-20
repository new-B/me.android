package me.java.library.mq.ons.mqtt;

import com.google.common.base.Strings;
import me.java.library.mq.base.MessageListener;
import me.java.library.mq.ons.AbstractOnsConsumer;

/**
 * Created by sylar on 2017/1/6.
 */
public class OnsMqttConsumer extends AbstractOnsConsumer {
    private OnsMqttClient client;
    private String topic;

    @Override
    public Object getNativeConsumer() {
        return client;
    }

    @Override
    public void subscribe(String topic, MessageListener messageListener, String... tags) {
        super.subscribe(topic, messageListener, tags);

        if (client == null) {
            client = new OnsMqttClient(brokers, clientId, accessKey, secretKey);
        }

        try {
            client.subscribe(topic, messageListener);
            this.topic = topic;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe() {
        try {
            if (Strings.isNullOrEmpty(topic)) {
                client.unsubscribe(topic);
                topic = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
