package com.cons.services;

import com.cons.ServiceParameter;
import com.cons.utils.SWConstants;

public abstract class Service implements Runnable {
    
    ServiceParameter serviceParameter = new ServiceParameter();
    ServiceOrchestrator serviceOrchestrator;
    private String errorCall;        //message for successful/unsuccessful call
    private boolean successfulCall;      //boolean for  for successful/unsuccessful call
    
    public Service(){   //default constructor
        super();
    }
    
    public Service(ServiceParameter sp){ 
        setServiceParamater(sp);
    }
    
    public void run() {
        printStatus(SWConstants.SERVICE_RUNNING);
        service();
        if (isSuccessfulCall()) {
            printStatus(SWConstants.SERVICE_SUCCESS);
        } else {
            printStatus(SWConstants.SERVICE_FAILED + ":" + getErrorCall());
        }
    }
    
    /*
     * Service is the main method that all services should override
     */
    public abstract void service();
       
    public void setServiceParamater(ServiceParameter serviceParameter) {
        this.serviceParameter = serviceParameter;
    }

    public ServiceParameter getServiceParameter() {
        return serviceParameter;
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
    

    public void setServiceOrchestrator(ServiceOrchestrator serviceOrchestrator) {
        this.serviceOrchestrator = serviceOrchestrator;
    }

    public ServiceOrchestrator getServiceOrchestrator() {
        return serviceOrchestrator;
    }
    
    protected void printStatus(String status) {
        this.serviceOrchestrator.printStatus(serviceParameter.getId() - 1, status);
    }
}
