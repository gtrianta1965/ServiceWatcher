package com.cons.services;

import com.cons.utils.SWConstants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketService extends Service {
    public SocketService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    public SocketService() {
        super();
    }

    @Override
    public void service() {
        try {
            // Try to open socket
            try(Socket soc = new Socket()){
                // TODO change constants to geters on commit
                // Try to connect if not throw IO
                soc.connect(new InetSocketAddress(serviceParameter.getUrl(), SWConstants.PING_TARGET_PORT), SWConstants.PING_DIE_INTERVAL);
                // If success set call true
                this.setSuccessfulCall(true);
            } catch (IOException ioex){
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall("Remote is unreachable.");
            }
        } catch (Exception ex){
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        }
    }
}