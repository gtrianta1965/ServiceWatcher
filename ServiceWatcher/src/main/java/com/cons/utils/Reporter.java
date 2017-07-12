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

public class Reporter {

    public Reporter() {
        super();
    }

    @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
    public static void sendMail() {
        // Recipient's email ID needs to be mentioned.
        String to = "alexkalavitis@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = SWConstants.REPORTER_NAME;

        // Get system properties
        Properties props = new Properties();
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.auth", "flase");
        props.setProperty("mail.transport.protocol", "smtp");

        //Session session = Session.getDefaultInstance(props);
        Session session = Session.getInstance(props);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Init message parts
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart msgBodyPart = new MimeBodyPart();

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(SWConstants.REPORTER_MSG_SUBJECT);

            // Parse template
            File input = new File("../report_template.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            // Add date
            Element element = doc.select("p#date").first();
            Date date = new Date();
            element.text(date.toString());
            // Add log
            doc.select("p#field")
               .first()
               .appendElement("p")
               .text("Log");

            // Set HTML message
            msgBodyPart.setContent(doc.toString(), "text/html");

            // Add HTML to multipart
            multipart.addBodyPart(msgBodyPart);

            // Add image to message
            msgBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("../sw.png");
            msgBodyPart.setDataHandler(new DataHandler(fds));
            msgBodyPart.setHeader("Content-ID", "<image>");

            // Add image to multipart
            multipart.addBodyPart(msgBodyPart);

            // Set message content as multipart
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
