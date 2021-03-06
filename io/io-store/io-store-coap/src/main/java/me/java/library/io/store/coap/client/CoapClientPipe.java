package me.java.library.io.store.coap.client;

import com.google.common.base.Preconditions;
import me.java.library.io.base.cmd.Cmd;
import me.java.library.io.base.pipe.BasePipe;
import me.java.library.io.store.coap.CoapRequestCmd;
import me.java.library.io.store.coap.CoapResponseCmd;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * File Name             :  CoapClientPipe
 *
 * @author :  sylar
 * Create                :  2020/7/17
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
public class CoapClientPipe extends BasePipe<CoapClientParams> {
    protected CoapClient client;

    public CoapClientPipe(CoapClientParams params) {
        super(params);
    }

    @Override
    protected boolean onStart() throws Exception {
        if (client == null) {
            client = new CoapClient();
        }
        return true;
    }

    @Override
    protected boolean onStop() throws Exception {
        if (client != null) {
            client.shutdown();
            client = null;
        }
        return true;
    }

    @Override
    protected boolean onSend(Cmd request) throws Exception {

        CoapHandler coapHandler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                onReceived(new CoapResponseCmd(response));
            }

            @Override
            public void onError() {
                onException(new TimeoutException());
            }
        };

        CoapRequestCmd req = (CoapRequestCmd) request;
        client.setURI(req.getUri());
        switch (req.getMethod()) {
            case GET:
                client.get(coapHandler);
                break;
            case PUT:
                client.put(coapHandler, req.getContent(), req.getFormat().getValue());
                break;
            case POST:
                client.post(coapHandler, req.getContent(), req.getFormat().getValue());
                break;
            case DELETE:
                client.delete(coapHandler);
                break;
            default:
                throw new IllegalArgumentException("invalid coap method");
        }
        return true;
    }

    @Override
    protected Cmd onSyncSend(Cmd request, long timeout, TimeUnit unit) throws Exception {
        CoapRequestCmd cmd = (CoapRequestCmd) request;
        client.setTimeout(TimeUnit.MILLISECONDS.convert(timeout, unit));
        client.setURI(cmd.getUri());

        CoapResponse response;
        switch (cmd.getMethod()) {
            case GET:
                response = client.get();
                break;
            case PUT:
                response = client.put(cmd.getContent(), cmd.getFormat().getValue());
                break;
            case POST:
                response = client.post(cmd.getContent(), cmd.getFormat().getValue());
                break;
            case DELETE:
                response = client.delete();
                break;
            default:
                throw new IllegalArgumentException("invalid coap method");
        }

        return new CoapResponseCmd(response);
    }

    @Override
    protected void checkOnSend(Cmd request) {
        super.checkOnSend(request);
        Preconditions.checkState(request instanceof CoapRequestCmd);

        CoapRequestCmd req = (CoapRequestCmd) request;
        Preconditions.checkNotNull(req.getUri());
        Preconditions.checkNotNull(req.getMethod());
    }
}
