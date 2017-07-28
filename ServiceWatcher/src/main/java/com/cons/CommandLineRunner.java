package com.cons;

import com.cons.services.ServiceOrchestrator;

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
    
    public void help(){
        String NoGUI ="(-nogui) : runs the Service watcher without GUI(user interface\n ";
        String AutoRefresh="(-autorefresh : X) : Starts the auto refresh. X sets the interval between the autorefreshes. \nIf you do not select an " +
            "interval number the programme starts with a default interval.\n" +
            "Also arguments -autorefresh must be followed by a colon (:) and the refreshTime value.\n";
        String Encrypt="(-encrypt) : Obfuscates the passwords of an unencrypted file\n";
        String conf="(-conf :) : loads a castum configuration file\n";
        System.out.println("\n\n\n"+NoGUI+"\n"+AutoRefresh+"\n"+Encrypt+"\n"+conf+"\n");
        
    }
    

    public void setServiceOrchestrator(ServiceOrchestrator serviceOrchestrator) {
        this.serviceOrchestrator = serviceOrchestrator;
    }

    public ServiceOrchestrator getServiceOrchestrator() {
        return serviceOrchestrator;
    }
}
