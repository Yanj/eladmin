package me.zhengjie.httpclient;

import me.zhengjie.config.SmsConfig;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun
 * @date 2021-01-11 11:42
 */
@Component
public class SmsChannelHttpClient extends AbstractHttpClient {

    private SmsConfig config;

    public SmsChannelHttpClient(SmsConfig config, ConnectionManager connectionManager) {
        super(config, connectionManager);
        this.config = config;
    }

    /**
     * 发送短信
     *
     * @param mobiles
     * @param content
     * @return
     * @throws Exception
     */
    public String sendSms(String mobiles, String content) throws Exception {
        if (!config.isEnable()) {
            return "1";
        }
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("CorpID", config.getCorpId()));
        list.add(new BasicNameValuePair("Pwd", config.getPwd()));
        list.add(new BasicNameValuePair("Mobile", mobiles));
        list.add(new BasicNameValuePair("Content", content));
        HttpEntity entity = new UrlEncodedFormEntity(list, "gb2312");
        return doPost(config.getOpUrl(), entity, new BasicResponseHandler());
    }

}
