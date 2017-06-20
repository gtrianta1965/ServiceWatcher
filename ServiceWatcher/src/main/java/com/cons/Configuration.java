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


    ClientLogger logger = new ClientLogger();
    private List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
    private int concurentThreads = 5;
    private final static String configFile = "config.properties";
    Logger clientLog = null;

    public void init() {
        clientLog = logger.LoggerConfig("confiquration");
        init(configFile);
    }

    public void init(String fileName) {
        //to do
        clientLog.info("Init With Filename:"+fileName);
        serviceParameters = this.getServiceParameters();
    }

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
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            //FileHandler handler = new FileHandler("myapp-log.%u.%g.txt", 1024 * 1024, 10, true);
            FileInputStream outpout = new FileInputStream(configFile);

            //Set values
            prop.load(outpout);
                        clientLog.info("url.1:"+prop.getProperty("url.1"));
                        clientLog.info("description.1:"+prop.getProperty("description.1"));
                        clientLog.info("type.1:"+prop.getProperty("type.1"));
                        clientLog.info("group.1:"+prop.getProperty("group.1"));
                        clientLog.info("searchString.1:"+prop.getProperty("searchString.1"));
                        clientLog.info("concurentThreads:"+prop.getProperty("concurentThreads"));

        } catch (FileNotFoundException e) {
            clientLog.warning("File not Found:"+e);
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

    public void setConcurentThreads(int concurentThreads) {
        this.concurentThreads = concurentThreads;
    }

    public int getConcurentThreads() {
        return concurentThreads;
    }
}
