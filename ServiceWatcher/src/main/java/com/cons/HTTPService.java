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
        System.out.println("Start Thread:" + Thread.currentThread().getId());
        try {
            Thread.sleep((long) (1000 + (Math.random() * 5000)));
        } catch (InterruptedException e) {
        }
        System.out.println("Stop thread:" + Thread.currentThread().getId());
    }

    @Override
    public void service(ServiceParameter sp) {
        System.out.println("bbb");
        
    }
    
    public int getResponseCodeFromUrl(String url){
        int responseCode=0;
        return responseCode;
    }
    
    public static void main(String args[]){
        HTTPService hp = new HTTPService();
        hp.run();
    }
}
