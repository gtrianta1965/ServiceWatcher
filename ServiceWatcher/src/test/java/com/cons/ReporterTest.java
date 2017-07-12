package com.cons;

import com.cons.utils.Reporter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReporterTest {
    
    private Reporter rep;
    
    public ReporterTest() {
        super();
    }
    
    @Before
    public void initReporter(){
        rep = new Reporter();
    }
    
    @Test
    public void testTest(){
        rep.sendMail();
        Assert.assertTrue(true);
    }
}
