package com.cons;

import com.cons.services.ServiceParameter;

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
    private boolean sendMailUpdates = false;
    private List<String> recipients = new ArrayList<String>();
    private int concurrentThreads = 5;
    private int httpResponseTimeout = 5000;
    private int socketDieInterval = 5000;
    private final static String configFile = "config.properties";

    Logger clientLog = null;
    boolean valid;
    String error;

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
                    serviceParameter.setUsername(prop.getProperty("username." + i));
                    serviceParameter.setPassword(prop.getProperty("password." + i)); //Read the password, g30 18/7/2017

                    //add each param based on the sequence number of the parameter
                    serviceParameters.add(serviceParameter);

                } else {
                    hasMore = false;
                }
            }


            //Read single value properties
            this.setConcurrentThreads(getNumberProperty(prop.getProperty("concurrentThreads"), 5));
            this.setHttpResponseTimeout(getNumberProperty(prop.getProperty("httpResponseTimeout"), 5000));
            this.setSocketDieInterval(getNumberProperty(prop.getProperty("socketDieInterval"), 5000));
            this.setSendMailUpdates(getBooleanProperty(prop.getProperty("sendMailUpdates"), false));

            if (getSendMailUpdates()) {
                String emails = prop.getProperty("to");
                if (emails != null) {
                    String[] emailArr = emails.split(",");
                    for (String email : emailArr) {
                        this.addRecipients(email);
                    }
                } else {
                    this.setSendMailUpdates(false);
                }
            }
        } catch (FileNotFoundException e) {
            setValid(false);
            setError("Property file " + fileName + " does not exist");
        } catch (IOException io) {
            setValid(false);
            setError("Error reading from Property file " + fileName);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    setValid(false);
                    setError("Error closing Property file " + fileName);
                }
            }
        }
    }

    private int getNumberProperty(String value, int defaultValue) {
        int intValue = 0;
        if (value != null) {
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                intValue = defaultValue;
                System.out.println("NumberFormatException reading property value " + value + ". Set to default (" +
                                   defaultValue + ")");
            }

        } else {
            intValue = defaultValue;
        }
        return intValue;

    }

    private boolean getBooleanProperty(String value, boolean defaultValue) {
        boolean booleanValue = false;
        if (value != null) {
            try {
                booleanValue = Boolean.parseBoolean(value);
            } catch (NumberFormatException nfe) {
                booleanValue = defaultValue;
                System.out.println("NumberFormatException reading property value " + value + ". Set to default (" +
                                   defaultValue + ")");
            }

        } else {
            booleanValue = defaultValue;
        }
        return booleanValue;

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

    public void setSendMailUpdates(boolean sendMailUpdates) {
        this.sendMailUpdates = sendMailUpdates;
    }

    public boolean getSendMailUpdates() {
        return this.sendMailUpdates;
    }

    public void setConcurrentThreads(int concurentThreads) {
        this.concurrentThreads = concurentThreads;
    }

    public int getConcurrentThreads() {
        return concurrentThreads;
    }

    public void setSocketDieInterval(int socketDieInterval) {
        this.socketDieInterval = socketDieInterval;
    }

    public int getSocketDieInterval() {
        return socketDieInterval;
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

    public void addRecipients(String recipient) {
        this.recipients.add(recipient);
    }

    public List<String> getRecipients() {
        return this.recipients;
    }

    public void setHttpResponseTimeout(int httpResponseTimeout) {
        this.httpResponseTimeout = httpResponseTimeout;
    }

    public int getHttpResponseTimeout() {
        return httpResponseTimeout;
    }
}
