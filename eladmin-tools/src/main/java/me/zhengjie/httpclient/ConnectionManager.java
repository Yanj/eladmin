package me.zhengjie.httpclient;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author yanjun
 * @date 2020-11-15 19:28
 */
@Component
public class ConnectionManager extends PoolingHttpClientConnectionManager {

    @PostConstruct
    private void init() {
        setMaxTotal(10);
        setDefaultMaxPerRoute(2);
    }

}
