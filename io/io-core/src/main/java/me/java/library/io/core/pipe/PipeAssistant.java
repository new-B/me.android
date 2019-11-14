package me.java.library.io.core.pipe;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.handler.timeout.IdleStateEvent;
import me.java.library.io.Cmd;
import me.java.library.io.Terminal;
import me.java.library.io.core.utils.ChannelAttr;

import java.util.Map;

/**
 * File Name             :  PipeAssistant
 *
 * @author :  sylar
 * Create                :  2019-11-14
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
public class PipeAssistant {

    synchronized public static PipeAssistant getInstance() {
        return new PipeAssistant();
    }

    private Map<Pipe, PipeContext> pipeAssistants;

    private PipeAssistant() {
        pipeAssistants = Maps.newConcurrentMap();
    }

    public void addPipe(Pipe pipe) {
        pipeAssistants.put(pipe, new PipeContext(pipe));
    }

    public void remove(Pipe pipe) {
        pipeAssistants.remove(pipe);
    }

    public PipeContext getPipeContext(Pipe pipe) {
        return pipeAssistants.get(pipe);
    }

    public PipeContext getPipeContext(Channel channel) {
        return pipeAssistants.get(getPipe(channel));
    }

    public Pipe getPipe(Channel channel) {
        return ChannelAttr.get(channel, ChannelAttr.ATTR_PIPE);
    }

    public Channel getChannel(AbstractPipe pipe, Terminal terminal) {
        Channel channel = getPipeContext(pipe).getTerminalState(terminal).getChannel();
        if (channel == null) {
            //非伺服器模式时
            return pipe.channel;
        } else {
            return channel;
        }
    }

    public void onThrowable(Channel channel, Throwable throwable) {
        AbstractPipe pipe = getBasePipe(channel);
        pipe.onException(throwable);
    }

    public void onReceived(Channel channel, Cmd cmd) {
        AbstractPipe pipe = getBasePipe(channel);
        getPipeContext(channel).getTerminals(channel).add(cmd.getFrom());
        onConnectionChanged(channel, cmd.getFrom(), true);

        pipe.onReceived(cmd);
    }

    public void onChannelIdle(Channel channel, IdleStateEvent idleStateEvent) {
        getPipeContext(channel).getTerminals(channel).forEach(terminal -> {
            getPipeContext(channel).getTerminals(channel).remove(terminal);
            onConnectionChanged(channel, terminal, false);
        });
    }

    public void onConnectionChanged(Channel channel, Terminal terminal, boolean connected) {
        TerminalState state = getPipeContext(channel).getTerminalState(terminal);
        if(state.isConnected() != connected){
            state.setConnected(connected);
            state.setChannel(connected ? channel : null);

            AbstractPipe pipe = getBasePipe(channel);
            pipe.onConnectionChanged(terminal, connected);
        }
    }

    private AbstractPipe getBasePipe(Channel channel) {
        Pipe pipe = ChannelAttr.get(channel, ChannelAttr.ATTR_PIPE);
        if (pipe instanceof AbstractPipe) {
            return (AbstractPipe) pipe;
        }
        throw new RuntimeException("不支持的pipe实现，pipe 须派生自 AbstractPipe");
    }

}