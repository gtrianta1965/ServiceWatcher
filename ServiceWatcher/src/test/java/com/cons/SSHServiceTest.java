package com.cons;

import com.cons.services.SSHService;

import com.cons.services.ServiceParameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SSHServiceTest {
    public SSHServiceTest() {
        super();
    }
    
    private SSHService sshs;
    private ServiceParameter s;
    
    @Before
    public void createSHHService() {
        sshs = new SSHService();
    }
    
    /**
      * Valid SSH connection test on a public server with:
      * URL = est.rebex.net
      * PORT = 22
      * USERNAME = demo
      * PASSWORD = password
     */
    @Test
    public void testValidCall() {
        s = init("est.rebex.net:22", "demo", "password");
        sshs.setServiceParameter(s);
        sshs.run();
        System.out.println(sshs.getErrorCall());
        Assert.assertTrue(sshs.isSuccessfulCall());
        Assert.assertEquals(null, sshs.getErrorCall());
    }
    
    //initialization method for ServiceParameter Object 
    public ServiceParameter init(String url, String username, String password){ 
        ServiceParameter sp = new ServiceParameter();
        sp.setId(1);
        sp.setUrl(url);
        sp.setUsername(username);
        sp.setPassword(password);
        sp.setDescription("test");
        sp.setGroup("test2");
        sp.setType("test3");
        return sp;
    }
}
