package com.cons;

import com.cons.services.ServiceOrchestrator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


public class CommandLineRunner extends Thread {
    private ServiceOrchestrator serviceOrchestrator;
    private long autoRefresh;
    
    public CommandLineRunner(long autoRefresh) {
        this.autoRefresh = autoRefresh;    
    }
    
    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("command line is Running");
        serviceOrchestrator.start();
        System.out.println(dateFormat.format (new Date())+" : " +"RUNNING");
        while (serviceOrchestrator.isRunning()) {

        }
        System.out.println(dateFormat.format (new Date())+" : "+serviceOrchestrator.getStatus().toString());
        //TODO: Send mail.
    }

    public void autorefresh() {
        while (true) {
            System.out.println("command line is auto-ref");
            run();
            try {
                sleep(autoRefresh * 60000);
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
