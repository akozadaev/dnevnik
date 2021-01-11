package ru.tsu.dnevnik.webgui.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Created by Alexey on 20.12.2014.
 */

public class LookupUtil
        implements AutoCloseable {

    public final static String
            NAME_CURRENT_EAR = "dnevnik-ear-1.0-SNAPSHOT",
            NAME_MODULE_DB_1_0 = "db-1.0-SNAPSHOT";

    private InitialContext context;

    public LookupUtil()
            throws LookupException {

        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            throw new LookupException("Can\'t initialize InitialContext", ex);
        }
    }

    public <T> T lookupBean(final String beanName, final Class<T> resultClass)
            throws LookupException {
        return lookupBean(beanName, null, null, resultClass);
    }

    public <T> T lookupBean(final String beanName, final String moduleName, final Class<T> resultClass)
            throws LookupException {
        return lookupBean(beanName, moduleName, null, resultClass);
    }

    public <T> T lookupBean(final String beanName, final String moduleName, final String earName,
                            final Class<T> resultClass)
            throws LookupException {

        String jndi;
        if(moduleName != null)
            jndi = "java:global/" + (earName != null ? earName : NAME_CURRENT_EAR)
                    + "/" + moduleName + "/" + beanName;
        else {
            jndi = "java:module/" + beanName;
            jndi = beanName;
        }

        return lookupJNDI(jndi, resultClass);

    }

    @SuppressWarnings("unchecked")
    public <T> T lookupJNDI(final String jndi, final Class<T> resultClass)
            throws LookupException {

        try {
            Object obj = context.lookup(jndi);
            if(resultClass.isInstance(obj))
                return (T)obj;
            else
                throw new LookupException(
                        String.format("Invalid EJB/resource class \"%s\" (expected \"%s\")",
                                obj != null ? obj.getClass().getCanonicalName() : "null",
                                resultClass.getCanonicalName()
                        ),
                        jndi);
        } catch (NamingException ex) {
            throw new LookupException("Failed to lookup EJB/resource", ex, jndi);
        }

    }

    @Override
    public void close()
            throws LookupException {
        if(context != null) {
            InitialContext tmp = context;
            context = null;
            try {
                tmp.close();
            } catch (NamingException ex) {
                throw new LookupException("Exception close InitialContext", ex);
            }
        }
    }
}
