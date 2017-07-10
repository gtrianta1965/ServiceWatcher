package com.cons.services;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHService extends Service {
    public SSHService(ServiceParameter serviceParameter) {
        super(serviceParameter);
    }

    public SSHService() {
        super();
    }

    @Override
    public void service() {
        int port = 22;
        String username = "demo";
        String password = "password";
        String url = "test.rebex.net";
        String protocol = "ssh";
        
        try{
            setSuccessfulCall(true);
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, url, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel(protocol);
            sftpChannel.connect();
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
