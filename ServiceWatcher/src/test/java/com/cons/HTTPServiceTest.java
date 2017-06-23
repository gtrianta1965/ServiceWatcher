package com.cons;

import com.cons.services.HTTPService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for HTTPService Object.
 */

public class HTTPServiceTest {
    
    private HTTPService hs;
    private ServiceParameter sp;
    
    public HTTPServiceTest() {
        super();
    }
    

    @Before
        public void createHTTPService() {
            hs = new HTTPService();
        }
    
    
    /**
         * Tests a scenario that a valid url is called
         */
        @Test
        public void testValidCall() {
            ServiceParameter s = hs.init("https://www.google.gr");
            hs.service(s);
            Assert.assertTrue(hs.isSuccessfulCall());
            Assert.assertEquals(null, hs.getErrorCall());
        }
    
    
    /**
         * Tests a scenario that an invalid url is called
         */
        @Test
        public void testInvalidCall() {
            ServiceParameter s = hs.init("https://www.google.grrr");
            hs.service(s);
            Assert.assertFalse(hs.isSuccessfulCall());
            Assert.assertEquals("bad url", hs.getErrorCall());
        }
    
    
    /**
         * Tests a scenario that is a 404 url is called
         */
        @Test
        public void testValidCallWithNoResult() {
            ServiceParameter s = hs.init("192.168.42.63:7003/test-sso/faces/Login");
            hs.service(s);
            Assert.assertFalse(hs.isSuccessfulCall());
            Assert.assertEquals("bad url", hs.getErrorCall());
        }
    
    
}
