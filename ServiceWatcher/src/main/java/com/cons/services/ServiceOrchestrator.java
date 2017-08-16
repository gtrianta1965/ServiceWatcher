package com.cons.services;

import com.cons.Configuration;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.Reporter;
import com.cons.utils.SWConstants;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceOrchestrator {
    private Configuration configuration;
    private ServicesTableModel serviceTableModel;
    private ExecutorService executor;
    private Reporter reporter;
    private OrchestratorStatus orchestratorStatus;
    
    public ServiceOrchestrator() {
        super();
        orchestratorStatus = new OrchestratorStatus();
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if(this.configuration.getSendMailUpdates()){
            this.startReporter();
        }
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

        //Reset all counters
        orchestratorStatus.reset();

        //Start Thread Pooling with services
        int totalSub = 0;
        //TODO:Use configuration parameter for pooling
        executor = Executors.newFixedThreadPool(configuration.getConcurrentThreads());
        for (int i=0; i<configuration.getServiceParameters().size() ;i++){
            totalSub = orchestratorStatus.getTotalSubmitted();
            orchestratorStatus.setTotalSubmitted(++totalSub);
        
            //Set the status submitted for both table model and serviceParameters array of ServiceParameter
            serviceTableModel.setStatus(i, SWConstants.SERVICE_SUBMITTED);
            configuration.getServiceParameters()
                         .get(i)
                         .setStatus(SWConstants.SERVICE_SUBMITTED); //Fix bug with unxplainable behavior of status counters

            Runnable serviceWorker =
                ServiceFactory.createService(configuration.getServiceParameters().get(i), configuration);
            ((Service)serviceWorker).setServiceOrchestrator(this);
            executor.execute(serviceWorker);
        }
        
        executor.shutdown();
        
        new Thread(new Runnable() {
            public void run() {
                while (isRunning()) {
                }
                checkSendMail();
            }
        }).start();
        
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
                orchestratorStatus.setTotalFailed(getValue + 1);
            }   
        }
        
        return orchestratorStatus;
    }

    /**
     * Checks if it should send emails for the current run.
     */
    public void checkSendMail(){
        if(this.configuration.getSendMailUpdates() && 
           (this.configuration.getSmtpSendEmailOnSuccess() || 
            this.orchestratorStatus.getTotalSubmitted()!=this.orchestratorStatus.getTotalSuccess())){
            reporter.send();
        }
    }
    
    public void startReporter(){
        this.reporter = new Reporter(this);
    }
}
