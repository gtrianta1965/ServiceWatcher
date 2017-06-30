package com.cons.services;


import com.cons.Configuration;
import com.cons.ServiceParameter;

import com.cons.utils.SWConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import java.util.List;

public class HTTPService extends Service {
    public HTTPService() {
        super();
    }
    
    public HTTPService(ServiceParameter sp) {
        super(sp);
    }

        
    @Override
    public void service() {
        String currentUrl = serviceParameter.getUrl();
        int responseCode=0;
        String responseMessage = null;
        boolean found = false;
        try {
            URL myPharmLink = new URL(currentUrl);
            HttpURLConnection huc =  (HttpURLConnection)myPharmLink.openConnection();
            huc.setRequestMethod("GET"); 
            huc.connect(); 
            responseCode = huc.getResponseCode();
            responseMessage = huc.getResponseMessage();
            
            BufferedReader in = new BufferedReader(
               new InputStreamReader(myPharmLink.openStream()));
            if (responseCode == 200){
                
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    if(serviceParameter.getSearchString()!=null){
                        if (inputLine.toLowerCase().contains(serviceParameter.getSearchString().toLowerCase())){
                        this.setSuccessfulCall(true);
                            found = true;
                        break;
                    }
                    }else{
                        this.setSuccessfulCall(true);
                        break;
                    }
                    
                }
                if (serviceParameter.getSearchString()!=null && found==false){//if no match found for getSearchString
                this.setSuccessfulCall(false);
                    this.setErrorCall(SWConstants.SEARCH_STRING_NOT_FOUND_MSG);
            }
            }else{               //if status is <>200
                this.setSuccessfulCall(false);
                this.setErrorCall(SWConstants.URL_RESPONSE_ERROR_MSG + responseMessage);
            }
            
            in.close();
            
        } catch (MalformedURLException e) {
            this.setSuccessfulCall(false);
            this.setErrorCall(SWConstants.MALLFORMED_URL_EXCEPTION_MSG + e.getMessage());
        }catch (ProtocolException e){
            this.setSuccessfulCall(false);
            this.setErrorCall(SWConstants.PROTOCOL_EXCEPTION_MSG + e.getMessage());
        }catch (Exception e){
            this.setSuccessfulCall(false);
            this.setErrorCall(SWConstants.GENERIC_EXCEPTION_MSG + e.getMessage());
        }        
    }
    
    
    public static void main(String args[]){
        
        HTTPService hs = new HTTPService();
        hs.run();
        
    }
    
    }
