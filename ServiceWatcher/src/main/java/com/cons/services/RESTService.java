package com.cons.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;


public class RESTService extends Service {
    final static Logger logger = Logger.getLogger(RESTService.class);

    public RESTService() {
        super();
    }

    public RESTService(ServiceParameter sp) {
        super(sp);
    }

    @Override
    public void service() {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL(serviceParameter.getUrl());
            conn = (HttpURLConnection) url.openConnection();

            if (serviceParameter.getCommand() == "POST") {
                conn.setDoOutput(true);
            }

            conn.setRequestMethod(serviceParameter.getCommand());

            conn.setRequestProperty("Content-Type", "application/json");

            String input = serviceParameter.getQuery();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (serviceParameter.getCommand() == "POST") {
                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
            } else {
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            this.setSuccessfulCall(true);
            this.setSuccessCall(sb.toString());

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
        } finally {
            conn.disconnect();
        }
    }
}
