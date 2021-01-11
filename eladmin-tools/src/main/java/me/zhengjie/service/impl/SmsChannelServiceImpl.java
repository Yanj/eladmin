package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.SmsVo;
import me.zhengjie.httpclient.SmsChannelHttpClient;
import me.zhengjie.service.SmsChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author yanjun
 * @date 2021-01-11 13:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsChannelServiceImpl implements SmsChannelService {

    private final SmsChannelHttpClient httpClient;

    @Override
    public String sendSms(SmsVo smsVo) throws Exception {
        if (null == smsVo || null == smsVo.getMobiles() || StringUtils.isEmpty(smsVo.getContent())) {
            throw new IllegalArgumentException();
        }
        StringBuilder mobiles = new StringBuilder();
        for (String mobile : smsVo.getMobiles()) {
            if (mobile.length() != 0) {
                mobiles.append(",");
            }
            mobiles.append(mobile);
        }
        if (mobiles.length() == 0) {
            throw new IllegalArgumentException();
        }

        return httpClient.sendSms(mobiles.toString(), smsVo.getContent());
    }

}
