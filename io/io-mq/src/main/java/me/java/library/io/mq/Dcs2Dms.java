package me.java.library.io.mq;

import me.java.library.io.base.cmd.Cmd;
import me.java.library.io.base.cmd.Host;
import me.java.library.io.base.cmd.Terminal;
import me.java.library.io.base.pipe.Pipe;
import me.java.library.io.base.pipe.PipeWatcher;

/**
 * File Name             :  Dcs2Dms
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
public class Dcs2Dms extends AbstractMqProducer {
    protected final Pipe pipe;

    public Dcs2Dms(Pipe pipe) {
        this.pipe = pipe;

        pipe.setWatcher(new PipeWatcher() {
            @Override
            public void onHostStateChanged(Host host, boolean isRunning) {

            }

            @Override
            public void onPipeRunningChanged(Pipe pipe, boolean isRunning) {

            }

            @Override
            public void onConnectionChanged(Pipe pipe, Terminal terminal, boolean isConnected) {

            }

            @Override
            public void onReceived(Pipe pipe, Cmd cmd) {

            }

            @Override
            public void onException(Pipe pipe, Throwable t) {

            }
        });
    }

    @Override
    protected String getGroupId() {
        return String.join("-", MqGroupPrefix.GROUP_DCS_TO_DMS, this.getClass().getCanonicalName());
    }

    @Override
    protected String getTopic() {
        return MqTopic.DCS_TO_DMS;
    }
}
