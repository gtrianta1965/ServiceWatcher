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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println("command line is Running");
        System.out.println("-----Start execution dateinfo----- \n "+ dateFormat.format(date));
        serviceOrchestrator.start();
        while (serviceOrchestrator.isRunning()) {

        }
        Date date1 = new Date();
        System.out.println(serviceOrchestrator.getStatus().toString()+"\n -----dateinfo----- \n "+ dateFormat.format(date1)+"\n -----finished-----\n ");
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
