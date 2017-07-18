package com.cons;

import com.cons.services.LDAPService;
import com.cons.services.ServiceParameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LDAPServiceTest {
    private LDAPService ldapService;
    ServiceParameter s = new ServiceParameter();

    public LDAPServiceTest() {
        super();
    }

    @Before
    public void prepareConfiguration() {
        ldapService = new LDAPService();
    }


    /**
     * Tests a scenario that a valid LDAP Bind operation is completed sucessfully
     *
     */
    @Test
    public void testValidBind() {

        //s = init("jdbc:oracle:thin:@shstst-scan.idika.gr:1521/tstdb_taf","epresbkp2","manager1");
        s = init("ldap://labtd.gr.oracle.com:1389", "cn=\"Directory Manager\"", "Manager1");
        ldapService.setServiceParameter(s);
        ldapService.run();
        Assert.assertTrue(ldapService.isSuccessfulCall());
        Assert.assertEquals(null, ldapService.getErrorCall());
    }


    /**
     * Tests a scenario that a LDAP Bind operation is not completed sucessfully
     *
     */
    @Test
    public void testInvalidBind() {
        s = init("ldap://labtd.gr.oracle.com:1389", "cn=\"Directory Manager\"", "Manager2");
        ldapService.setServiceParameter(s);
        ldapService.run();
        Assert.assertFalse(ldapService.isSuccessfulCall());
    }


    //initialization method for ServiceParameter Object
    public ServiceParameter init(String url, String userName, String pass) {
        ServiceParameter sp = new ServiceParameter();
        sp.setId(1);
        sp.setUsername(userName);
        sp.setPassword(pass);
        sp.setUrl(url);
        sp.setDescription("LDAP Test Bind");
        sp.setType("LDAP");
        return sp;
    }

}
