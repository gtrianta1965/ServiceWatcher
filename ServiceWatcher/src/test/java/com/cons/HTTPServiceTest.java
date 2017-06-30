package com.cons;

import com.cons.services.HTTPService;

import com.cons.utils.SWConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for HTTPService Object.
 */

public class HTTPServiceTest {
    
    private HTTPService hs;
    
    public HTTPServiceTest() {
        super();
    }
    

    @Before
        public void createHTTPService() {
            hs = new HTTPService();
        }
    
    
    /**
      * Tests a scenario that a valid url is called
      * e.g. https://www.google.gr
     */
    @Test
    public void testValidCall() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"http://www.idika.gr","H");
        hs.setServiceParamater(s);
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    
    /**
      * Tests a scenario that a valid url is called but the search string not found
      * e.g. https://www.google.gr and setSearchString= "�����������"
     */
    @Test
    public void unsuccessfulSearch() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"http://www.idika.gr","�������������");
        hs.setServiceParamater(s);
        Assert.assertFalse(hs.isSuccessfulCall());
        Assert.assertEquals("Search String not found in response", hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that a valid url is called
      * e.g. https://www.google.gr and setSearchString= "���������"
     */
    @Test
    public void successfulSearch() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"http://www.idika.gr","H");
        hs.setServiceParamater(s);
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that a valid url is called but the search string is empty
      * e.g. https://www.google.gr and setSearchString= null
     */
    @Test
    public void testValidCallNullSearch() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"http://www.idika.gr","");
        hs.setServiceParamater(s);
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that an invalid url is called
      * e.g. https://www.google.grrr
     */
    @Test
    public void testInvalidCall() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"https://www.google.grrr","���������");
        hs.setServiceParamater(s);
        String errorMsg = hs.getErrorCall();
        Assert.assertFalse(hs.isSuccessfulCall());
        System.out.println(hs.getErrorCall());
        Assert.assertTrue(errorMsg.contains(SWConstants.UNSUCCESSFUL_RESPONSE_MSG));
        
    }
    
    
    /**
     * Tests a scenario that is a 404 url is called
     * e.g. 192.168.42.63:7003/test-sso/faces/Login
     */
    @Test
    public void testValidCallWithNoResult() {
        ServiceParameter s = new ServiceParameter();
        s = init(s,"http://www.oracle.com","���������");
        hs.setServiceParamater(s);
        String errorMsg = hs.getErrorCall();
        Assert.assertFalse(hs.isSuccessfulCall());
        Assert.assertTrue(errorMsg.contains(SWConstants.UNSUCCESSFUL_RESPONSE_MSG));
        
    }
    
        //initialization method for ServiceParameter Object    
        public ServiceParameter init(ServiceParameter sp,String url, String searchString){ 
            sp.setUrl(url);
            sp.setDescription("test");
            sp.setGroup("test2");
            sp.setType("test3");
            sp.setSearchString(searchString);
            return sp;
        }
    
    
}

