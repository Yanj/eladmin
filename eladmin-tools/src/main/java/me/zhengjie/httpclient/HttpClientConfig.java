package me.zhengjie.httpclient;

/**
 * @author yanjun
 * @date 2020-11-15 08:47
 */
public interface HttpClientConfig {

    String getBaseUrl() throws Exception;

    String getOpUrl() throws Exception;

    String getHost();

}
