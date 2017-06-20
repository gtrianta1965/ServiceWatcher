package com.cons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
    public Configuration() {
        super();
        clientLog = logger.LoggerConfig("confiquration");
        //        //init(configFile);
        clientLog.info("Init With Filename:" + SWConstants.LOG_FILE_NAME_CONFIQURATION);
    }


    ClientLogger logger = new ClientLogger();
    private List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
    private String concurentThreads = "0";
    private final static String configFile = "config.properties";
    Logger clientLog = null;

    public void init() {
//        clientLog = logger.LoggerConfig("confiquration");
//        //init(configFile);
//        clientLog.info("Init With Filename:" + SWConstants.LOG_FILE_NAME_CONFIQURATION);
        serviceParameters = this.getServiceParameters();
    }

//    public void init(String fileName) {
//        //to do
//        clientLog.info("Init With Filename:" + fileName);
//        serviceParameters = this.getServiceParameters();
//    }

    public boolean isValid() {
        return true;
    }

    public void save() {
        //to do
    }

    public void setserviceParameters(List<ServiceParameter> serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    public List<ServiceParameter> getServiceParameters() {
        clientLog.info("Confiquration.getserviceParameters::START:");
        ServiceParameter Sparams = new ServiceParameter();
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            //FileHandler handler = new FileHandler("myapp-log.%u.%g.txt", 1024 * 1024, 10, true);
            FileInputStream outpout = new FileInputStream(configFile);

            int i = 1;
            boolean b = true;
            while (b) {
                //Set values
                prop.load(outpout);
                clientLog.info("url." + i+" :"+ prop.getProperty("url." + i));
                Sparams.setUrl(prop.getProperty("url." + i));
                clientLog.info("description." + i+" :"+ prop.getProperty("description." + i));
                Sparams.setDescription(prop.getProperty("description." + i));
                clientLog.info("type." + i+" :"+ prop.getProperty("type." + i));
                Sparams.setType(prop.getProperty("type." + i));
                clientLog.info("group." + i+" :"+ prop.getProperty("group." + i));
                Sparams.setGroup(prop.getProperty("group." + i));
                clientLog.info("searchString." + i+" :"+ prop.getProperty("searchString." + i));
                Sparams.setSearchString(prop.getProperty("searchString." + i));
                //add each param based on the sequence number of the parameter
                serviceParameters.add(Sparams);
                    i++;
                b = prop.containsKey("url." + i);
                System.out.println("b:" + b);
            }
            this.setConcurentThreads(prop.getProperty("concurentThreads"));
            clientLog.info("concurentThreads:" + prop.getProperty("concurentThreads"));

        } catch (FileNotFoundException e) {
            clientLog.warning("File not Found:" + e);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        clientLog.info("Confiquration.getserviceParameters::END:");
        return serviceParameters;
    }

    public void setConcurentThreads(String concurentThreads) {
        this.concurentThreads = concurentThreads;
    }

    public String getConcurentThreads() {
        return concurentThreads;
    }
}
