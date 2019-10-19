package me.java.library.io.base.cmd;


import me.java.library.common.po.IBaseDbEnum;

import java.io.Serializable;

/**
 * Created by sylar on 16/5/25.
 */

/**
 * @author :  sylar
 * @FileName :  MqttConst
 * @CreateDate :  2017/11/08
 * @Description :
 * @ReviewedBy :
 * @ReviewedOn :
 * @VersionHistory :
 * @ModifiedBy :
 * @ModifiedDate :
 * @Comments :
 * @CopyRight : COPYRIGHT(c) iot.zs-inc.com All Rights Reserved
 * *******************************************************************************************
 */
public enum CmdType implements IBaseDbEnum, Serializable {

    //@formatter:off

    Normal      (0,"Normal"),
    Info        (1,"Info"),
    Data        (2,"Data"),
    Alarm       (3,"Alarm"),
    Event       (4,"Event"),
    Ota         (5,"Ota"),
    McuLog      (6,"McuLog"),
//    DasConnection(7,""),
//    DeviceConnection(8,""),
    //@formatter:on
    ;

    private final int value;
    private final String name;

    CmdType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CmdType valueOf(int value) {
        return IBaseDbEnum.fromValue(CmdType.class, value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return value;
    }
}