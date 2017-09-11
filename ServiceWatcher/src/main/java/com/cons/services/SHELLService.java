package com.cons.services;

import com.cons.utils.GenericUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class SHELLService extends Service {

    final static Logger logger = Logger.getLogger(SHELLService.class);


    public SHELLService() {
        super();
    }

    public SHELLService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }


    @Override
    public void service() {
        String result = executeCommand();

        if ((result != null) && (result.contains(serviceParameter.getSearchString()))) {
            setSuccessfulCall(true);
            setSuccessCall(serviceParameter.getSearchString());
        } else {
            setSuccessfulCall(false);
            if ((result == null) || (result.trim() == "")) {
                result = "SHELL command execution: Failed";
            }

            setErrorCall(result);
        }
    }

    private String executeCommand() {

        logger.debug("SHELL command execution started");

        StringBuffer outputBuffer = new StringBuffer();

        Process p = null;

        try {

            String[] cmd = new String[3];

            if (GenericUtils.isWindows()) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/c";
            } else {
                cmd[0] = "/bin/sh";
                cmd[1] = "-c";
            }
            cmd[2] = serviceParameter.getCommand();

            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                outputBuffer.append(line);
            }
        } catch (IOException ex) {
            logger.debug("IOException: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.debug("InterruptedException: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
        } finally {
            p.destroy();
        }

        if (outputBuffer.toString().contains(serviceParameter.getSearchString())) {
            logger.debug("SHELL command execution: Success");
        } else {
            logger.debug("SHELL command execution: Failed");
        }

        return outputBuffer.toString().contains(serviceParameter.getSearchString()) ?
               serviceParameter.getSearchString() : outputBuffer.toString();

    }

}
