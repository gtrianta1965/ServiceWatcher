package com.cons.services;

import com.cons.Configuration;
import com.cons.ui.ServicesTableModel;
import com.cons.utils.DateUtils;
import com.cons.utils.Reporter;
import com.cons.utils.SWConstants;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceOrchestrator {
    protected List<String> statusLog;
    private Configuration configuration;
    private ServicesTableModel serviceTableModel;
    private ExecutorService executor;
    private Timer refreshClock;
    
    //The following variables control the fire times of the refresh timer.    
    private Date currentRefreshFireTime = null;
    private Date nextRefreshFireTime = null;
    private OrchestratorStatus orchestratorStatus;
    private long diff;
    
    public ServiceOrchestrator() {
        super();
        orchestratorStatus = new OrchestratorStatus();
        this.statusLog = new ArrayList<String>();
        this.refreshClock = new Timer();
    }

    public void checkStartRefresh(long refreshTime) {
        refreshClock.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(checkRefreshClock(refreshTime));
            }
        }, 0, 200);
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
            configuration.getServiceParameters().get(i).setStatus(SWConstants.SERVICE_SUBMITTED); //Fix bug with unxplainable behavior of status counters
            
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
        Reporter.sendMail(configuration.getRecipients().toArray(new String[0]),
                          this.statusLog,
                          configuration.getSmtpHost(),
                          configuration.getSmtpPort(),
                          configuration.getSmtpUsername(),
                          configuration.getSmtpPassword());
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
    
    /**
     * Cleans log
     */
    public void cleanLog(){
        this.statusLog = new ArrayList<String>();
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
    
    public void disableRefresh(){
        //Autorefresh is disabled. Nullify current and next fire times
        currentRefreshFireTime = null;
        nextRefreshFireTime = null;
    }
    
    public boolean checkRefreshClock(long refreshIn){
        //Check if we are running, that is the current time is not null
        if (currentRefreshFireTime != null) {
            diff = DateUtils.getDateDiff(new Date(), nextRefreshFireTime, TimeUnit.SECONDS);
            //The diff should be positive (as the nextRefreshDate points in the future
            //In case the diff is negative then force a refresh. This might happen if 
            //we put the PC in a sleep mode. When it wakes up the diff is negative
            if (diff < 0) {
                nextRefreshFireTime = DateUtils.addMinutesToDate(new Date(), refreshIn);                    
                diff = DateUtils.getDateDiff(new Date(), nextRefreshFireTime, TimeUnit.SECONDS);
            }
            if (diff == 0) {
                refreshServices(refreshIn);
                return true;
            }
        } else {
            //The refresh never executed (execute it now)
            currentRefreshFireTime = new Date(); //Set the current to NOW!!!
            //Set the next fire time according to interval specified
            nextRefreshFireTime = DateUtils.addMinutesToDate(currentRefreshFireTime,refreshIn);
            this.start();
            return true;
        }
        return false;
    }
    private void refreshServices(long refreshIn) {
        currentRefreshFireTime = nextRefreshFireTime;
        nextRefreshFireTime = DateUtils.addMinutesToDate(currentRefreshFireTime, refreshIn);
        this.start();
    }
    public long getDiff(){
        return this.diff;
    }
}
