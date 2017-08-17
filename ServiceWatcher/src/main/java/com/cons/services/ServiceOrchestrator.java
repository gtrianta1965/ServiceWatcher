package com.cons.services;

import com.cons.Configuration;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.Reporter;
import com.cons.utils.SWConstants;

import java.io.File;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class ServiceOrchestrator {
    static final Logger logger = Logger.getLogger(ServiceOrchestrator.class);
    
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
        logger.debug("Begin");
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
        for (int i = 0; i < configuration.getServiceParameters().size(); i++) {
            totalSub = orchestratorStatus.getTotalSubmitted();
            orchestratorStatus.setTotalSubmitted(++totalSub);

            //Set the status submitted for both table model and serviceParameters array of ServiceParameter
            serviceTableModel.setStatus(i, SWConstants.SERVICE_SUBMITTED);
            configuration.getServiceParameters()
                         .get(i)
                         .setStatus(SWConstants.SERVICE_SUBMITTED); //Fix bug with unxplainable behavior of status counters

            Runnable serviceWorker =
                ServiceFactory.createService(configuration.getServiceParameters().get(i), configuration);
            ((Service) serviceWorker).setServiceOrchestrator(this);
            executor.execute(serviceWorker);
        }

        logger.debug(configuration.getServiceParameters().size() + " submitted to thread pool.");

        executor.shutdown();
        logger.debug("Executor shutdown.");
        logger.debug("Starting send mail thread.");
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

    public void loadNewFile(File f) {

        configuration.init(f.getName());
        serviceTableModel.initFromConfiguration(configuration);
        setServiceTableModel(serviceTableModel);
        setConfiguration(configuration);
    }

    public Boolean isRunning() {
        Boolean running;
        if (executor != null && !executor.isTerminated()) {
            running = true;
        } else {
            running = false;
        }
        return running;
    }

    /*check how many of services are submitted, running, successful or failed*/
    public OrchestratorStatus getStatus() {
        /*clear orchestratorStatus obj except of totalSubmitted*/
        int submitted = orchestratorStatus.getTotalSubmitted();
       // int retries = orchestratorStatus.getTotalRetries();
        orchestratorStatus.reset();
        orchestratorStatus.setTotalSubmitted(submitted);
        //orchestratorStatus.setTotalRetries(retries);
        List<ServiceParameter> lsp = configuration.getServiceParameters();
        orchestratorStatus.setTotalServices(lsp.size());
        int getValue = 0;
        for (ServiceParameter s : lsp) {
            if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_RUNNING)) {
                getValue = orchestratorStatus.getTotalRunning();
                orchestratorStatus.setTotalRunning(++getValue);
            } else if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_SUCCESS)) {
                getValue = orchestratorStatus.getTotalSuccess();
                orchestratorStatus.setTotalSuccess(++getValue);
            } else if (s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_FAILED)) {
                getValue = orchestratorStatus.getTotalFailed();
                orchestratorStatus.setTotalFailed(getValue + 1);
            }
            
            if (s.getActualRetries() > 0 &&(s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_SUCCESS)
                                            ||s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_FAILED)
                                            ||s.getStatus().equalsIgnoreCase(SWConstants.SERVICE_RUNNING))) {
                getValue = orchestratorStatus.getTotalRetries();
                orchestratorStatus.setTotalRetries(++getValue);
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
