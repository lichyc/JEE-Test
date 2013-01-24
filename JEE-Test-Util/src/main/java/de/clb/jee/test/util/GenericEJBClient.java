/**
 * 
 */
package de.clb.jee.test.util;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author clichybi
 *
 */
public class GenericEJBClient {
	
	static Logger log = Logger.getLogger("GenericEJBClient") ;
	
	/**
     * Looks up and returns the proxy to remote stateless EJB
     *
     * @param appName The app name is the application name of the deployed EJBs. This is typically the ear name
            without the .ear suffix. However, the application name could be overridden in the application.xml of the
            EJB deployment on the server.
            Since we haven't deployed the application as a .ear, the app name for us will be an empty string.
     * @param moduleName This is the module name of the deployed EJBs on the server. This is typically the jar name of the
            EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
            In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
            jboss-as-ejb-remote-app.
     * @param distinctName AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
            our EJB deployment, so this is an empty string.
     * @param beanName The EJB name which by default is the simple class name of the bean implementation class.
     * @param viewClassName the remote view fully qualified class name.
     * @return
     * @throws NamingException
     */
    public static Object lookupRemoteStatelessSSB31(String appName, String moduleName, String distinctName, String beanName, String viewClassName) throws NamingException {
        final Hashtable<String,String> jndiProperties = new Hashtable<String,String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
        jndiProperties.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
        jndiProperties.put("jboss.naming.client.ejb.context", "true");
        
        jndiProperties.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","false");
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS","false");
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");  
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
        
        final Context context = new InitialContext(jndiProperties);
        // let's do the lookup
        if (null != distinctName) {
        	return context.lookup(appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
        } else {
        	return context.lookup(appName + "/" + moduleName + "/" + beanName + "!" + viewClassName);
        }
    }
    
    /**
     * Looks up and returns the proxy to remote stateless EJB
     *
     * @param serverId identifier for server to be looked-up in properties.
     * @param appName The app name is the application name of the deployed EJBs. This is typically the ear name
            without the .ear suffix. However, the application name could be overridden in the application.xml of the
            EJB deployment on the server.
            Since we haven't deployed the application as a .ear, the app name for us will be an empty string.
     * @param moduleName This is the module name of the deployed EJBs on the server. This is typically the jar name of the
            EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
            In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
            jboss-as-ejb-remote-app.
     * @param distinctName AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
            our EJB deployment, so this is an empty string.
     * @param beanName The EJB name which by default is the simple class name of the bean implementation class.
     * @param viewClassName the remote view fully qualified class name.
     * @return
     * @throws NamingException
     */
    public static Object lookupRemoteStatelessSSB31(String serverId, String appName, String moduleName, String distinctName, String beanName, String viewClassName) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        
        PropertiesManager pm = PropertiesManager.getInstance("de.clb.jee.test");
        
        pm.printInMemoryProperties();
        
        String providerURL = pm.getProperty("remote.connection."+serverId+".provider.url");
        String securityPrincipal = pm.getProperty("remote.connection."+serverId+".security.principal");
        String securityCredential = pm.getProperty("remote.connection."+serverId+".security.credential");
        
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
        jndiProperties.put("jboss.naming.client.ejb.context", "true");
        
//        jndiProperties.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","false");
//        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS","false");
//        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");  
//        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
        
//        if (null != providerURL) {
//        	jndiProperties.put(Context.PROVIDER_URL, providerURL);
//        	log.info("PROVIDER_URL is use: " + providerURL);
//        }
        if (null != securityPrincipal) {
        	jndiProperties.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        	log.info("SECURITY_PRINCIPAL is use: " + securityPrincipal);
        }
        if (null != securityCredential) {
        	jndiProperties.put(Context.SECURITY_CREDENTIALS, securityCredential);
        	log.info("SECURITY_CREDENTIALS is use: " + securityCredential);
        }
        	
        final Context context = new InitialContext(jndiProperties);
        // let's do the lookup
        if (null != distinctName) {
        	return context.lookup("ejb:"+appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
        } else {
        	return context.lookup("ejb:"+appName + "/" + moduleName + "//" + beanName + "!" + viewClassName);
        }
    }

}
