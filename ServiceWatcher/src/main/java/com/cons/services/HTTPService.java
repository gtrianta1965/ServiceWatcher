package com.cons.services;


import com.cons.utils.SWConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                huc.setReadTimeout(configuration.getHttpResponseTimeout());
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
                            this.setSuccessCall("");
                            break;
                        }
                    // TODO: add service parameter for get tables colounm
                    // TODO: Must be generalized
                    } else if (serviceParameter.getDescription().equalsIgnoreCase("lab")){
                        System.out.println(getTableData(serviceParameter.getUrl(), 10));
                        if(getTableData(serviceParameter.getUrl(), 10)){
                            this.setSuccessfulCall(false);
                            this.setSuccessCall("Warning");
                            logger.debug("Warning");
                        }else {
                            this.setSuccessfulCall(true);
                            this.setSuccessCall("");
                            logger.debug("Success");
                        }
                        break;
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
    
// TODO: Must be generalized need fixes
    private boolean getTableData(String html, float thresHold){
        boolean isWarning = false;
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");
            
//            Elements tableHeaderEles = tableElements.select("thead tr th");
//            System.out.println("HEADERS");
//            for(int i=0; i<tableHeaderEles.size();i++){
//                System.out.println(tableElements.get(i).text());
//            }
//            System.out.println();
            
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            for (int i=0; i<tableRowElements.size(); i++){
                Element row = tableRowElements.get(i);
//                System.out.println("row");
                Elements rowItems = row.select("td");
                for (int j=0; j<rowItems.size(); j++){
                    if (j == 7){
                        Float temp = Float.parseFloat((rowItems.get(j).text()).split(" ")[0]);
                        if(temp < thresHold){
                            isWarning = true;
                        }
                    }
                }
            }
        } catch (IOException exio){
            exio.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return isWarning;
    }
}