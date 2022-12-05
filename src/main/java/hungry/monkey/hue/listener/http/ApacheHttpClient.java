package hungry.monkey.hue.listener.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class ApacheHttpClient implements HttpClient {
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * @return This returns an instance of closeableHttpClient
     */
    public CloseableHttpClient getHttpClientInstance() {
        return httpClient;
    }

    public void close() throws IOException {
        httpClient.close();
    }

    @Override
    public String execute(HttpPut httpRequest, ResponseHandler<String> responseHandler) throws IOException {
        return httpClient.execute(httpRequest, responseHandler);
    }
}
