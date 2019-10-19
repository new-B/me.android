package me.java.library.mq.ons;

import me.java.library.mq.base.IClient;

/**
 * File Name             :  IOnsClient
 * Author                :  sylar
 * Create Date           :  2018/4/11
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) xxx.com   All Rights Reserved
 * *******************************************************************************************
 */
public interface IOnsClient extends IClient {

    /**
     * AccessKey
     *
     * @return AccessKey
     */
    String getAccessKey();

    /**
     * SecretKey
     *
     * @return SecretKey
     */
    String getSecretKey();
}