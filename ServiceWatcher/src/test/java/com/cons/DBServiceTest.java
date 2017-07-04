package com.cons;

import com.cons.services.DBService;

import com.cons.services.ServiceParameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class DBServiceTest {
    private DBService ds;
    ServiceParameter s = new ServiceParameter();
    public DBServiceTest() {
        super();
    }
    
    @Before
    public void prepareConfiguration() {
        
        ds = new DBService();
    }
    

    /**
      * Tests a scenario that a valid DB URL is called
      * e.g. 
     */
    @Test
    public void testValidCall() {
        
        //s = init("jdbc:oracle:thin:@shstst-scan.idika.gr:1521/tstdb_taf","epresbkp2","manager1");
        s = init("jdbc:oracle:thin:@shstst-scan.idika.gr:1521/tstdb_taf","epresbkp2","Manager1");
        ds.setServiceParameter(s);
        ds.run();
        Assert.assertTrue(ds.isSuccessfulCall());
        Assert.assertEquals(null, ds.getErrorCall());
    }

    
    /**
      * Tests a scenario that a WRONG CREDENTIALS ON URL is called
      * e.g. 
     */
    @Test
    public void testInvalidLoginCall() {
        s = init("jdbc:oracle:thin:@shstst-scan.idika.gr:1521/tstdb_taf","epresbkp2","Manager");
        ds.setServiceParameter(s);
        ds.run();
        Assert.assertFalse(ds.isSuccessfulCall());
    } 
        
    //    /**
    //      * Tests a scenario that a NON DB URL is called
    //      * e.g. 
    //     */
    //    @Test
    //    public void testInvalidTypeCall() {
    //        ServiceParameter s = new ServiceParameter();
    //        s = init(s,"jdbc:oracle:thin:epresbkp2/Manager1@shstst-scan.idika.gr:1521/tstdb_taf","http");
    //        ds.service(s);
    //        Assert.assertFalse(ds.isSuccessfulCall());
    //    }
    
    //initialization method for ServiceParameter Object    
    public ServiceParameter init(String url, String userName ,String pass){
        ServiceParameter sp = new ServiceParameter();
        sp.setId(1);
        sp.setUsername(userName);
        sp.setPassword(pass);
        sp.setUrl(url);
        sp.setDescription("test");
        sp.setGroup("Internal DB");
        sp.setType("DB");        
        return sp;
    }
    
}
