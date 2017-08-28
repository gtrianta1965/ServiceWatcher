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

import org.apache.log4j.Logger;

public class Configuration {
    final static Logger logger = Logger.getLogger(Configuration.class);

    private CommandLineArgs cmdArgs;
    private List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
    private boolean sendMailUpdates = false;
    private boolean smtpSendEmailOnSuccess = false;
    private boolean isProduction = false;
    private boolean isLogEnabled = false;
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
    private String configFile = "config.properties";
    private String[] autoRefreshIntervals = null;

    Logger clientLog = null;
    boolean valid;
    String error;

    public Configuration() {
        super();
    }

    public void init() {
        //Initialize from the factory default configuration file (configFile)
        //CryptoUtils.obfuscatePasswordInConfig(configFile);
        init(getCmdArguments().getConfigFile());
    }

    public void init(String fileName) {
        logger.debug("init with file:" + fileName);
        this.serviceParameters.clear();
        setConfigFile(fileName);
        Properties prop = new Properties();
        InputStream input = null;


        setValid(true);
        setError(null);
        try {
            input = new FileInputStream(fileName);
            prop.load(input);
            //Read first all "array" properties (ServiceParameters)
            initServiceParameters(prop);

            //Read single value properties
            this.setConcurrentThreads(getNumberProperty(prop.getProperty("concurrentThreads"), 5));
            logger.debug("ConcurrentThreads=" + this.getConcurrentThreads());
            this.setHttpResponseTimeout(getNumberProperty(prop.getProperty("httpResponseTimeout"), 5000));
            logger.debug("HttpResponseTimeout=" + this.getHttpResponseTimeout());
            this.setLdapResponseTimeout(getNumberProperty(prop.getProperty("ldapResponseTimeout"), 5000));
            logger.debug("LdapResponseTimeout=" + getLdapResponseTimeout());
            this.setSocketDieInterval(getNumberProperty(prop.getProperty("socketDieInterval"), 5000));
            logger.debug("SocketDieInterval=" + getSocketDieInterval());
            this.setSendMailUpdates(getBooleanProperty(prop.getProperty("sendMailUpdates"), false));
            logger.debug("SendMailUpdates=" + getSendMailUpdates());
            this.setSmtpSendEmailOnSuccess(getBooleanProperty(prop.getProperty("smtpSendMailOnSuccess"), false));
            logger.debug("SmtpSendEmailOnSuccess=" + getSmtpSendEmailOnSuccess());
            this.setIsProduction(getBooleanProperty(prop.getProperty("isProduction"), false));
            logger.debug("IsProduction=" + isProduction());
            this.setIsLogEnabled(getBooleanProperty(prop.getProperty("isLogEnabled"), false));
            logger.debug("IsLogEnabled" + isLogEnabled());
            this.setSmtpSendActivityEmailInterval(getNumberProperty(prop.getProperty("smtpSendActivityEmailInterval"),
                                                                    5) * 1000);
            logger.debug("SmtpSendActivityEmailInterval=" + getSmtpSendActivityEmailInterval());
            this.setSmtpHost(getStringProperty(prop.getProperty("smtpHost"), "smtp.gmail.com"));
            logger.debug("SmtpHost=" + getSmtpHost());
            this.setSmtpPort(getNumberProperty(prop.getProperty("smtpPort"), 465));
            logger.debug("SmtpPort=" + getSmtpPort());
            this.setSmtpUsername(getStringProperty(prop.getProperty("smtpUsername"), ""));
            logger.debug("SmtpUsername=" + getSmtpUsername());
            if (prop.getProperty("smtpPassword") != null) {
                if (CryptoUtils.decrypt(prop.getProperty("smtpPassword")) != null) {
                    this.setSmtpPassword(getStringProperty(CryptoUtils.decrypt(prop.getProperty("smtpPassword")), ""));
                } else {
                    this.setSmtpPassword(getStringProperty(prop.getProperty("smtpPassword"), ""));
                }
            }
            this.setAutoRefreshIntervals(getStringArrayProperty(prop.getProperty("autoRefreshIntervals"),
                                                                new String[] { "1", "2", "3" }));
            logger.debug("AutoRefreshIntervals=" + this.getAutoRefreshIntervals().toString());

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

        if (isProduction() || getCmdArguments().getEncrypt()) {
            logger.info("Production mode:Obfuscating " + fileName);
            CryptoUtils.obfuscatePasswordInConfig(fileName);
        }

        //additional files
        if (getCmdArguments().getAllConfigFiles().size() > 1) {
            for (int i = 1; i < getCmdArguments().getAllConfigFiles().size(); i++) {
                prop = new Properties();
                try {
                    input = new FileInputStream(getCmdArguments().getAllConfigFiles().get(i));
                    prop.load(input);
                } catch (FileNotFoundException e) {
                    logger.error("Additional configuration file " + getCmdArguments().getAllConfigFiles().get(i) +
                                 " not found");
                } catch (IOException f) {
                    logger.error("Error reading additional configuration file " +
                                 getCmdArguments().getAllConfigFiles().get(i) + " (" + f.getMessage() + ")");
                }
                logger.debug("Read Services from additional configuration file:" +
                             getCmdArguments().getAllConfigFiles().get(i));
                initServiceParameters(prop);
            }

            getCmdArguments().getAllConfigFiles().clear();
        }

    }

    private String getStringProperty(String value, String defaultValue) {
        String strValue = "";
        if (value != null) {
            try {
                strValue = value;
            } catch (NumberFormatException nfe) {
                strValue = defaultValue;
                logger.warn("NumberFormatException reading property value " + value + ". Set to default (" +
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
                logger.warn("NumberFormatException reading property value " + value + ". Set to default (" +
                            defaultValue + ")");
            }

        } else {
            intValue = defaultValue;
        }
        return intValue;

    }

    private String[] getStringArrayProperty(String value, String[] defaultValue) {
        String[] strValue = { "" };
        if (value != null) {
            try {
                strValue = value.split(",");
            } catch (NumberFormatException nfe) {
                strValue = defaultValue;
                logger.warn("NumberFormatException reading property value " + value + ". Set to default (" +
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
                logger.warn("NumberFormatException reading property value " + value + ". Set to default (" +
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

    public void setCmdArguments(CommandLineArgs cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    public CommandLineArgs getCmdArguments() {
        return this.cmdArgs;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public int getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpUsername() {
        return this.smtpUsername;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSmtpPassword() {
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

    public void setSmtpSendEmailOnSuccess(boolean smtpSendEmailOnSuccess) {
        this.smtpSendEmailOnSuccess = smtpSendEmailOnSuccess;
    }

    public boolean getSmtpSendEmailOnSuccess() {
        return this.smtpSendEmailOnSuccess;
    }

    public void setIsProduction(boolean isProduction) {
        this.isProduction = isProduction;
    }

    public boolean isProduction() {
        return this.isProduction;
    }

    public void setIsLogEnabled(boolean isLogEnabled) {
        this.isLogEnabled = isLogEnabled;
    }

    public boolean isLogEnabled() {
        return this.isLogEnabled;
    }

    public void setSmtpSendActivityEmailInterval(int smtpSendActivityEmailInterval) {
        this.smtpSendActivityEmailInterval = smtpSendActivityEmailInterval;
    }

    public int getSmtpSendActivityEmailInterval() {
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

    public void setLdapResponseTimeout(int ldapResponseTimeout) {
        this.ldapResponseTimeout = ldapResponseTimeout;
    }

    public int getLdapResponseTimeout() {
        return this.ldapResponseTimeout;
    }

    public String[] getAutoRefreshIntervals() {
        return this.autoRefreshIntervals;
    }

    public void setAutoRefreshIntervals(String[] autoRefreshIntervals) {
        this.autoRefreshIntervals = autoRefreshIntervals;
    }

    protected void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return this.configFile;
    }


    private void initServiceParameters(Properties prop) {
        boolean hasMore = true;
        int i = 0;

        logger.debug("Start");
        while (hasMore) {
            i++;
            ServiceParameter serviceParameter = new ServiceParameter();
            serviceParameter.setUrl(prop.getProperty("url." + i));
            if (serviceParameter.getUrl() != null) {
                serviceParameter.setId(serviceParameters.size() + 1);
                serviceParameter.setDescription(prop.getProperty("description." + i));
                serviceParameter.setType(prop.getProperty("type." + i));
                serviceParameter.setGroup(prop.getProperty("group." + i));
                serviceParameter.setSearchString(prop.getProperty("searchString." + i));
                serviceParameter.setUsername(prop.getProperty("username." + i));
                //serviceParameter.setPassword(prop.getProperty("password." + i)); //Read the password, g30 18/7/2017
                if (prop.getProperty("password." + i) != null) {
                    if (CryptoUtils.decrypt(prop.getProperty("password." + i)) != null) {
                        serviceParameter.setPassword(CryptoUtils.decrypt(prop.getProperty("password." + i)));
                    } else {
                        serviceParameter.setPassword(prop.getProperty("password." + i));
                    }
                }
                if (prop.getProperty("retries." + i) != null) {
                    serviceParameter.setRetries(Integer.parseInt(prop.getProperty("retries." + i)));
                } else {
                    serviceParameter.setRetries(0);
                }
                serviceParameter.setQuery(prop.getProperty("query." + i));

                //add each param
                serviceParameter.setQuery(prop.getProperty("query." + i));
                serviceParameter.setCommand(prop.getProperty("command." + i));

                //add each param based on the sequence number of the parameter
                serviceParameters.add(serviceParameter);

            } else {
                hasMore = false;
            }
        }
        logger.debug("Read " + serviceParameters.size() + " services.");
    }
}
