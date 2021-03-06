package com.cons.services;

import com.cons.Configuration;


public class ServiceFactory {
    public ServiceFactory() {
        super();
    }
    
    public static Service createService(ServiceParameter serviceParameter, Configuration configuration){
        Service serviceToBeCreated = null;
        if (serviceParameter.getType() !=null){
            if(serviceParameter.getType().equalsIgnoreCase("HTTP")){
                serviceToBeCreated = new HTTPService(serviceParameter);                
            }else if (serviceParameter.getType().equalsIgnoreCase("DB")){
                serviceToBeCreated = new DBService(serviceParameter); //soon to be added
            }else if (serviceParameter.getType().equalsIgnoreCase("PING")){
                serviceToBeCreated = new SocketService(serviceParameter);
            }else if (serviceParameter.getType().equals("SFTP")){
                serviceToBeCreated = new SFTPService(serviceParameter);
            }else if (serviceParameter.getType().equals("LDAP")){
                serviceToBeCreated = new LDAPService(serviceParameter);
            }else if (serviceParameter.getType().equals("SSH")){
                serviceToBeCreated = new SSHProcService(serviceParameter);
            }else if (serviceParameter.getType().equals("SHELL")){
                serviceToBeCreated = new SHELLService(serviceParameter);
            }
            
        }else{
            serviceToBeCreated = null;
        }
        
        if (serviceToBeCreated != null) {
            serviceToBeCreated.setConfiguration(configuration);
        }
        return serviceToBeCreated;
    }
}
