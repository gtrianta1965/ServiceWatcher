package com.cons.services;

public class ServiceFactory {
    public ServiceFactory() {
        super();
    }
    
    public static Service createService(String serviceType){
        Service serviceToBeCreated = null;
        if (serviceType !=null){
            if(serviceType.equalsIgnoreCase("HTTP")){
                serviceToBeCreated = new HTTPService();
            }else if (serviceType.equalsIgnoreCase("DB")){
                serviceToBeCreated = new DBService(); //soon to be added
            }
            
        }else{
            serviceToBeCreated = null;
        }
        
        return serviceToBeCreated;
    }
}
