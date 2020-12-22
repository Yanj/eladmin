package me.zhengjie.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author yanjun
 * @date 2020-11-15 08:37:00
 */
public abstract class AbstractHttpClient implements InitializingBean {

    private final HttpClientConfig config;
    private final ConnectionManager connectionManager;
    private CloseableHttpClient httpClient;

    public AbstractHttpClient(HttpClientConfig config, ConnectionManager connectionManager) {
        this.config = config;
        this.connectionManager = connectionManager;
    }

    public <T> T doGet(String uri, ResponseHandler<? extends T> responseHandler) throws Exception {
        System.out.println("uri:" + uri);
        HttpGet get = new HttpGet(uri);
        return execute(get, responseHandler);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws Exception {
        Args.notNull(responseHandler, "Response handler");

        HttpResponse response = getHttpClient().execute(request);

        final T result;
        try {
            result = responseHandler.handleResponse(response);
        } catch (Exception t) {
            HttpEntity entity = response.getEntity();
            try {
                EntityUtils.consume(entity);
            } catch (Exception e1) {

            }
            throw t;
        }

        EntityUtils.consume(response.getEntity());
        return result;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpClientConfig getConfig() {
        return config;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initHttpClient();
    }

    private void initHttpClient() throws Exception {
        HttpClientBuilder builder = HttpClientBuilder.create();

        // https
        initHttps(builder);
        // connection pool
        builder.setConnectionManager(getConnectionManager());

        builder.disableAutomaticRetries();
        builder.setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(30 * 1000)
                .setSocketTimeout(60 * 1000)
                .setConnectionRequestTimeout(30 * 1000)
                .build());

        httpClient = builder.build();
    }

    private void initHttps(HttpClientBuilder builder) throws Exception {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager[] tm = {xtm};
        sslContext.init(null, tm, new SecureRandom());

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        builder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, hostnameVerifier));
        builder.setSSLHostnameVerifier(hostnameVerifier);
    }

}
