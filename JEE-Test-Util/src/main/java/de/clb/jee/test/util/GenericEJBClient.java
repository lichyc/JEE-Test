/**
 * 
 */
package de.clb.jee.test.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author clichybi
 *
 */
public class GenericEJBClient {
	
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
        final Context context = new InitialContext(jndiProperties);
        // let's do the lookup
        return context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
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
        
        String providerURL = pm.getProperty("remote.connection."+serverId+".provider.url");
        String securityPrincipal = pm.getProperty("remote.connection."+serverId+".security.principal");
        String securityCredential = pm.getProperty("remote.connection."+serverId+".security.credential");
        
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
        if (null != providerURL) {
        	jndiProperties.put(Context.PROVIDER_URL, providerURL);
        }
        if (null != securityPrincipal) {
        	jndiProperties.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        }
        if (null != securityCredential) {
        	jndiProperties.put(Context.SECURITY_CREDENTIALS, securityCredential);
        }
        	
        final Context context = new InitialContext(jndiProperties);
        // let's do the lookup
        return context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
    }

}
