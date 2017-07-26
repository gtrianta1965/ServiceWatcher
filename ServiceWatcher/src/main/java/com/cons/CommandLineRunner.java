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
        Date date = new Date();
        System.out.println("command line is Running");
        serviceOrchestrator.start();
        System.out.println("-----*Start execution*----- \n "+" Before execution Date-info: "+ dateFormat.format(date)+"\n"+serviceOrchestrator.getStatus().toString()+"\n");
        while (serviceOrchestrator.isRunning()) {

        }
        Date date1 = new Date();
        System.out.println(" After execution Date-info: "+ dateFormat.format(date1)+"\n"+serviceOrchestrator.getStatus().toString()+"\n -----*finished*-----\n\n");
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
