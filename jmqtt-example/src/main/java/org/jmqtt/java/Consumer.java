package org.jmqtt.java;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);
    private static final String broker = "tcp://127.0.0.1:1883";
    private static final String topic = "MQTT/+";
    private static final String clientId = "MQTT_SUB_CLIENT";

    public static void main(String[] args) throws MqttException, InterruptedException {
        MqttClient subClient = getMqttClient();
        subClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                LOG.info("Connect lost,do some thing to solve it");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                LOG.info("From topic: " + s);
                LOG.info("Message content: " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        subClient.subscribe(topic);
    }


    private static MqttClient getMqttClient(){
        try {
            MqttClient pubClient = new MqttClient(broker,clientId,new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            LOG.info("Connecting to broker: " + broker);
            pubClient.connect(connectOptions);
            return pubClient;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }
}
