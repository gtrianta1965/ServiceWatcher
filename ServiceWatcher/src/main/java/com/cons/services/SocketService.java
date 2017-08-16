package com.cons.services;

import com.cons.utils.SWConstants;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class SocketService extends Service {
    final static Logger logger = Logger.getLogger(SocketService.class);
    
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
        logger.debug("Socket Service url mode is " + (urlMode==true?"Enabled":"Disabled"));
        String [] fullURL = serviceParameter.getUrl().split(":");
        String url = fullURL[0];
        String port;
        //If port defined set port = 80 (HTTP)
        try{
            port = fullURL[1];
        }catch (ArrayIndexOutOfBoundsException oofbex){
            logger.warn("No port specified using default port 80.");
            port = "80";
        }
        
        String ipAddr;
        //If URL based
        if(urlMode){
            //Get ip from URL
            logger.debug("Socket Service using url mode.");
            try{
                logger.debug("Socket Service resolving ip from url " + "http://" + url);
                ipAddr = (InetAddress.getByName(new URL("http://"+url).getHost())).getHostAddress();
            }catch (UnknownHostException uhex){
                // Catch and set call false and set error
                logger.error("Socket Service error failed to resolve " + "http://" + url);
                this.setSuccessfulCall(false);
                this.setErrorCall("Unknown Host");
            }catch (MalformedURLException murlex){
                // Catch and set call false and set error
                logger.error("Socket Service error bad url " + "http://" + url);
                this.setSuccessfulCall(false);
                this.setErrorCall("Bad URL");
            }catch (Exception ex){
                // Catch and set call false and set error
                logger.error("Socket Service error " + ex.getMessage());
                this.setSuccessfulCall(false);
                this.setErrorCall(ex.getMessage());
            }
        }
        
        try {
            logger.debug("Socket Service initializing new socket");
            // Try to open socket
            Socket soc = new Socket();
            // Try to connect if not throw IO
            try{
                logger.debug("Socket Service is trying to bind.");
                if(configuration != null){
                    logger.debug("Socket Service setting default die interval from configuration.");
                    soc.connect(new InetSocketAddress(url, Integer.parseInt(port)), configuration.getSocketDieInterval());
                }else{
                    logger.warn("Socket Service configuration was null setting default die interval to 5ms");
                    soc.connect(new InetSocketAddress(url, Integer.parseInt(port)), 5000);
                }
                logger.debug("Socket Service Bind was successful.");
                // If success set call true
                this.setSuccessfulCall(true);
            } catch (IOException ioex){
                // Catch and set call false and set error
                logger.error("Socket Service error " + SWConstants.SERVICE_SOCKET_UNREACHABLE_MSG + fullURL[0] +":"+ fullURL[1]);
                this.setSuccessfulCall(false);
                this.setErrorCall(SWConstants.SERVICE_SOCKET_UNREACHABLE_MSG + fullURL[0] +":"+ fullURL[1]);
            } catch (NumberFormatException nfex){
                logger.error("Socket Serice error number format error.");
                this.setSuccessfulCall(false);
                this.setErrorCall("Number format error");
            } catch (Exception ex){
                logger.error("Socket Service error " + ex.getMessage());
                this.setSuccessfulCall(false);
                this.setErrorCall(ex.getMessage());
            }finally {
                logger.debug("Closing socket.");
                soc.close();
            }
        } catch (Exception ex){
            logger.error("Socket Service error " + ex.getMessage());
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