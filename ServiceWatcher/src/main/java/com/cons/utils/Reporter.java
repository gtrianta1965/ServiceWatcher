package com.cons.utils;

import com.cons.Configuration;
import com.cons.services.ServiceOrchestrator;

import com.cons.services.ServiceParameter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.io.IOException;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimeZone;

import java.util.TimerTask;

import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.activation.FileDataSource;

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


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Reporter{
    protected List<String> statusLog;
    private Timer mailTimer;
    private ServiceOrchestrator serviceOrchestrator;
    private Configuration configuration;
    
    public Reporter(ServiceOrchestrator serviceOrchestrator) {
        super();
        this.serviceOrchestrator = serviceOrchestrator;
        this.configuration = this.serviceOrchestrator.getConfiguration();
        this.statusLog = new ArrayList<String>();
    }
    
    public void send() {
        try {
            System.out.print(SWConstants.REPORTER_RUN_ONCE_MSG);
            sendMail();
            System.out.println(SWConstants.REPORTER_RUN_ONCE_DONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This function sends a log email to the recipients based on an html template.
     * @param recipients is a string array which includes all the recipients e-mails.
     * @param log is a string array which includes the log to be sent via e-mail.
     */
    private void sendMail() {
        makeLog();
        // Mail Header
        final String[] recipients = this.configuration.getRecipients().toArray(new String[0]);
        final List<String> log = this.statusLog;
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

        // Get system properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port",Integer.toString(port));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Integer.toString(port));
        //get Session
        Session session = Session.getDefaultInstance(props,    
            new javax.mail.Authenticator() {    
            protected PasswordAuthentication getPasswordAuthentication() {    
                return new PasswordAuthentication(username, password);  
                }
            }
        );

        //Session session = Session.getDefaultInstance(props);

        try {
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

            for(String report:log){
                if(report.contains("DOWN")){
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
            msgBodyPart = makeAttachment("../sw.png", "<image>");

            // Add image to multipart
            multipart.addBodyPart(msgBodyPart);

            // Set message content as multipart
            message.setContent(multipart);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
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
        DataSource fds = new FileDataSource(attachmentPath);
        try {
            msgBodyPart.setDataHandler(new DataHandler(fds));
            msgBodyPart.setHeader("Content-ID", id);
            msgBodyPart.setFileName(MimeUtility.encodeText("logo.png", "UTF-8", null));
        } catch (MessagingException msge) {
            msge.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return msgBodyPart;
    }
    
    private void makeLog(){
        this.statusLog.clear();
        for(ServiceParameter sp:this.serviceOrchestrator.getConfiguration().getServiceParameters()){
            statusLog.add("Service: " + sp.getDescription() + " is " + (sp.getStatus() == SWConstants.SERVICE_SUCCESS?"UP":"DOWN"));
        }
    }
    
    private String vlc(){
        StringWriter w = new StringWriter();
        VelocityEngine ve = new VelocityEngine();
        VelocityContext context = new VelocityContext();
        ve.init();
        Vector logVector = new Vector();
        
        Template t = ve.getTemplate("report_template.html");
        
        context.put("title", SWConstants.REPORTER_TEMPLATE_TITLE + " Version " + SWConstants.PROGRAM_VERSION);
        context.put("date", (new Date()).toString());
        context.put("configuration", configuration);
        context.put("service_parameters", configuration.getServiceParameters());
        
        makeLog();
        for(String alog : statusLog){
            logVector.addElement(alog);
        }
        
        context.put("logs", logVector.iterator());
        
        t.merge(context, w);
        
        return w.toString();
    }
    
    /**
     * JSON log exporter.
     * 
     * @param log the log of services to be exported
     * @return returns a JSONObject which includes:
     * name of application,
     * application version,
     * timestamp of export,
     * services log.
     */
    public static JSONObject exportToJSON(List<String> log){
        JSONObject jsn = new JSONObject();
        jsn.put("Application", "Service Watcher");
        jsn.put("Version", "1");
        jsn.put("Date",Calendar.getInstance(TimeZone.getTimeZone("Greece/Athens")).getTime().toString());
        
        JSONArray alog = new JSONArray();
        for(String a:log){
            alog.put(a);
        }
        
        jsn.put("Log", alog);
        return jsn;
    }
}
