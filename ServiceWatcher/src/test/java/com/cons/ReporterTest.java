package com.cons;

import com.cons.utils.Reporter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ReporterTest {
        
    public ReporterTest() {
        super();
    }
    
    @Test
    public void testTest(){
        List<String> log = new ArrayList<String>();
        log.add("Server: 127.0.0.1 - DOWN");
        log.add("Server: www.google.com - UP");
        log.add("Server: www.crazycows.gr - UP");
        
        String [] to = new String[]{"alexkalavitis@gmail.com"};
        //Reporter.sendMail(new String[]{"alexkalavitis@gmail.com"},log);
        Assert.assertTrue(true);
    }
}
