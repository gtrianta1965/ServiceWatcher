package com.cons.services;


public class OrchestratorStatus {
    
    private int totalServices;
    private int totalRunning;
    private int totalSubmitted;
    private int totalSuccess;
    private int totalFailed;
    
    public OrchestratorStatus() {
        super();
        reset();
    }


    public void setTotalServices(int totalServices) {
        this.totalServices = totalServices;
    }

    public int getTotalServices() {
        return totalServices;
    }

    public void setTotalRunning(int totalRunning) {
        this.totalRunning = totalRunning;
    }

    public int getTotalRunning() {
        return totalRunning;
    }

    public void setTotalSubmitted(int totalSubmitted) {
        this.totalSubmitted = totalSubmitted;
    }

    public int getTotalSubmitted() {
        return totalSubmitted;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void reset(){
        this.totalSubmitted = 0;
        this.totalServices = 0;
        this.totalRunning = 0;
        this.totalFailed = 0;
        this.totalSuccess = 0;        
    }
    
    @Override
    public String toString() {
        String statusBarMsg = "Total services : %d "+ "  Submited : %d" +"  Running : %d" +"  Successful : %d"+ "  Failed : %d";
        return String.format(statusBarMsg, this.totalServices,this.totalSubmitted, this.totalRunning, this.totalSuccess, this.totalFailed);
    }
    
    public String toHTML(){
        //soon to be added.
        String statusBarMsg = ("<html>"+"Total services : %d "+ "  Submited :<font color=blue> %d </font>" +"  Running : %d" +"  Successful :<font color=green> %d</font>"+ "  Failed : <font color=red> %d </font>"+"</html>");
        return String.format(statusBarMsg, this.totalServices,this.totalSubmitted, this.totalRunning, this.totalSuccess, this.totalFailed);
    }
    
}
