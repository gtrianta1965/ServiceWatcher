package com.cons.services;

import com.cons.Configuration;
import com.cons.ui.ServicesTableModel;

import com.cons.utils.SWConstants;

import java.io.File;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceOrchestrator {
    private Configuration configuration;
    private ServicesTableModel serviceTableModel;
    private ExecutorService executor;
    private Integer numberOfFinished;
    private Integer totalOfServices;
    private Integer totalOfRunning;
    private Integer totalOfSubmitted;
    private Integer totalOfSuccess;
    private Integer totalOfFailed;
    
    
    public ServiceOrchestrator() {
        super();
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
    
    public void setTotalOfServices(Integer totalOfServices) {
        this.totalOfServices = totalOfServices;
    }

    public Integer getTotalOfServices() {
        return totalOfServices;
    }

    public void setTotalOfRunning(Integer totalOfRunning) {
        this.totalOfRunning = totalOfRunning;
    }

    public Integer getTotalOfRunning() {
        return totalOfRunning;
    }

    public void setTotalOfSubmitted(Integer totalOfSubmitted) {
        this.totalOfSubmitted = totalOfSubmitted;
    }

    public Integer getTotalOfSubmitted() {
        return totalOfSubmitted;
    }

    public void setTotalOfSuccess(Integer totalOfSuccess) {
        this.totalOfSuccess = totalOfSuccess;
    }

    public Integer getTotalOfSuccess() {
        return totalOfSuccess;
    }

    public void setTotalOfFailed(Integer totalOfFailed) {
        this.totalOfFailed = totalOfFailed;
    }

    public Integer getTotalOfFailed() {
        return totalOfFailed;
    }   
    
    
    public void start() {
        //Check if we are running
        if (executor != null && !executor.isTerminated()) {
            System.out.println("Executor is running");
            return;
        }
        numberOfFinished = 0;
        //Start Thread Pooling with services
        //TODO:Use configuration parameter for pooling
        executor = Executors.newFixedThreadPool(configuration.getConcurrentThreads());
        for (int i = 0; i < configuration.getServiceParameters().size() ; i++) {
            configuration.getServiceParameters().get(i).setStatus(SWConstants.SERVICE_SUBMITTED);
            numberOfFinished++;
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

    public ExecutorService getExecutor() {
        return executor;
    }
    
    public void loadNewFile(File f){
        
        configuration.init(f.getName());
        serviceTableModel.initFromConfiguration(configuration);
        setServiceTableModel(serviceTableModel);
        setConfiguration(configuration);
        
    }

    public void setNumberOfFinished(Integer numberOfFinished) {
        this.numberOfFinished = numberOfFinished;
    }

    public Integer getNumberOfFinished() {
        return numberOfFinished;
    }

    
    public Boolean isRunning(){
        Boolean flag;
        if (executor != null && !executor.isTerminated()){
           flag = true;
        }else {
            flag = false;
        }
        return flag;
    }
    
    /*check how many of services are submitted, running, successful or failed*/
    public void getStatus(List<ServiceParameter> lsp){
        totalOfServices = lsp.size();
        for(ServiceParameter s :lsp){
            if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_SUBMITTED)){
                totalOfSubmitted++;
            }else if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_RUNNING)){
                totalOfRunning++;
            }else if(s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_SUCCESS)){
                totalOfSuccess++;
            }else if(s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_FAILED)){
                totalOfFailed++;
            }   
        }
    }

}
