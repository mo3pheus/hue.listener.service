package hungry.monkey.hue.listener.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class HueProcessor {

    @Value("${mqtt.host.url}")
    private String mqttHostUrl;

    @Value("${mqtt.telemetry.topic}")
    private String topicName;

    private IMqttClient mqttClient;

    @PostConstruct
    public void setUpMqtt() throws MqttException {
        mqttClient = new MqttClient(mqttHostUrl, MqttAsyncClient.generateClientId());

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMaxInflight(20);

        mqttClient.connect(mqttConnectOptions);
        mqttClient.subscribe(topicName);
        mqttClient.setCallback(new HuePhotonReceptor());

        log.info("Mqtt connection has been established and receptor set up.");
    }
}
