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
    
    private OrchestratorStatus orchestratorStatus;
    
    public ServiceOrchestrator() {
        super();
        orchestratorStatus = new OrchestratorStatus();
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
    
    public void setOrchestratorStatus(OrchestratorStatus orchestratorStatus) {
        this.orchestratorStatus = orchestratorStatus;
    }

    public OrchestratorStatus getOrchestratorStatus() {
        return orchestratorStatus;
    }
    
    
    public void start() {
        //Check if we are running
        if (executor != null && !executor.isTerminated()) {
            System.out.println("Executor is running");
            return;
        }
        int totalSub = 0;
        //Start Thread Pooling with services
        //TODO:Use configuration parameter for pooling
        executor = Executors.newFixedThreadPool(configuration.getConcurrentThreads());
        for (int i = 0; i < configuration.getServiceParameters().size() ; i++) {
            configuration.getServiceParameters().get(i).setStatus(SWConstants.SERVICE_SUBMITTED);
            
            totalSub = orchestratorStatus.getTotalSubmitted();
            orchestratorStatus.setTotalSubmitted(++totalSub);
            
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

    
    public Boolean isRunning(){
        Boolean running;
        if (executor != null && !executor.isTerminated()){
           running = true;
        }else {
            running = false;
        }
        return running;
    }
    
    /*check how many of services are submitted, running, successful or failed*/
    public OrchestratorStatus getStatus(){
        /*clear orchestratorStatus obj except of totalSubmitted*/
        int submitted = orchestratorStatus.getTotalSubmitted();
        orchestratorStatus.reset();
        orchestratorStatus.setTotalSubmitted(submitted);
        
        List<ServiceParameter> lsp = configuration.getServiceParameters();
        orchestratorStatus.setTotalServices(lsp.size());
        int getValue = 0;
        for(ServiceParameter s :lsp){
            if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_RUNNING)){
                getValue = orchestratorStatus.getTotalRunning();
                orchestratorStatus.setTotalRunning(++getValue);
            }else if(s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_SUCCESS)){
                getValue = orchestratorStatus.getTotalSuccess();
                orchestratorStatus.setTotalSuccess(++getValue);
            }else if(s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_FAILED)){
                getValue = orchestratorStatus.getTotalFailed();
                orchestratorStatus.setTotalFailed(++getValue);
            }   
        }
        
        return orchestratorStatus;
    }

    
}
