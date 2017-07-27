package com.cons;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for CommandLineArgs Object.
 */

public class CommandLineArgsTest {

    private CommandLineArgs cla;

    public CommandLineArgsTest() {
        super();
    }

    @Before
    public void prepareCommandLineArgs() {
        cla = new CommandLineArgs();
    }

    @Test
    public void testEmptyArgs() {
        String[] args = { };
        cla.setCommandLineArgs(args);
        Assert.assertEquals("config.properties", cla.getConfigFile());
        Assert.assertEquals(false, cla.getEncrypt());
        Assert.assertEquals(false, cla.isNoGUI());
        Assert.assertEquals(0, cla.getAutoRefreshTime());
    }

    @Test
    public void testValidConfArg() {
        String[] args = { "-conf:test.properties" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals("test.properties", cla.getConfigFile());
    }

    @Test
    public void testInvalidConfArg() {
        String[] args = { "-conf/home/agregori/test.properties" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals("config.properties", cla.getConfigFile());
    }

    @Test
    public void testValidEncryptArg() {
        String[] args = { "-encrypt" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(true, cla.getEncrypt());
    }

    @Test
    public void testInvalidEncryptArg() {
        String[] args = { "-encrypt:" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(false, cla.getEncrypt());
    }

    @Test
    public void testValidNoGUIArg() {
        String[] args = { "-nogui" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(true, cla.isNoGUI());
    }

    @Test
    public void testInvalidNoGUIArg() {
        String[] args = { "-nogui:" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(false, cla.isNoGUI());
    }

    @Test
    public void testValidDefaultAutoRefreshTimeArg() {
        String[] args = { "-autorefresh" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(1, cla.getAutoRefreshTime());
    }

    @Test
    public void testValidAutoRefreshTimeArg() {
        String[] args = { "-autorefresh:4" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(4, cla.getAutoRefreshTime());
    }

    @Test
    public void testInvalidAutoRefreshTimeArg() {
        String[] args = { "-autorefresh4" };
        cla.setCommandLineArgs(args);
        Assert.assertEquals(0, cla.getAutoRefreshTime());
    }

}

