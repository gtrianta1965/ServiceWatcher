package com.cons.services;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.apache.log4j.Logger;

public class SFTPService extends Service {
    final static Logger logger = Logger.getLogger(SFTPService.class);
    
    public SFTPService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    public SFTPService() {
        super();
    }

    @Override
    public void service() {
        String[] temp = serviceParameter.getUrl().split(":");
        int port = Integer.parseInt(temp[1]);
        String username = serviceParameter.getUsername();
        String password = serviceParameter.getPassword();
        String url = temp[0];
        String protocol = "sftp";
        
        try{
            logger.debug("Starting session.");
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, url, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            logger.debug("Session Started");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel(protocol);
            logger.debug("Starting SFTP Connection");
            sftpChannel.connect();
            logger.debug("Connected!");
            sftpChannel.disconnect();
            session.disconnect();
            logger.debug("SFTP Service Success");
            setSuccessfulCall(true);
        }catch (JSchException jschex){
            logger.debug("SFTP Service error " + jschex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(jschex.getMessage());
        }catch (Exception ex){
            logger.debug("SFTP Service error " + ex.getMessage());
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
        }
    }
}
