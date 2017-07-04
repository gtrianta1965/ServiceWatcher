package com.cons;

import com.cons.services.HTTPService;

import com.cons.services.ServiceParameter;
import com.cons.utils.SWConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for HTTPService Object.
 */

public class HTTPServiceTest {
    ServiceParameter s;
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
        s = init("http://www.idika.gr","H");
        hs.setServiceParameter(s);
        hs.run();
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    
    /**
      * Tests a scenario that a valid url is called but the search string not found
      * e.g. https://www.google.gr and setSearchString= "Αναζήτησηηη"
     */
    @Test
    public void unsuccessfulSearch() {
        s = init("http://www.idika.gr","Ηλεκτρονικήηη");
        hs.setServiceParameter(s);
        hs.run();
        Assert.assertFalse(hs.isSuccessfulCall());
        Assert.assertEquals("Search String not found in response", hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that a valid url is called
      * e.g. https://www.google.gr and setSearchString= "Αναζήτηση"
     */
    @Test
    public void successfulSearch() {
        s = init("http://www.idika.gr","H");
        hs.setServiceParameter(s);
        hs.run();
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that a valid url is called but the search string is empty
      * e.g. https://www.google.gr and setSearchString= null
     */
    @Test
    public void testValidCallNullSearch() {
        s = init("http://www.idika.gr","");
        hs.setServiceParameter(s);    
        hs.run();
        Assert.assertTrue(hs.isSuccessfulCall());
        Assert.assertEquals(null, hs.getErrorCall());
    }
    
    /**
      * Tests a scenario that an invalid url is called
      * e.g. https://www.google.grrr
     */
    @Test
    public void testInvalidCall() {
        s = init("https://www.googlesdsds.grrr","Αναζήτηση");
        hs.setServiceParameter(s);
        hs.run();
        String errorMsg = hs.getErrorCall();
        Assert.assertFalse(hs.isSuccessfulCall());
        System.out.println(hs.getErrorCall());
        Assert.assertTrue(errorMsg.contains(SWConstants.GENERIC_EXCEPTION_MSG));
    }
    
    
    /**
     * Tests a scenario that is a 404 url is called
     * e.g. 192.168.42.63:7003/test-sso/faces/Login
     */
    @Test
    public void testValidCallWithNoResult() {
        s = init("http://www.idika.gr","sdfdsfdsfds");
        hs.setServiceParameter(s);
        hs.run();
        String errorMsg = hs.getErrorCall();
        Assert.assertFalse(hs.isSuccessfulCall());
        Assert.assertTrue(errorMsg.contains(SWConstants.SEARCH_STRING_NOT_FOUND_MSG));
        
    }
    
        //initialization method for ServiceParameter Object    
        public ServiceParameter init(String url, String searchString){ 
            ServiceParameter sp = new ServiceParameter();
            sp.setId(1);
            sp.setUrl(url);
            sp.setDescription("test");
            sp.setGroup("test2");
            sp.setType("test3");
            sp.setSearchString(searchString);
            return sp;
        }
    
    
}

