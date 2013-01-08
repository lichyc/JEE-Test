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
     * @return
     * @throws NamingException
     */
    public static Object lookupRemoteStatelessSSB31(
    		// The app name is the application name of the deployed EJBs. This is typically the ear name
            // without the .ear suffix. However, the application name could be overridden in the application.xml of the
            // EJB deployment on the server.
            // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
    		String appName, 
    		// This is the module name of the deployed EJBs on the server. This is typically the jar name of the
            // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
            // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
            // jboss-as-ejb-remote-app
    		String moduleName,
    		// AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
            // our EJB deployment, so this is an empty string
    		String distinctName,
    		// The EJB name which by default is the simple class name of the bean implementation class
            //final String beanName = SimpleSsb.class.getSimpleName();
    		String beanName, 
    		// the remote view fully qualified class name
    		String viewClassName) throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        // let's do the lookup
        return context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
    }

}
