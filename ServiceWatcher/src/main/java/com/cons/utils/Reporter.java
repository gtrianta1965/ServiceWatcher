package com.cons.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Reporter {
    
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    
    public Reporter() {
        super();
    }
    public void sendMail(){
        // Recipient's email ID needs to be mentioned.
        String to = "alexkalavitis@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "reporter@servicewatcher.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties props = new Properties();
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.auth", "true");
        
        props.setProperty("mail.transport.protocol", "smtp");
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(props);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alexkalavitis@gmail.com", "PeriSterones194969");
            }
        });
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Service Watcher Report");

            // Now set the actual message
            message.setContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
            "    <head>\n" + 
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" + 
            "        <title>A Simple Responsive HTML Email</title>\n" + 
            "        <style type=\"text/css\">\n" + 
            "        body {margin: 0; padding: 0; min-width: 100%!important;}\n" + 
            "        .content {width: 100%; max-width: 600px;}  \n" + 
            "        </style>\n" + 
            "    </head>\n" + 
            "    <body yahoo bgcolor=\"#f6f8f1\">\n" + 
            "    <h1>Service Watcher</h1>" +
            "    <h3>Report Date : " + new Date() +
            "    </body>\n" + 
            "</html>", "text/html");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
