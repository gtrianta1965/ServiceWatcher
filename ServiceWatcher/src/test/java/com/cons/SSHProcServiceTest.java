package com.cons;

import com.cons.services.SSHProcService;
import com.cons.services.ServiceParameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SSHProcServiceTest {
    public SSHProcServiceTest() {
        super();
    }

    private SSHProcService sshps;
    private ServiceParameter serviceParameter;

    @Before
    public void prepareConfiguration() {
        sshps = new SSHProcService();
    }


    @Test
    public void testValidCall() {
        serviceParameter = init("10.172.116.85:22", "oracle", "Manager1");
        sshps.setServiceParameter(serviceParameter);
        sshps.run();
        Assert.assertTrue(sshps.isSuccessfulCall());
        Assert.assertEquals(null, sshps.getErrorCall());
    }

    @Test
    public void testInvalidCall() {
        serviceParameter = init("labgr2.gr.oracle.com:22", "oracle", "Manager2");
        sshps.setServiceParameter(serviceParameter);
        sshps.run();
        Assert.assertFalse(sshps.isSuccessfulCall());
        Assert.assertNotNull(sshps.getErrorCall());
    }

    public ServiceParameter init(String url, String username, String password) {
        ServiceParameter sp = new ServiceParameter();
        sp.setId(1);
        sp.setUrl(url);
        sp.setUsername(username);
        sp.setPassword(password);
        sp.setType("SSH");
        sp.setCommand("echo $USER");
        sp.setSearchString("oracle");
        return sp;
    }
}
