package hungry.monkey.hue.listener.http;

import hungry.monkey.hue.listener.domain.HuePhoton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

@Slf4j
public class RequestUtil {
    @Getter
    private String hueBridgeIp;

    @Getter
    private String hueBridgeUserName;

    @Autowired
    public RequestUtil(@Value("${hue.bridge.ip}") String hueBridgeIp,
                       @Value("${hue.user.name}") String hueBridgeUserName) {
        this.hueBridgeUserName = hueBridgeUserName;
        this.hueBridgeIp = hueBridgeIp;
    }

    @PostConstruct
    public void debugMethod() {
        log.info("Created Request Util " + hueBridgeIp + " " + hueBridgeUserName);
    }

    public HttpPost createPostRequest(@NotNull HuePhoton huePhoton) {
        try {
            String requestUrl = "https://" + hueBridgeIp + "/api/"
                    + hueBridgeUserName + "/lights/10/state";
            log.info("Firing request" + requestUrl + " huePhoton = " + huePhoton.toString());

            HttpPost httpPost = new HttpPost(requestUrl);
            StringEntity bodyEntity = new StringEntity(huePhoton.toString());
            httpPost.setEntity(bodyEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            return httpPost;
        } catch (UnsupportedEncodingException e) {
            log.error("Could not create HttpPost request.", e);
            return null;
        }
    }
}
