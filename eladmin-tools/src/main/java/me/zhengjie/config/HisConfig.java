package me.zhengjie.config;

import lombok.Data;
import me.zhengjie.httpclient.HttpClientConfig;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

/**
 * @author yanjun
 * @date 2020-11-12 16:28
 */
@Data
@Component
@Configuration
public class HisConfig implements HttpClientConfig {

    @Value("${his.server.protocol}")
    private String protocol;

    @Value("${his.server.host}")
    private String host;

    @Value("${his.server.port}")
    private int port;

    @Value("${his.server.path}")
    private String path;

    @Value("${his.server.opName}")
    private String opName;

    private URI baseUri;

    private URI opUri;

    @Override
    public synchronized String getBaseUrl() throws Exception {
        if (null == baseUri) {
            baseUri = new URIBuilder()
                    .setScheme(protocol)
                    .setHost(host)
                    .setPort(port)
                    .setPath(path)
                    .build();
        }
        return baseUri.toString();
    }

    @Override
    public synchronized String getOpUrl() throws Exception {
        if (null == opUri) {
            opUri = new URIBuilder()
                    .setScheme(protocol)
                    .setHost(host)
                    .setPort(port)
                    .setPath(path + '/' + opName)
                    .build();

        }
        return opUri.toString();
    }

}
