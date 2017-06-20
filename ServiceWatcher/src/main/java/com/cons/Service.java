package com.cons;

public interface Service extends Runnable {
    
    public void run();
    
    public void service(ServiceParameter sp);
    
    public int getResponseCodeFromUrl(String url);
}
