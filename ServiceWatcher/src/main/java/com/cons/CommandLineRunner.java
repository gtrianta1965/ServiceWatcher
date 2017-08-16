package com.cons;

import com.cons.services.ServiceOrchestrator;
import com.cons.utils.SWConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;


public class CommandLineRunner extends Thread {
    private ServiceOrchestrator serviceOrchestrator;
    private long autoRefresh;

    public CommandLineRunner() {
        this(0);
    }

    public CommandLineRunner(long autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    @Override
    public void run() {
        if (this.autoRefresh == 0) {
            runOnce();
        } else {
            runPeriodically();
        }
    }

    private void runOnce() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(dateFormat.format(new Date()) + ": " + SWConstants.RUNNING_STATUS);
        serviceOrchestrator.start();
        while (serviceOrchestrator.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        System.out.println(dateFormat.format(new Date()) + " : " + serviceOrchestrator.getStatus().toString());
    }

    private void runPeriodically() {
        while (true) {
            runOnce();
            try {
                sleep(autoRefresh * 60000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("Interrupt requested.");
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
