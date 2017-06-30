package com.cons;

import com.cons.services.DBService;

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
        
        s = init(s,"jdbc:oracle:thin:epresbkp2/Manager1@shstst-scan.idika.gr:1521/tstdb_taf","DB");
        ds.setServiceParamater(s);
        ds.service();
        Assert.assertTrue(ds.isSuccessfulCall());
        Assert.assertEquals(null, ds.getErrorCall());
    }

    
    /**
      * Tests a scenario that a WRONG CREDENTIALS ON URL is called
      * e.g. 
     */
    @Test
    public void testInvalidLoginCall() {
        s = init(s,"jdbc:oracle:thin:epresbkp2/manager1@shstst-scan.idika.gr:1521/tstdb_taf","DB");
        ds.setServiceParamater(s);
        ds.service();
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
    public ServiceParameter init(ServiceParameter sp,String url, String type){ 
        sp.setUrl(url);
        sp.setDescription("test");
        sp.setGroup("test2");
        sp.setType(type);
        sp.setSearchString("test");
        return sp;
    }
    
}
