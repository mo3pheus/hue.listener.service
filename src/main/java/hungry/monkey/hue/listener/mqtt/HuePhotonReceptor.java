package hungry.monkey.hue.listener.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hungry.monkey.hue.listener.domain.HuePhoton;
import hungry.monkey.hue.listener.http.ApacheHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class HuePhotonReceptor implements MqttCallback {

    /**
     * hue.bridge.ip = 192.168.1.242
     * hue.user.name = ndrPGDf9pdTxmpi3FaTKR5MdhBXxbL9mchjveUD8
     */
    private String hueBridgeIp = "192.168.1.242";

    private String hueUserName = "ndrPGDf9pdTxmpi3FaTKR5MdhBXxbL9mchjveUD8";
    private ApacheHttpClient apacheHttpClient = new ApacheHttpClient();

    @PostConstruct
    private void debug() {
        log.info("Hue ip = " + hueBridgeIp + " hueUserName " + hueUserName);
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("Mqtt connection lost.", cause);
    }

    /**
     * public void testRequest() {
     * try {
     * ResponseHandler<String> responseHandler = response -> {
     * int status = response.getStatusLine().getStatusCode();
     * if (status >= 200 && status < 300) {
     * HttpEntity entity = response.getEntity();
     * return entity != null ? EntityUtils.toString(entity) : null;
     * } else {
     * throw new ClientProtocolException("Unexpected response status: " + status);
     * }
     * };
     * <p>
     * String httpResponse = apacheHttpClient.execute(createRequest(), responseHandler);
     * System.out.println(httpResponse);
     * } catch (IOException io) {
     * log.error("IO Exception", io);
     * }
     * }
     *
     * @param topic   name of the topic on the message was published to
     * @param message the actual message.
     * @throws Exception
     */

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("Received huePhoton traffic." + new String(message.getPayload()));

        ObjectMapper objectMapper = new ObjectMapper();
        HuePhoton huePhoton = objectMapper.readValue(new String(message.getPayload()), HuePhoton.class);

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };

        HttpPut hueProcessingRequest = createRequest(huePhoton);
        String hueResponse = apacheHttpClient.execute(hueProcessingRequest, responseHandler);

        log.info(hueResponse);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            log.info("Message delivery complete." + new String(token.getMessage().getPayload()));
        } catch (MqttException e) {
            log.error("Encountered Mqtt Exception", e);
        }
    }

    private HttpPut createRequest(HuePhoton huePhoton) {
        String requestUrl = "http://" + hueBridgeIp + "/api/" + this.hueUserName + "/lights/10/state";
        log.info("Firing request" + requestUrl + " huePhoton = " + huePhoton.toString());

        HttpPut httpPut = new HttpPut(requestUrl);
        StringEntity bodyEntity = new StringEntity(huePhoton.toString(), ContentType.APPLICATION_JSON);
        httpPut.setEntity(bodyEntity);

        return httpPut;
    }
}
