package com.cons;

import com.cons.services.SocketService;

import com.cons.services.ServiceParameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test fir PingService Object.
 */

public class SocketServiceTest {
    
    private SocketService ps;
    private ServiceParameter s;
    
    public SocketServiceTest() {
        super();
    }
    
    @Before
    public void createSocketService(){
        ps = new SocketService();
    }
    
    /**
     * Test if ping returns with loopback ip
     * With loop back ip = "127.0.0.1"
     */
    @Test
    public void pingLoopback(){
        s = init("127.0.0.1");
        ps.setServiceParameter(s);
        ps.run();
        Assert.assertTrue(ps.isSuccessfulCall());
        Assert.assertEquals(null, ps.getErrorCall());
    }
    
    /**
     * Test if invalid ping returns invalid
     * With test url/ip = "fmi342jf982ffn3"
     */
    @Test
    public void pingInvalid(){
        s = init("fmi342jf982ffn3");
        ps.setServiceParameter(s);
        ps.run();
        Assert.assertFalse(ps.isSuccessfulCall());
        Assert.assertNotNull(ps.getErrorCall());
    }
    
    
    //TODO uncomment when parameters IP and Port are implemented in serviceParameters.
    /**
     * Test valid URL ping.
     * With URL = www.google.com
     */
//    @Test
//    public void wwwCheck(){
//        s = init("http://wwww.google.com");
//        ps.setServiceParameter(s);
//        ps.run();
//        Assert.assertTrue(ps.isSuccessfulCall());
//        Assert.assertEquals(null, ps.getErrorCall());
//    }
    
    /**
     * Test invalid URL ping.
     * With URL = asdasdwas
     */
//    @Test
//    public void wwwCheck(){
//        s = init("asdasdwas");
//        ps.setServiceParameter(s);
//        ps.run();
//        Assert.assertTrue(ps.isSuccessfulCall());
//        Assert.assertEquals(null, ps.getErrorCall());
//    }
    
    //initialization method for ServiceParameter Object 
    public ServiceParameter init(String url){ 
        ServiceParameter sp = new ServiceParameter();
        sp.setId(1);
        sp.setUrl(url);
        sp.setDescription("test");
        sp.setGroup("test2");
        sp.setType("test3");
        return sp;
    }
}
