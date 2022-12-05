package hungry.monkey.hue.listener.http;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * @author pchauha
 */
public interface HttpClient {
    void close() throws IOException;

    CloseableHttpClient getHttpClientInstance();

    String execute(HttpPut httpPut, ResponseHandler<String> responseHandler) throws IOException;
}
