package com.cons;

import com.cons.services.ServiceOrchestrator;


public class CommandLineRunner {
    private ServiceOrchestrator serviceOrchestrator;
    public CommandLineRunner() {
        super();
    }
    
    public void run(){
        System.out.println("command line is Running");
        
        serviceOrchestrator.start();
        while(serviceOrchestrator.isRunning()){
            
            
            
        }
        System.out.println(serviceOrchestrator.getStatus().toString());
        //TODO: Send mail.
    }

    public void setServiceOrchestrator(ServiceOrchestrator serviceOrchestrator) {
        this.serviceOrchestrator = serviceOrchestrator;
    }

    public ServiceOrchestrator getServiceOrchestrator() {
        return serviceOrchestrator;
    }
}
