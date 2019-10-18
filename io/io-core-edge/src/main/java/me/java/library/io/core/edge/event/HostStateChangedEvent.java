package me.java.library.io.core.edge.event;

import me.java.library.io.base.Host;
import me.java.library.utils.event.AbstractEvent;

/**
 * File Name             :  HostStateChangedEvent
 *
 * @Author :  sylar
 * @Create :  2019-10-17
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) me.iot.com   All Rights Reserved
 * *******************************************************************************************
 */
public class HostStateChangedEvent extends AbstractEvent<Host, Boolean> {

    public HostStateChangedEvent(Host host, Boolean isRunning) {
        super(host, isRunning);
    }
}
