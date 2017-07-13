package com.cons;

import com.cons.utils.DateUtils;

import java.util.Date;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
    public DateUtilsTest() {
        super();
    }
    
    @Test
    public void testTimeDifference() throws InterruptedException {        
        Date d1;
        Date d2;

        d1 = new Date();
        Thread.sleep(1000);
        d2 = new Date();
        
        Long diff = DateUtils.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
        Assert.assertEquals(new Long(1000), diff,10);
        
    }
    
    @Test
    public void testAddTime() throws InterruptedException {        
        Date d1;
        Date d2;
        
        d1 = new Date();        
        d2 = DateUtils.addMinutesToDate(d1, 22);
        
        
        Long diff = DateUtils.getDateDiff(d1, d2, TimeUnit.MINUTES);
        Assert.assertEquals(new Long(22), diff);
    }
    
}
