package com.cons.services;

import com.cons.Configuration;
import com.cons.ui.ServicesTableModel;

import com.cons.utils.Reporter;
import com.cons.utils.SWConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceOrchestrator {
    protected List<String> statusLog;
    private Configuration configuration;
    private ServicesTableModel serviceTableModel;
    private ExecutorService executor;
    private boolean firstTime = true;
    
    public ServiceOrchestrator() {
        super();
        this.statusLog = new ArrayList<String>();
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setServiceTableModel(ServicesTableModel serviceTableModel) {
        this.serviceTableModel = serviceTableModel;
    }

    public ServicesTableModel getServiceTableModel() {
        return serviceTableModel;
    }
    
    public void printStatus(int row, String status) {
        serviceTableModel.setStatus(row, status);
    }
    
    public void start() {
        
        //Check if we are running
        if (executor != null && !executor.isTerminated()) {
            System.out.println("Executor is running");
            return;
        }else if(!firstTime){
            sendStatusLog();
            this.statusLog = new ArrayList<String>();
        }
        firstTime = false;
        //Start Thread Pooling with services
        //TODO:Use configuration parameter for pooling
        executor = Executors.newFixedThreadPool(configuration.getConcurrentThreads());
        
        for (int i = 0; i < configuration.getServiceParameters().size() ; i++) {
            serviceTableModel.setStatus(i, SWConstants.SERVICE_SUBMITTED);
            Runnable serviceWorker = ServiceFactory.createService(configuration.getServiceParameters().get(i),configuration);
            ((Service)serviceWorker).setServiceOrchestrator(this);
            executor.execute(serviceWorker);
        }
        
        executor.shutdown();
        /* Study the following code and activate it when it is needed
        while (!executor.isTerminated()) {
        }
        
        System.out.println("Finished all threads");
        */
        
    }
    
    
    public void sendStatusLog(){
        Reporter.sendMail(new String[]{"alexkalavitis@gmail.com"}, this.statusLog);
    }
    
    public ExecutorService getExecutor() {
        return executor;
    }
}
