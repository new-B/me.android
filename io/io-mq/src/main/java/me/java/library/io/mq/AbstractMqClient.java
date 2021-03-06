package me.java.library.io.mq;

import me.java.library.common.Disposable;
import me.java.library.mq.base.Factory;
import me.java.library.utils.base.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * File Name             :  AbstractMqClient
 *
 * @author :  sylar
 * Create                :  2019-10-23
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) allthings.vip  All Rights Reserved
 * *******************************************************************************************
 */
public abstract class AbstractMqClient implements Disposable {
    @Autowired
    protected Factory factory;

    protected abstract String getGroupId();

    protected String getClientId() {
        String hostName = NetworkUtils.getHostName();
        if (hostName != null) {
            return hostName;
        }

        String hostMac = NetworkUtils.getHostMac();
        if (hostMac != null) {
            return hostMac;
        }

        return UUID.randomUUID().toString();
    }
}
