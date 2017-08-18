package com.cons.services;

import com.cons.Configuration;
import com.cons.utils.SWConstants;

public abstract class Service implements Runnable {

    ServiceParameter serviceParameter = new ServiceParameter();
    ServiceOrchestrator serviceOrchestrator;
    Configuration configuration;
    private String errorCall; //message for successful/unsuccessful call
    private String successCall; //message for successful/unsuccessful call
    private boolean successfulCall; //boolean for  for successful/unsuccessful call

    public Service() { //default constructor
        super();
    }

    public Service(ServiceParameter sp) {
        setServiceParameter(sp);
    }


    public void run() {
        printStatus(SWConstants.SERVICE_RUNNING);
        serviceParameter.setStatus(SWConstants.SERVICE_RUNNING);
        serviceParameter.setActualRetries(0); //Reset the actual retries


        while (serviceParameter.getActualRetries() <= serviceParameter.getRetries()) {
            service();
            if (serviceParameter.getActualRetries() == serviceParameter.getRetries()) {
                break; //We reached the maximum number of retries. Stop trying.
            } else {
                //Give it another try if it is still  Unsuccesfull
                if (!isSuccessfulCall()) {
                    serviceParameter.setActualRetries(serviceParameter.getActualRetries() + 1);
                    printStatus(SWConstants.SERVICE_RUNNING); // Refresh the retry counter
                } else {
                    break; //Again step out. Service succeeded.
                }
            }

        }

        if (isSuccessfulCall()) {
            serviceParameter.setStatus(SWConstants.SERVICE_SUCCESS);

            if (getSuccessCall() != null) {
                printStatus(SWConstants.SERVICE_SUCCESS);

            } else {
                printStatus(SWConstants.SERVICE_SUCCESS);
            }

            if (serviceOrchestrator != null) {
                serviceOrchestrator.statusLog.add("Service: " + serviceParameter.getDescription() + " is UP");
            }
        } else {
            serviceParameter.setStatus(SWConstants.SERVICE_FAILED);
            serviceParameter.setError(getErrorCall());
            printStatus(SWConstants.SERVICE_FAILED);
            if (serviceOrchestrator != null) {
                serviceOrchestrator.statusLog.add("Service: " + serviceParameter.getDescription() + " is DOWN (" +
                                                  serviceParameter.getError() + ")");
            }
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
        serviceParameter.setContext(errorCall);
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
        if (serviceOrchestrator != null) {
            this.serviceOrchestrator.printStatus(serviceParameter.getId() - 1, status);
        } else {
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

    public void setSuccessCall(String successCall) {
        this.successCall = successCall;
        serviceParameter.setContext(successCall);
        
    }

    public String getSuccessCall() {
        return successCall;
    }
}
