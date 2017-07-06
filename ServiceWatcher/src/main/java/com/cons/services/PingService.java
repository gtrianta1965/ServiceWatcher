package com.cons.services;

import com.cons.utils.SWConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.concurrent.Exchanger;

public class PingService extends Service {
    public PingService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    public PingService() {
        super();
    }

    @Override
    public void service() {        
        try {
            boolean result = false;
            //Get inet obj based on name (ip/url)
            InetAddress addr = InetAddress.getByName(serviceParameter.getUrl());
            //Ping with wait time PING_DIE_INTERVAL
            result = addr.isReachable(SWConstants.PING_DIE_INTERVAL);
            this.setSuccessfulCall(result);
            if(result == false){
                this.setErrorCall("Unreachable URL/PING.");
            }
        } catch (UnknownHostException uhe) {
            this.setSuccessfulCall(false);
            this.setErrorCall(uhe.getMessage());
            uhe.printStackTrace();
        } catch (Exception ex){
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
            ex.printStackTrace();
        }
    }
}