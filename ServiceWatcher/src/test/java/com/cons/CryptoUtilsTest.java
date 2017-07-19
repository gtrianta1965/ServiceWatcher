package com.cons;

import com.cons.services.ServiceParameter;
import com.cons.utils.CryptoUtils;

import org.junit.Before;


/**
 * Unit test for CryptoUtils Object.
 */

public class CryptoUtilsTest {

    private CryptoUtils cu;
    private ServiceParameter s;

    public CryptoUtilsTest() {
        super();
    }


    @Before
    public void createHTTPService() {
        cu = new CryptoUtils("", "");
    }

}

