package com.cons.services;

import com.cons.utils.SWConstants;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;


/**
 * Helper Class for LDAP Bind Operation
 */
public class LDAPService extends Service {

    public LDAPService() {
        super();
    }

    public LDAPService(ServiceParameter sp) {
        super(sp);
    }


    @Override
    public void service() {

        LdapContext ctx = null;
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            env.put(Context.SECURITY_PRINCIPAL, serviceParameter.getUsername()); //user DN is the right name
            env.put(Context.SECURITY_CREDENTIALS, serviceParameter.getPassword());
            env.put(Context.PROVIDER_URL, serviceParameter.getUrl());
            ctx = new InitialLdapContext(env, null);

            this.setSuccessfulCall(true);

        } catch (NamingException ex) {
            this.setSuccessfulCall(false);
            this.setErrorCall(SWConstants.SERVICE_LDAP_ERROR_MSG + ex.getMessage());
        }
    }

}
