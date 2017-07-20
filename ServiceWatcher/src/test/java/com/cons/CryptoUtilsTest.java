package com.cons;

import com.cons.services.ServiceParameter;
import com.cons.utils.CryptoUtils;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for CryptoUtils Object.
 */

public class CryptoUtilsTest {

    private ServiceParameter s;

    public CryptoUtilsTest() {
        super();
    }

    @Test
    public void testValidDecryption() {
        Assert.assertEquals(CryptoUtils.decrypt("AmSQIbkENly0RnG7GtfMwQ=="), "Manager1");
    }

    @Test
    public void testInvalidDecryption() {
        Assert.assertEquals(CryptoUtils.decrypt("Manager1"), null);
    }

}

