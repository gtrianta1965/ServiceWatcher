package com.cons.services;

import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;
import com.cons.utils.SWConstants;


public abstract class Service implements Runnable {
    
    ServiceParameter serviceParameter = new ServiceParameter();
    ServiceOrchestrator serviceOrchestrator;
    Configuration configuration;
    private String errorCall;        //message for successful/unsuccessful call
    private boolean successfulCall;      //boolean for  for successful/unsuccessful call
    
    public Service(){   //default constructor
        super();
    }
    
    public Service(ServiceParameter sp){ 
        setServiceParameter(sp);
    }
    
    public void run() {
        printStatus(SWConstants.SERVICE_RUNNING);
        serviceParameter.setStatus(SWConstants.SERVICE_RUNNING);
        service();
        if (isSuccessfulCall()) {
            serviceParameter.setStatus(SWConstants.SERVICE_SUCCESS);
            printStatus(SWConstants.SERVICE_SUCCESS);
        } else {
            serviceParameter.setStatus(SWConstants.SERVICE_FAILED);
            serviceParameter.setError(getErrorCall());
            printStatus(SWConstants.SERVICE_FAILED + ":" + getErrorCall());
        }
    }
    
    /*
     * Service is the main method that all services should override
     */
    public abstract void service();
       
    public void setServiceParameter(ServiceParameter serviceParameter) {
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
        if (serviceOrchestrator != null ) {
           this.serviceOrchestrator.printStatus(serviceParameter.getId() - 1, status);
        }
        else {
            /* Uncomment for command line status
            System.out.println(status);
            */
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
