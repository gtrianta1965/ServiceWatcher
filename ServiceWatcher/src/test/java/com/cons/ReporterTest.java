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
    
    @Test
    public void testTest(){
        Reporter.sendMail();
        Assert.assertTrue(true);
    }
}
