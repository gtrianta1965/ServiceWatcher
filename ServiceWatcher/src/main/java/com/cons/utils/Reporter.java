package com.cons.utils;

import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;
import com.cons.services.ServiceParameter;

import java.io.InputStream;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Reporter{
    final static Logger logger = Logger.getLogger(Reporter.class);

    protected List<Hashtable<String, String>> statusLog;
    private ServiceOrchestrator serviceOrchestrator;
    private Configuration configuration;
    
    public Reporter(ServiceOrchestrator serviceOrchestrator) {
        super();
        this.serviceOrchestrator = serviceOrchestrator;
        this.configuration = this.serviceOrchestrator.getConfiguration();
        this.statusLog = new ArrayList<Hashtable<String, String>>();
    }
    
    public void send() {
        try {
            sendMail();
        } catch (Exception ex) {
            logger.debug("Failed to send e-mail. " + ex.getMessage());
            if(configuration.getCmdArguments().isNoGUI()){
                System.out.println("Failed to send e-mail.");
            }
        }
    }
    
    /**
     * This function sends a log email to the recipients based on an html template.
     * @param recipients is a string array which includes all the recipients e-mails.
     * @param log is a string array which includes the log to be sent via e-mail.
     */
    private void sendMail() {
        logger.info("Starting Sending Mail Proccess");
        
        logger.debug("Making log from results to send.");
        makeLog();
        // Mail Header
        final String[] recipients = this.configuration.getRecipients().toArray(new String[0]);
        final List<Hashtable<String, String>> log = this.statusLog;
        final String host = this.configuration.getSmtpHost();
        final int port = this.configuration.getSmtpPort();
        final String username = this.configuration.getSmtpUsername();
        final String password = this.configuration.getSmtpPassword();
        
        // IP resolve
        List<InternetAddress> addresses = new ArrayList<InternetAddress>();
        // Recipient's email ID needs to be mentioned.
        InternetAddress[] to;
        // Sender's email ID needs to be mentioned
        String from = SWConstants.REPORTER_NAME;
        
        logger.debug("Initializing SMTP properties.");
        // Get system properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port",Integer.toString(port));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Integer.toString(port));
        logger.debug("Creating authentication.");
        //get Session
        Session session = Session.getDefaultInstance(props,    
            new javax.mail.Authenticator() {    
            protected PasswordAuthentication getPasswordAuthentication() {    
                return new PasswordAuthentication(username, password);  
                }
            }
        );

        try {
            logger.debug(SWConstants.REPORTER_DEBUG_COOKING);
            int failed = 0;
            String appendSubjectStatus = "";
            
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Init message parts
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart msgBodyPart = new MimeBodyPart();

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            for(String recipient:recipients){
                addresses.add(new InternetAddress(recipient));
            }
            to = addresses.toArray(new InternetAddress[addresses.size()]);
            // Set To: header field of the header.
            message.addRecipients(Message.RecipientType.TO, to);
            log.get(0);
            for(Hashtable report:log){
                if(((String) report.get("status")).contains("Down")){
                    ++failed;
                }
            }
            
            if(failed > 0){
                appendSubjectStatus = " (" + failed + " of " + log.size() + " have failed)";
            }
            // Set Subject: header field
            message.setSubject(SWConstants.REPORTER_MSG_SUBJECT + appendSubjectStatus);

            // Set HTML message
            msgBodyPart.setContent(vlc().toString(), "text/html");

            // Add HTML to multipart
            multipart.addBodyPart(msgBodyPart);

            // Add image to message
            msgBodyPart = new MimeBodyPart();
            msgBodyPart = makeAttachment("/images/swCrop.png", "<image>");

            // Add image to multipart
            multipart.addBodyPart(msgBodyPart);

            // Set message content as multipart
            message.setContent(multipart);
            logger.info(SWConstants.REPORTER_INFO_STATUS_SENDING);
            // Send message
            Transport.send(message);
            
            logger.info(SWConstants.REPORTER_INFO_STATUS_SEND);
        } catch (AuthenticationFailedException auther){
            logger.warn(SWConstants.REPORTER_WARN);
            logger.error(SWConstants.REPORTER_FAIL_AUTH);
            logger.debug("Trace: " + auther.getMessage());
        } catch (MessagingException mex) {
            logger.warn(SWConstants.REPORTER_WARN);
            logger.error(SWConstants.REPORTER_FAIL_MESSAGING);
            logger.debug("Trace: " + mex.getMessage());
        } catch (Exception ex) {
            logger.warn(SWConstants.REPORTER_WARN);
            logger.error(SWConstants.REPORTER_FAIL_GENERAL);
            logger.debug("Trace: " + ex.getMessage());
        }
    }
    
    /**
     * Makes a mail attachment from a relative file path and adds refrenses it to an id to be later used
     * in an HTML page.
     * 
     * @param attachmentPath relative file path to attachment file.
     * @param id an id reference which defines where it should be put if the id exists in the HTML.
     * @return returns a bodypart object in order to be added to a multipart object message in an email.
     */
    private static BodyPart makeAttachment(String attachmentPath, String id){
        BodyPart msgBodyPart = new MimeBodyPart();
        InputStream stream = Reporter.class.getResourceAsStream(attachmentPath); //or null if you can't obtain a ServletContext

        if (stream == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = Reporter.class.getClassLoader();
            }
            stream = classLoader.getResourceAsStream(attachmentPath);
       }

       try {
            DataSource fds = new ByteArrayDataSource(stream, "image/*");
            msgBodyPart.setDataHandler(new DataHandler(fds));
            msgBodyPart.setHeader("Content-ID", id);
            msgBodyPart.setFileName(MimeUtility.encodeText("logo.png", "UTF-8", null));
        } catch (MessagingException msge) {
            logger.error(SWConstants.REPORTER_FAIL_MESSAGING);
            logger.debug("Trace: " + msge.getMessage());
        } catch (Exception ex) {
            logger.error(SWConstants.REPORTER_FAIL_GENERAL);
            logger.debug("Trace: " + ex.getMessage());
        }
        return msgBodyPart;
    }
    
    private void makeLog(){
        this.statusLog.clear();
        for(ServiceParameter sp:this.serviceOrchestrator.getConfiguration().getServiceParameters()){
            Hashtable<String, String> logKV = new Hashtable<String, String>();
            logKV.put("description", sp.getDescription()==null?"":sp.getDescription());
            logKV.put("url", sp.getUrl()==null?"":sp.getUrl());
            logKV.put("type", sp.getType()==null?"":sp.getType());
            logKV.put("group",sp.getGroup()==null?"":sp.getGroup());
            logKV.put("username",sp.getUsername()==null?"":sp.getUsername());
            logKV.put("error",sp.getError()==null?"":sp.getError());
            logKV.put("retries",Integer.toString(sp.getRetries())==null?"":Integer.toString(sp.getRetries()));
            logKV.put("actual_retries",Integer.toString(sp.getActualRetries())==null?"":Integer.toString(sp.getActualRetries()));
            logKV.put("search_string",sp.getSearchString()==null?"":sp.getSearchString());
            logKV.put("context",sp.getContext()==null?"":sp.getContext());
            logKV.put("query",sp.getQuery()==null?"":sp.getQuery());
            logKV.put("command",sp.getCommand()==null?"":sp.getCommand());
            logKV.put("status", (sp.getStatus() == SWConstants.SERVICE_SUCCESS?"Up":"Down"));
            statusLog.add(logKV);
        }
    }
    
    /**
     * Fills an html template with the log output and programs operating configuration.
     * @return
     */
    private String vlc(){
        StringWriter w = new StringWriter();
        VelocityEngine ve = new VelocityEngine();
        VelocityContext context = new VelocityContext();
        ve.init();
        
        Template t = ve.getTemplate("simple_report_template.html");
        
        context.put("program_name", SWConstants.REPORTER_TEMPLATE_TITLE);
        context.put("version", SWConstants.PROGRAM_VERSION);
        context.put("date", (new Date()).toString());
        context.put("configuration", configuration);
        context.put("service_parameters", configuration.getServiceParameters());
        
        makeLog();
        context.put("logs", statusLog);
        
        t.merge(context, w);
        
        return w.toString();
    }
    
    /**
     * Fills a json file using a specified Template with application configuration and logs.
     * @param jsonTemplate
     */
    public String vlcJSON(String fileName){
        StringWriter w = new StringWriter();
        VelocityEngine ve = new VelocityEngine();
        VelocityContext context = new VelocityContext();
        
        Template jsonTemplate = ve.getTemplate(fileName);
        
        context.put("program_name", SWConstants.REPORTER_TEMPLATE_TITLE);
        context.put("version", SWConstants.PROGRAM_VERSION);
        context.put("date", (new Date()).toString());
        context.put("configuration", configuration);
        context.put("service_parameters", configuration.getServiceParameters());
        
        makeLog();
        context.put("logs", this.statusLog);
        jsonTemplate.merge(context, w);

        return w.toString();
    }
}