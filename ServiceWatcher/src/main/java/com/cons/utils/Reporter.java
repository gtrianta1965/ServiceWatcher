package com.cons.utils;

import java.io.File;

import java.io.IOException;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Reporter {
    
    public Reporter() {
        super();
    }

    @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
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
        props.setProperty("mail.smtp.auth", "flase");
        
        props.setProperty("mail.transport.protocol", "smtp");
        // Get the default Session object.
        //Session session = Session.getDefaultInstance(props);
        Session session = Session.getInstance(props);
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Service Watcher Report");
            File input = new File("report.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            Element element = doc.select("p#date").first();
            
            Date date = new Date();
            element.text(date.toString());
            
            doc.select("p#field").first().appendElement("p").text("Log");
            

            // Now set the actual message
            message.setContent(doc.toString(), "text/html");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
