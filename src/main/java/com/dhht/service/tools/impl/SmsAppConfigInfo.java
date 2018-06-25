package com.dhht.service.tools.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 短信接口配置信息
 *
 * @author 赵兴龙
 * @date 2018.6.23
 */
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsAppConfigInfo {
    /**
     * 短信接口id
     */
    private int appId;
    /**
     * 短信接口key
     */
    private String appKey;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
