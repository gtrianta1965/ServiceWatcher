package com.cons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;

import java.util.List;

public class HTTPService implements Service {
    public HTTPService() {
        super();
    }

    /*run method reads the ServiceParameter list with the obj */
    @Override
    public void run() {
        Configuration conf = new Configuration();
        List<ServiceParameter> serviceParamObj = conf.getServiceParameters();
        for(ServiceParameter s : serviceParamObj){
            service(s);
        }
        
        /*ServiceParameter sp = new ServiceParameter();
        sp.setUrl("http://192.168.42.63:7003/test-sso/faces/Logn");
        service(sp);*/
    }

    @Override
    public void service(ServiceParameter sp) {
        System.out.println("bbb");
            String currentUrl = sp.getUrl();
            int responseFromUrl = getResponseCodeFromUrl(currentUrl);
            System.out.println(responseFromUrl);
            
            if (responseFromUrl ==-1){
                System.out.println("bad syntax");
            }else if(responseFromUrl == 200){
                System.out.println("url is ok 	HTTP_OK");
            }else if(responseFromUrl ==404 ){
                System.out.println("url HTTP_NOT_FOUND");
            }else if(responseFromUrl ==204 ){
                System.out.println("url HTTP_NO_CONTENT");
            }else if(responseFromUrl ==500 ){
                System.out.println("url HTTP_INTERNAL_ERROR");
            }else if(responseFromUrl == -3){
                System.out.println("connection lost");
            }
    }
    
    public int getResponseCodeFromUrl(String url){
        int responseCode=0;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.19.54", 8080));
            URL myPharmLink = new URL(url);
            HttpURLConnection huc =  (HttpURLConnection)myPharmLink.openConnection(proxy);
            huc.setRequestMethod("GET"); 
            huc.connect(); 
            responseCode = huc.getResponseCode();
            System.out.println("its ok");
            
        } catch (MalformedURLException e) {
            System.out.println("bad syntax error");
            responseCode = -1;
            
        }catch (ProtocolException e){
            System.out.println(e.toString());
            System.out.println("exception protocol");
            responseCode = -2;
        }catch (Exception e){
            String str = "502 Proxy Error";
            if (e.toString().toLowerCase().contains(str.toLowerCase())){
                System.out.println(str);
                responseCode = -1;
            }else{
                System.out.println(e.toString());
                System.out.println("exception connection Lost");
                responseCode = -3;
            }
        
        }
        return responseCode;
    }
    
    public static void main(String args[]){
        HTTPService hp = new HTTPService();
        hp.run();
    }
}
