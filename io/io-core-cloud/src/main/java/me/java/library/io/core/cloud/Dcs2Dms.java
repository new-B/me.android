package me.java.library.io.core.cloud;

import me.java.library.io.base.Cmd;
import me.java.library.io.base.Host;
import me.java.library.io.base.Terminal;
import me.java.library.io.core.pipe.Pipe;
import me.java.library.io.core.pipe.PipeWatcher;

/**
 * File Name             :  Dcs2Dms
 *
 * @Author :  sylar
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
public class Dcs2Dms extends AbstractMqProducer {
    protected final Pipe pipe;

    public Dcs2Dms(String brokers, Pipe pipe) {
        super(brokers);
        this.pipe = pipe;

        pipe.setWatcher(new PipeWatcher() {
            @Override
            public void onReceived(Pipe pipe, Cmd cmd) {

            }

            @Override
            public void onConnectionChanged(Pipe pipe, Terminal terminal, boolean isConnected) {

            }

            @Override
            public void onHostStateChanged(Host host, boolean isRunning) {

            }
        });
    }

    @Override
    protected String getGroupId() {
        return String.join("-", MqConstants.GROUP_DCS_TO_DMS, this.getClass().getCanonicalName());
    }

    @Override
    protected String getTopic() {
        return MqConstants.TOPIC_DCS_TO_DMS;
    }
}