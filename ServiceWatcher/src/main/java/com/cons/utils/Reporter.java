package com.cons.utils;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import java.util.TimeZone;

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

import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Reporter {

    public Reporter() {
        super();
    }
    
    /**
     * This function sends a log email to the recipients based on an html template.
     * @param recipients is a string array which includes all the recipients e-mails.
     * @param log is a string array which includes the log to be sent via e-mail.
     */
    public static void sendMail(String[] recipients, List<String> log, final String host, int port, final String username, final String password) {
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

            // Set Subject: header field
            message.setSubject(SWConstants.REPORTER_MSG_SUBJECT);

            // Set HTML message
            msgBodyPart.setContent(makePage("../report_template.html", log).toString(), "text/html");

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
     * This function initializes and loads a Document based on an HTML template with UTF-8 encoding
     * pareses it with jsoup adds now date to a <p> element with id="date"
     * and appends <p>log n</p> * n logs to the log section of the page
     * 
     * @param templatePath is the relative path to the HTML template
     * @param log is the log to be added in the log section of the page
     * @return returns an HTML page type of Document.
     */
    private static Document makePage(String templatePath, List<String> log){
        Document doc = null;
        File input = null;
        try{
            // Parse template
            input = new File(templatePath);
            doc = Jsoup.parse(input, "UTF-8");
            // Add date
            Element element = doc.select("p#date").first();
            Date date = new Date();
            element.text(date.toString());
            // Add log
            element = doc.select("p#field").first().appendElement("h4 id=\"logtlt\" align=\"center\"");
            element.text("Log");
            
            int id=0;
            for(String report:log){
                String status = "white";
                if(report.contains("UP")){
                    status = "#76BB1E";
                }else{
                    status = "#D24626";
                }
                doc.select("p#field").first()
                   .appendElement("p id=\"" + id + "\" style=\"background-color: " + status + ";\"")
                   .text(report);
                id++;
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return doc;
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
