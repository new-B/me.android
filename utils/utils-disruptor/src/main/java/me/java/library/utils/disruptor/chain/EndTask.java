package me.java.library.utils.disruptor.chain;

import com.lmax.disruptor.EventHandler;

/**
 * File Name             :  EndTask
 *
 * @author :  sylar
 * Create :  2019-10-20
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
public final class EndTask implements EventHandler<ChainContext> {

    protected ChainContainer container;

    public EndTask(ChainContainer container) {
        this.container = container;
    }

    @Override
    public void onEvent(ChainContext event, long sequence, boolean endOfBatch) throws Exception {
        try {
            container.onChainFinished(event);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.clear();
        }
    }
}
