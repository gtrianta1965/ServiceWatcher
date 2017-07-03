package com.cons.services;


public class ServiceFactory {
    public ServiceFactory() {
        super();
    }
    
    public static Service createService(ServiceParameter serviceParameter){
        Service serviceToBeCreated = null;
        if (serviceParameter.getType() !=null){
            if(serviceParameter.getType().equalsIgnoreCase("HTTP")){
                serviceToBeCreated = new HTTPService(serviceParameter);
            }else if (serviceParameter.getType().equalsIgnoreCase("DB")){
                serviceToBeCreated = new DBService(serviceParameter); //soon to be added
            }
            
        }else{
            serviceToBeCreated = null;
        }
        
        return serviceToBeCreated;
    }
}
