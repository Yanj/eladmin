package me.zhengjie.config;

import lombok.Data;
import me.zhengjie.httpclient.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author yanjun
 * @date 2021-01-11 11:33
 */
@Data
@Component
@Configuration
public class SmsConfig implements HttpClientConfig {

    @Value("${sms.enable}")
    private boolean enable;

    @Value("${sms.channel.host}")
    private String host;

    @Value("${sms.channel.server}")
    private String server;

    @Value("${sms.channel.opName}")
    private String opName;

    @Value("${sms.channel.corpId}")
    private String corpId;

    @Value("${sms.channel.pwd}")
    private String pwd;

    @Override
    public String getBaseUrl() throws Exception {
        return getServer();
    }

    @Override
    public String getOpUrl() throws Exception {
        return getBaseUrl() + '/' + getOpName();
    }

}
