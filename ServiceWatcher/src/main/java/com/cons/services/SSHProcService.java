package com.cons.services;

import com.cons.utils.GenericUtils;

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
            String result = sendCommand(session);
            logger.debug("SSH Service Success");

            if ((result != null) && (result.contains(serviceParameter.getSearchString()))) {
                setSuccessfulCall(true);
                setSuccessCall(serviceParameter.getSearchString());
            } else {
                setSuccessfulCall(false);
                if ((result == null) || (result.trim() == "")) {
                    result = "SSH Session command execution: Failed";
                }

                setErrorCall(result);
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

    private String sendCommand(Session session) {
        logger.debug("SSH Session sending command to remote PC");

        if ((serviceParameter.getCommand() == null) || (serviceParameter.getSearchString() == null)) {
            return null;
        }

        String command;

        if (GenericUtils.isWindows()) {
            command = "cmd.exe /c " + serviceParameter.getCommand();
        } else {
            command = serviceParameter.getCommand();
        }

        logger.debug("Command: " + command);

        StringBuilder outputBuffer = new StringBuilder();
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            logger.debug("SSH Session opening a channel from current session");
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

        } catch (IOException ex) {
            logger.debug("SSH Session command: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
            return null;
        } catch (JSchException ex) {
            logger.debug("SSH Session command: " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
            return null;
        } finally {
            channel.disconnect();
        }

        logger.debug("Output: " + outputBuffer.toString());

        if (outputBuffer.toString().contains(serviceParameter.getSearchString())) {
            logger.debug("SSH Session command execution: Success");
        } else {
            logger.debug("SSH Session command execution: Failed");
        }

        return outputBuffer.toString().contains(serviceParameter.getSearchString()) ?
               serviceParameter.getSearchString() : outputBuffer.toString();
    }

}
