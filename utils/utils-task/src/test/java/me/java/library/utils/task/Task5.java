package me.java.library.utils.task;

/**
 * File Name             :  Task2
 *
 * @author :  sylar
 * Create                :  2019-11-22
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
public class Task5 extends Task1 {
    public Task5(TaskContext<Foo> context) {
        super(context);
    }

    @Override
    protected Foo call(Object... args) throws Throwable {
//        return super.call(args);
        throw new Throwable("任务5 发生异常");
    }
}
