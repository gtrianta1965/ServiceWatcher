package com.cons.services;


import com.cons.Configuration;
import com.cons.ServiceParameter;

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
    public void run() {
        
        ServiceParameter sp = new ServiceParameter();
        service(sp);

    }
    
    

    @Override
    public void service(ServiceParameter sp) {
        String currentUrl = sp.getUrl();
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
                    if(sp.getSearchString()!=null){
                        if (inputLine.toLowerCase().contains(sp.getSearchString().toLowerCase())){
                            this.setSuccessfulCall(true);
                            found = true;
                            break;
                        }
                    }else{
                        this.setSuccessfulCall(true);
                        break;
                    }
                    
                }
                if (sp.getSearchString()!=null && found==false){//if no match found for getSearchString
                    this.setSuccessfulCall(false);
                    this.setErrorCall("Search String not found in response");
                }
            }else{               //if status is <>200
                this.setSuccessfulCall(false);
                this.setErrorCall("url with response message: "+responseMessage);
            }
            
            in.close();
            
        } catch (MalformedURLException e) {
            this.setSuccessfulCall(false);
            this.setErrorCall("url with response message: "+e.getMessage());
        }catch (ProtocolException e){
            this.setSuccessfulCall(false);
            this.setErrorCall("url with response message: "+e.getMessage());
        }catch (Exception e){
            this.setSuccessfulCall(false);
            this.setErrorCall("url with response message: "+e.getMessage());
        }        
    }
    
    
    public static void main(String args[]){
        
        HTTPService hs = new HTTPService();
        hs.run();
        
    }

}
