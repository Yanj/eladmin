package me.zhengjie.service;

import me.zhengjie.domain.vo.SmsVo;

/**
 * @author yanjun
 * @date 2021-01-11 13:36
 */
public interface SmsChannelService {

    /**
     * 发送短信
     *
     * @param smsVo .
     * @return .
     */
    String sendSms(SmsVo smsVo) throws Exception;

}
