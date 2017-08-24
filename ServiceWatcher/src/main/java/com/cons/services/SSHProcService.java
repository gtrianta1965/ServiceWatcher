package com.cons.services;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class SSHProcService extends Service {
    final static Logger logger = Logger.getLogger(SSHProcService.class);
    private String output;

    public SSHProcService() {
        super();
    }

    public SSHProcService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    @Override
    public void service() {

        String url = "";
        int port = 22;

        if (serviceParameter.getUrl()
                            .split(":")
                            .length == 2) {
            url = serviceParameter.getUrl().split(":")[0];
            port = Integer.parseInt(serviceParameter.getUrl().split(":")[1]);
        } else {
            url = serviceParameter.getUrl();
        }

        Session session = null;
        try {
            logger.debug("Starting SSH session.");
            JSch jsch = new JSch();
            session = jsch.getSession(serviceParameter.getUsername(), url, port);
            session.setPassword(serviceParameter.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            logger.debug("SSH Session Started");
            logger.debug("Starting SSH Connection");
            logger.debug("Connected!");
            boolean result = sendCommand(session, serviceParameter.getCommand());
            logger.debug("SSH Service Success");

            if (!result) {
                setSuccessfulCall(false);
                setErrorCall(getOutput());
            } else {
                setSuccessfulCall(true);
                setSuccessCall(serviceParameter.getSearchString());
            }

        } catch (JSchException jschex) {
            logger.debug("SSH Service error " + jschex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(jschex.getMessage());
        } catch (Exception ex) {
            logger.debug("SSH Service error " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
        } finally {
            session.disconnect();
        }
    }

    private boolean sendCommand(Session session, String command) {
        logger.debug("SSH Session sending command to remote PC");

        if (command == null) {
            return false;
        }

        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = session.openChannel("exec");
            logger.debug("SSH Session opening a channel from current session");
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        } catch (IOException ex) {
            logger.debug("SSH Session command: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
            return false;
        } catch (JSchException ex) {
            logger.debug("SSH Session command: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
            return false;
        }

        logger.debug("SSH Session command execution: Success");

        setOutput(outputBuffer.toString());

        return outputBuffer.toString().contains(serviceParameter.getSearchString());
    }

    private void setOutput(String output) {
        this.output = output;
    }

    private String getOutput() {
        return output;
    }
}
