package com.cons.services;

import com.cons.ServiceParameter;

public abstract class Service implements Runnable {
    
    ServiceParameter currentServParam = new ServiceParameter();
    private String errorCall;        //message for successful/unsuccessful call
    private boolean successfulCall;      //boolean for  for successful/unsuccessful call
    
    public Service(){   //default constructor
        this.currentServParam.setUrl(" ");
        this.currentServParam.setDescription(" ");
        this.currentServParam.setType(" ");
        this.currentServParam.setGroup(" ");
        this.currentServParam.setSearchString(" ");
    }
    
    public Service(ServiceParameter sp){ 
        
        this.currentServParam.setUrl(sp.getUrl());
        this.currentServParam.setDescription(sp.getDescription());
        this.currentServParam.setType(sp.getType());
        this.currentServParam.setGroup(sp.getGroup());
        this.currentServParam.setSearchString(sp.getSearchString());
        //service(sp);
    }
    
    
    public abstract void run();
    
    public abstract void service(ServiceParameter sp);
       
    public void setCurrentServParam(ServiceParameter currentServParam) {
        this.currentServParam = currentServParam;
    }

    public ServiceParameter getCurrentServParam() {
        return currentServParam;
    }

    public void setSuccessfulCall(boolean successfulCall) {
        this.successfulCall = successfulCall;
    }

    public boolean isSuccessfulCall() {
        return successfulCall;
    }

    public void setErrorCall(String errorCall) {
        this.errorCall = errorCall;
    }

    public String getErrorCall() {
        return errorCall;
    }
}