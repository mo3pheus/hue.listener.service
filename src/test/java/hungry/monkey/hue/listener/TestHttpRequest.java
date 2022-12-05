package hungry.monkey.hue.listener;

import hungry.monkey.hue.listener.domain.HuePhoton;
import hungry.monkey.hue.listener.http.ApacheHttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class TestHttpRequest {
    private String hueBridgeIp = "192.168.1.242";
    private String hueUserName = "ndrPGDf9pdTxmpi3FaTKR5MdhBXxbL9mchjveUD8";
    private ApacheHttpClient apacheHttpClient = new ApacheHttpClient();

    @Test
    public void testRequest() {
        try {
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String httpResponse = apacheHttpClient.execute(createRequest(), responseHandler);
            System.out.println(httpResponse);
        } catch (IOException io) {
            log.error("IO Exception", io);
        }
    }

    @SneakyThrows
    private HttpPut createRequest() {
        Float[] xy = new Float[2];
        xy[0] = 0.5425f;
        xy[1] = 0.4196f;

        HuePhoton huePhoton = new HuePhoton(xy, 12750, 200, 150, true);
        String requestUrl = "http://" + hueBridgeIp + "/api/" + this.hueUserName + "/lights/10/state";
        log.info("Firing request" + requestUrl + " huePhoton = " + huePhoton.toString());

        HttpPut httpPut = new HttpPut(requestUrl);
        StringEntity bodyEntity = new StringEntity(huePhoton.toString(), ContentType.APPLICATION_JSON);
        httpPut.setEntity(bodyEntity);

        return httpPut;
    }
}
