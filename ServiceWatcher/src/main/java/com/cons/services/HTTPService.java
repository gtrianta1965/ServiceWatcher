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
        init(sp);
        service(sp);

    }
    
    

    @Override
    public void service(ServiceParameter sp) {
        String currentUrl = sp.getUrl();
        int responseCode=0;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.getSocketAddress(), this.getSocketPort()));
            URL myPharmLink = new URL(currentUrl);
            HttpURLConnection huc =  (HttpURLConnection)myPharmLink.openConnection(proxy);
            huc.setRequestMethod("GET"); 
            huc.connect(); 
            responseCode = huc.getResponseCode();
            
            BufferedReader in = new BufferedReader(
               new InputStreamReader(myPharmLink.openStream()));
            
            if (responseCode == 200){
                
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    System.out.println(inputLine);
                    if (inputLine.toLowerCase().contains(sp.getSearchString().toLowerCase())){
                        System.out.println("find it");
                        this.setSuccessfulCall(true);
                        System.out.println("setting success status "+ this.isSuccessfulCall());
                        break;
                    }
                    
                    System.out.println(this.isSuccessfulCall());
                    
                }
            }else{
                System.out.println("set error msg");
                this.setSuccessfulCall(false);
                this.setErrorCall("bad url");
            }
            
            in.close();
            
        } catch (MalformedURLException e) {
            this.setSuccessfulCall(false);
            this.setErrorCall("bad url");
            System.out.println(this.getErrorCall());
            
        }catch (ProtocolException e){
            this.setSuccessfulCall(false);
            this.setErrorCall("bad url");
            System.out.println(this.getErrorCall());
        }catch (Exception e){
            this.setSuccessfulCall(false);
            this.setErrorCall("bad url");
            System.out.println(this.getErrorCall());
        }        
    }
    
    
    public static void main(String args[]){
        
        HTTPService hs = new HTTPService();
        hs.run();
        
    }
    
    public void init(ServiceParameter sp){ //method for initialise tempor for test
        sp.setUrl("https://www.google.gr");//192.168.42.63:7003/test-sso/faces/Login https://www.google.gr
        sp.setDescription("test");
        sp.setGroup("test2");
        sp.setType("test3");
        sp.setSearchString("Αναζήτηση");
        this.setSocketAddress("192.168.19.54");
        this.setSocketPort(8080);
    }
    
    public ServiceParameter init(String url){ //method for junit test initialization
        ServiceParameter sp = new ServiceParameter();
        sp.setUrl(url);//192.168.42.63:7003/test-sso/faces/Login https://www.google.gr
        sp.setDescription("test");
        sp.setGroup("test2");
        sp.setType("test3");
        sp.setSearchString("Αναζήτηση");
        this.setSocketAddress("192.168.19.54");
        this.setSocketPort(8080);
        return sp;
    }

}
