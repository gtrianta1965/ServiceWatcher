package com.cons;

import com.cons.services.ServiceOrchestrator;


public class CommandLineRunner extends Thread {
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
    
    public void autorefresh(){
        while(true){
            System.out.println("command line is auto-ref");
            serviceOrchestrator.start();
            System.out.println(serviceOrchestrator.getStatus().toString());
            try {
               sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setServiceOrchestrator(ServiceOrchestrator serviceOrchestrator) {
        this.serviceOrchestrator = serviceOrchestrator;
    }

    public ServiceOrchestrator getServiceOrchestrator() {
        return serviceOrchestrator;
    }
}
