package com.cons.services;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPService extends Service {
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
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, url, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel(protocol);
            sftpChannel.connect();
            sftpChannel.disconnect();
            session.disconnect();
            setSuccessfulCall(true);
        }catch (JSchException jschex){
            setSuccessfulCall(false);
            setErrorCall(jschex.getMessage());
        }catch (Exception ex){
            setSuccessfulCall(false);
            setErrorCall(ex.getMessage());
        }
    }
}
