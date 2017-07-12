package com.cons.utils;

import java.io.File;

import java.io.IOException;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

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
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart msgBodyPart = new MimeBodyPart();
            
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Service Watcher Report");
            
            // Parse template
            File input = new File("../report_template.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            // Add date
            Element element = doc.select("p#date").first();
            Date date = new Date();
            element.text(date.toString());
            // Add log
            doc.select("p#field").first().appendElement("p").text("Log");
            
            // Now set the actual message
            msgBodyPart.setContent(doc.toString(), "text/html");
            multipart.addBodyPart(msgBodyPart);
            // second part (the image)
            msgBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("../sw.png");
            msgBodyPart.setDataHandler(new DataHandler(fds));
            msgBodyPart.setHeader("Content-ID", "<image>");
            
            multipart.addBodyPart(msgBodyPart);
            
            message.setContent(multipart);

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
