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
    }
    Logger logger = Logger.getLogger("confiquration");
    public List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
    private int concurentThreads = 5;
    private final static String configFile="config.properties";

    public void init() {
        init(configFile);
    }

    public void init(String fileName) {
        //to do
        logger.info("Init With Filename:"+fileName);
        serviceParameters=this.getServiceParameters();
    }
    public boolean isValid(){
        return true;
    }
    public void save(){
        //to do 
    }

    public void setserviceParameters(List<ServiceParameter> serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    public List<ServiceParameter> getServiceParameters() {
        
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            FileInputStream outpout = new FileInputStream(configFile);

            //Set values
            prop.load(outpout);
            logger.info("url.1:"+prop.getProperty("url.1"));
            logger.info("description.1:"+prop.getProperty("description.1"));
            logger.info("type.1:"+prop.getProperty("type.1"));
            logger.info("group.1:"+prop.getProperty("group.1"));
            logger.info("searchString.1:"+prop.getProperty("searchString.1"));
            logger.info("concurentThreads:"+prop.getProperty("concurentThreads"));

        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
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
        
        
        
        return serviceParameters;
    }

    public void setConcurentThreads(int concurentThreads) {
        this.concurentThreads = concurentThreads;
    }

    public int getConcurentThreads() {
        return concurentThreads;
    }
}
