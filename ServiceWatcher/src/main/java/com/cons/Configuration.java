package com.cons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
    private List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
    private int concurrentThreads = 5;
    private final static String configFile = "config.properties";
    ClientLogger logger = new ClientLogger();
    Logger clientLog = null;
    boolean valid;
    String  error;

    public Configuration() {
        super();
    }


    public void init() {
        //Initialize from the factory default configuration file (configFile)
        init(configFile);
    }

    public void init(String fileName) {
        this.serviceParameters.clear();
        
        Properties prop = new Properties();
        InputStream input = null;
        
        setValid(true);
        setError(null);
        try {
            input = new FileInputStream(fileName);
            prop.load(input);
            //Read first all "array" properties (ServiceParameters)
            int i = 0;
            boolean hasMore = true;
            while (hasMore) {
                i++;
                ServiceParameter serviceParameter = new ServiceParameter();
                serviceParameter.setUrl(prop.getProperty("url." + i));
                if (serviceParameter.getUrl() != null) {
                    serviceParameter.setId(i);
                    serviceParameter.setDescription(prop.getProperty("description." + i));
                    serviceParameter.setType(prop.getProperty("type." + i));
                    serviceParameter.setGroup(prop.getProperty("group." + i));
                    serviceParameter.setSearchString(prop.getProperty("searchString." + i));
                    //add each param based on the sequence number of the parameter
                    serviceParameters.add(serviceParameter);

                } else {
                    hasMore = false;
                } 
            }
            //Read single value properties
            String v = prop.getProperty("concurrentThreads");
            if ( v != null) {
               try {
                    this.setConcurrentThreads(Integer.parseInt(v));
                } catch (NumberFormatException nfe) {
                    this.setValid(false);
                    this.setError("NumberFormatException");
                }
            }

        } catch (FileNotFoundException e) {
            setValid(false);
            setError("Property file " + fileName + " does not exist");
        } catch (IOException io) {
            setValid(false);
            setError("Error reading from Property file " + fileName );
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    setValid(false);
                    setError("Error closing Property file " + fileName );
                }
            }
        }
    }


    public void save() {
        //to do
    }

    public void setserviceParameters(List<ServiceParameter> serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    public List<ServiceParameter> getServiceParameters() {
        return this.serviceParameters;
    }

    public void setConcurrentThreads(int concurentThreads) {
        this.concurrentThreads = concurentThreads;
    }

    public int getConcurrentThreads() {
        return concurrentThreads;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
    
    public static void main(String args[]){
        
        Configuration c = new Configuration();
        c.init();

    }
}
