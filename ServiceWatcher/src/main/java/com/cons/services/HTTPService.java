package com.cons.services;


import com.cons.utils.SWConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.log4j.Logger;

public class HTTPService extends Service {
    final static Logger logger = Logger.getLogger(HTTPService.class);
    public HTTPService() {
        super();
    }

    public HTTPService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {

        int responseCode = 0;
        String responseMessage = null;
        boolean found = false;
        try {
            logger.debug("starting an HTTP connection");
            URL url = new URL(serviceParameter.getUrl());
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            if (configuration != null) {
                huc.setConnectTimeout(configuration.getHttpResponseTimeout());
                logger.debug("connection timed out");
            }
            huc.connect();
            logger.debug("connected");
            responseCode = huc.getResponseCode();
            responseMessage = huc.getResponseMessage();

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            if (responseCode == 200) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (serviceParameter.getSearchString() != null) {
                        if (inputLine.toLowerCase().contains(serviceParameter.getSearchString().toLowerCase())) {
                            logger.debug("connection success");
                            this.setSuccessfulCall(true);
                            found = true;
                            break;
                        }
                    } else {
                        logger.debug("connection success");
                        this.setSuccessfulCall(true);
                        break;
                    }

                }
                if (serviceParameter.getSearchString() != null && found == false) { //if no match found for getSearchString
                    logger.debug("connection failed");
                    this.setSuccessfulCall(false);
                    this.setErrorCall(SWConstants.SEARCH_STRING_NOT_FOUND_MSG);
                }
            } else { //if status is <>200
                logger.debug(SWConstants.URL_RESPONSE_ERROR_MSG + responseMessage);
                this.setSuccessfulCall(false);
                this.setErrorCall(SWConstants.URL_RESPONSE_ERROR_MSG + responseMessage);
            }
            
            logger.debug("connection closed");
            in.close();

        } catch (MalformedURLException e) {
            this.setSuccessfulCall(false);
            logger.debug(SWConstants.MALLFORMED_URL_EXCEPTION_MSG + e.getMessage());
            this.setErrorCall(SWConstants.MALLFORMED_URL_EXCEPTION_MSG + e.getMessage());
        } catch (ProtocolException e) {
            this.setSuccessfulCall(false);
            logger.debug(SWConstants.PROTOCOL_EXCEPTION_MSG + e.getMessage());
            this.setErrorCall(SWConstants.PROTOCOL_EXCEPTION_MSG + e.getMessage());
        } catch (Exception e) {
            this.setSuccessfulCall(false);
            logger.debug(SWConstants.GENERIC_EXCEPTION_MSG + e.getMessage());
            this.setErrorCall(SWConstants.GENERIC_EXCEPTION_MSG + e.getMessage());
        }
    }


    public static void main(String args[]) {

        HTTPService hs = new HTTPService();
        hs.run();

    }

}
