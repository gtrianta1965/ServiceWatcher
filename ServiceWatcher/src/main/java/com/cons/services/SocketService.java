package com.cons.services;

import com.cons.utils.SWConstants;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class SocketService extends Service {
    public SocketService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    public SocketService() {
        super();
    }

    @Override
    public void service() {
        //Check if socket is URL based
        boolean urlMode = getURLMode();
        // TODO change serviceParameter.getUrl(); to get .getIp();
        String ipAddr = serviceParameter.getUrl();
        //If URL based
        if(urlMode){
            //Get ip from URL
            try{
                ipAddr = (InetAddress.getByName(new URL(serviceParameter.getUrl()).getHost())).getHostAddress();
            }catch (UnknownHostException uhex){                
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall("Unknown Host");
            }catch (MalformedURLException murlex){
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall("Bad URL");
            }catch (Exception ex){
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall(ex.getMessage());
            }
        }
        
        try {
            // Try to open socket
            try(Socket soc = new Socket()){
                // TODO change constants to geters on commit
                // Try to connect if not throw IO
                soc.connect(new InetSocketAddress(ipAddr, SWConstants.PING_TARGET_PORT), SWConstants.PING_DIE_INTERVAL);
                // If success set call true
                this.setSuccessfulCall(true);
            } catch (IOException ioex){
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall(SWConstants.SERVICE_SOCKET_UNREACHABLE_MSG + ipAddr +":" + SWConstants.PING_TARGET_PORT);
            }
        } catch (Exception ex){
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        }
    }
    
    /**
    * Returns false if URL is not set or null else true
    */
    private boolean getURLMode(){
        if(serviceParameter.getUrl() != null){
            return true;
        }
        return false;
    }
}