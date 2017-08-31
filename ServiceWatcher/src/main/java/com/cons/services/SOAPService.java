package com.cons.services;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class SOAPService extends Service {
    final static Logger logger = Logger.getLogger(SOAPService.class);

    public SOAPService() {
        super();
    }

    public SOAPService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {
        String responseString = "";
        String outputString = "";
        String wsURL = serviceParameter.getUrl();
        URL url;
        try {
            url = new URL(wsURL);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            //            String xmlInput =
            //                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://litwinconsulting.com/webservices/\">\n" +
            //                " <soapenv:Header/>\n" + " <soapenv:Body>\n" + " <web:GetWeather>\n" + " <!--Optional:-->\n" +
            //                " <web:City>" + city + "</web:City>\n" + " </web:GetWeather>\n" + " </soapenv:Body>\n" +
            //                " </soapenv:Envelope>";
            String xmlInput = serviceParameter.getQuery();

            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();

            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", serviceParameter.getCommand());
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            //Write the content of the request to the outputstream of the HTTP Connection.
            out.write(b);
            out.close();
            //Ready with sending the request.

            //Read the response.
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            //Write the SOAP message response to a String.
            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
            //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
            Document document = parseXmlFile(outputString);
            NodeList nodeLst = document.getElementsByTagName(serviceParameter.getSearchString());
            String result = nodeLst.item(0).getTextContent();
            logger.debug(result);

            //Write the SOAP message formatted to the console.
            String formattedSOAPResponse = formatXML(outputString);
            logger.debug(formattedSOAPResponse);
        } catch (MalformedURLException ex) {
            // Catch and set call false and set error
            logger.debug("MalformedURLException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        } catch (IOException ex) {
            // Catch and set call false and set error
            logger.debug("IOException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        }
    }

    //format the XML in your String
    private String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException ex) {
            // Catch and set call false and set error
            logger.debug("IOException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException ex) {
            // Catch and set call false and set error
            logger.debug("ParserConfigurationException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        } catch (SAXException ex) {
            // Catch and set call false and set error
            logger.debug("SAXException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        } catch (IOException ex) {
            // Catch and set call false and set error
            logger.debug("IOException: " + ex.getMessage());
            this.setSuccessfulCall(false);
            this.setErrorCall(ex.getMessage());
        }
    }

}
