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
        String [] fullURL = serviceParameter.getUrl().split(":");
        String url = fullURL[0];
        String port;
        //If port defined set port = 80 (HTTP)
        try{
            port = fullURL[1];
        }catch (ArrayIndexOutOfBoundsException oofbex){
            port = "80";
        }
        
        String ipAddr;
        //If URL based
        if(urlMode){
            //Get ip from URL
            try{
                ipAddr = (InetAddress.getByName(new URL("http://"+url).getHost())).getHostAddress();
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
            Socket soc = new Socket();
            // Try to connect if not throw IO
            try{
                if(configuration != null){
                    soc.connect(new InetSocketAddress(url, Integer.parseInt(port)), configuration.getSocketDieInterval());
                }else{
                    soc.connect(new InetSocketAddress(url, Integer.parseInt(port)), 5000);
                }
                // If success set call true
                this.setSuccessfulCall(true);
            } catch (IOException ioex){
                // Catch and set call false and set error
                this.setSuccessfulCall(false);
                this.setErrorCall(SWConstants.SERVICE_SOCKET_UNREACHABLE_MSG + fullURL[0] +":"+ fullURL[1]);
            } catch (NumberFormatException nfex){
                this.setSuccessfulCall(false);
                this.setErrorCall("Number format error");
            } catch (Exception ex){
                this.setSuccessfulCall(false);
                this.setErrorCall(ex.getMessage());
            }finally {
                soc.close();
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