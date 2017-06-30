package com.cons;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cons.ServiceParameter;

/**
 * Performs unit tests for the Configuration object.
 */

public class ConfigurationTest {
    private Configuration c;
    public ConfigurationTest() {
        super();
    }
    
    @Before
    public void prepareConfiguration() {
        c = new Configuration();
    }
    
    /**
     * Tests a scenario that a correct number is read for property concurrentThreads
     */
    @Test
    public void testReadConcurrentThreads() {        
        c.init();
        Assert.assertTrue(c.isValid());
        Assert.assertEquals(4, c.getConcurrentThreads());
    }

    /**
     * Tests a scenario that a concurrentThreads property does not exist
     */    
    @Test
    public void testDefaultConcurrentThreads() {
        c.init("config-ut1.properties");
        Assert.assertTrue(c.isValid());
        Assert.assertEquals(5,c.getConcurrentThreads());  //Make sure it is the default
    }
    
    /**
     * Tests number of ServiceParameters
     */    
    @Test
    public void testServiceParametersSize() {
        c.init("config-ut1.properties");
        Assert.assertTrue(c.isValid());
        List s = c.getServiceParameters();
        Assert.assertEquals(4, s.size());  //Make sure we got 4 URL's
    }    
    /**
     * Tests a scenario of reading from a non-existing configuration
     */
    @Test
    public void testNonExistingConfigFile() {
        c.init("Dummy");
        Assert.assertFalse(c.isValid());
        Assert.assertEquals(c.getError(), "Property file Dummy does not exist");
    }
    
    /**
     * Tests a scenario of reading from a non-existing configuration
     */
    @Test
    public void testInvalidConcurrentThreads() {
        c.init("config-ut2.properties");
        Assert.assertFalse(c.isValid());
        Assert.assertEquals(c.getError(), "NumberFormatException");
    }
    
    /**
     * Tests correct double initialization
     */
    @Test
    public void testConfigurationReInitialization() {
        c.init("config-ut1.properties");
        Assert.assertTrue(c.isValid());
        List s = c.getServiceParameters();
        Assert.assertEquals(4, s.size()); 
        Assert.assertEquals(5,c.getConcurrentThreads());

        c.init();
        Assert.assertTrue(c.isValid());
        s = c.getServiceParameters();
        Assert.assertEquals(3, s.size()); 
        Assert.assertEquals(4,c.getConcurrentThreads());        
    }

    /**
     * Tests that checks the id's assigned to ServiceParameters
     */
    @Test
    public void testServiceParametersIds() {
        c.init("config-ut1.properties");
        Assert.assertTrue(c.isValid());
        ArrayList s = (ArrayList)c.getServiceParameters();
        for (int i = 0; i < s.size() ; i++) {
            Assert.assertEquals(i+1,((ServiceParameter)s.get(i)).getId());
        }        
    }    
    
    
}
