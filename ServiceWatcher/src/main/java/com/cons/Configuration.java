package com.cons;

import com.cons.services.ServiceParameter;
import com.cons.utils.CryptoUtils;

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
    private boolean smtpSendEmailOnSuccess = false;
    private int smtpSendActivityEmailInterval = 60000;
    private String smtpHost = "smtp.gmail.com";
    private int smtpPort = 465;
    private String smtpUsername = "";
    private String smtpPassword = "";
    private List<String> recipients = new ArrayList<String>();
    private int concurrentThreads = 5;
    private int httpResponseTimeout = 5000;
    private int ldapResponseTimeout = 5000;
    private int socketDieInterval = 5000;
    private final static String configFile = "config.properties";
    private String[] autoRefreshIntervals=null;

    Logger clientLog = null;
    boolean valid;
    String error;

    public Configuration() {
        super();
    }


    public void init() {
        //Initialize from the factory default configuration file (configFile)
        //CryptoUtils.obfuscatePasswordInConfig(configFile);
        init(configFile);
    }

    public void init(String fileName) {
        this.serviceParameters.clear();

        //CryptoUtils.deObfuscatePasswordInConfig(fileName);

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
                    if (prop.getProperty("password." + i) != null) {
                        serviceParameter.setPassword(CryptoUtils.decrypt(prop.getProperty("password." +
                                                                                          i))); //Read the password, g30 18/7/2017
                    } else {
                        serviceParameter.setPassword(prop.getProperty("password." +
                                                                      i)); //Read the password, g30 18/7/2017
                    }

                    //add each param based on the sequence number of the parameter
                    serviceParameters.add(serviceParameter);

                } else {
                    hasMore = false;
                }
            }

            //Read single value properties
            this.setConcurrentThreads(getNumberProperty(prop.getProperty("concurrentThreads"), 5));
            this.setHttpResponseTimeout(getNumberProperty(prop.getProperty("httpResponseTimeout"), 5000));
            this.setLdapResponseTimeout(getNumberProperty(prop.getProperty("ldapResponseTimeout"), 5000));
            this.setSocketDieInterval(getNumberProperty(prop.getProperty("socketDieInterval"), 5000));
            this.setSendMailUpdates(getBooleanProperty(prop.getProperty("sendMailUpdates"), false));
            this.setSmtpSendEmailOnSuccess(getBooleanProperty(prop.getProperty("smtpSendMailOnSuccess"), false));
            this.setSmtpSendActivityEmailInterval(getNumberProperty(prop.getProperty("smtpSendActivityEmailInterval"), 5)*1000);
            this.setSmtpHost(getStringProperty(prop.getProperty("smtpHost"), "smtp.gmail.com"));
            this.setSmtpPort(getNumberProperty(prop.getProperty("smtpPort"), 465));
            this.setSmtpUsername(getStringProperty(prop.getProperty("smtpUsername"), ""));
            this.setSmtpPassword(getStringProperty(prop.getProperty("smtpPassword"), ""));
            this.setAutoRefreshIntervals(getStringArrayProperty(prop.getProperty("autoRefreshIntervals"),new String[] {"1","2","3"}));
            
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
        //CryptoUtils.obfuscatePasswordInConfig(fileName);
    }
    
    private String getStringProperty(String value, String defaultValue) {
        String strValue = "";
        if (value != null) {
            try {
                strValue = value;
            } catch (NumberFormatException nfe) {
                strValue = defaultValue;
                System.out.println("NumberFormatException reading property value " + value + ". Set to default (" +
                                   defaultValue + ")");
            }

        } else {
            strValue = defaultValue;
        }
        return strValue;

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

    private String[] getStringArrayProperty(String value, String[] defaultValue) {
        String[] strValue = {""};
        if (value != null) {
            try {
                strValue = value.split(",");
            } catch (NumberFormatException nfe) {
                strValue = defaultValue;
                System.out.println("NumberFormatException reading property value " + value + ". Set to default (" +
                                   defaultValue + ")");
            }

        } else {
            strValue = defaultValue;
        }
        return strValue;

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
    
    
    public void setSmtpHost(String smtpHost){
        this.smtpHost = smtpHost;
    }
    
    public String getSmtpHost(){
        return this.smtpHost;
    }
    
    public void setSmtpPort(int smtpPort){
        this.smtpPort = smtpPort;
    }
    
    public int getSmtpPort(){
        return this.smtpPort;
    }
    
    public void setSmtpUsername(String smtpUsername){
        this.smtpUsername = smtpUsername;
    }
    
    public String getSmtpUsername(){
        return this.smtpUsername;
    }
    
    public void setSmtpPassword(String smtpPassword){
        this.smtpPassword = smtpPassword;
    }
    
    public String getSmtpPassword(){
        return this.smtpPassword;
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

    public void setSmtpSendEmailOnSuccess(boolean smtpSendEmailOnSuccess){
        this.smtpSendEmailOnSuccess = smtpSendEmailOnSuccess;
    }
    
    public boolean getSmtpSendEmailOnSuccess(){
        return this.smtpSendEmailOnSuccess;
    }
    
    public void setSmtpSendActivityEmailInterval(int smtpSendActivityEmailInterval){
        this.smtpSendActivityEmailInterval = smtpSendActivityEmailInterval;
    }
    
    public int getSmtpSendActivityEmailInterval(){
        return this.smtpSendActivityEmailInterval;
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
    
    public void setLdapResponseTimeout(int ldapResponseTimeout){
        this.ldapResponseTimeout = ldapResponseTimeout;
    }
    
    public int getLdapResponseTimeout(){
        return this.ldapResponseTimeout;
    }
    
    public String[] getAutoRefreshIntervals(){
        return this.autoRefreshIntervals;
}
    
    public void setAutoRefreshIntervals(String[] autoRefreshIntervals){
        this.autoRefreshIntervals=autoRefreshIntervals;        
    }
}
