package de.clb.jee.test.jee6.ejb;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

/**
 * t
 * 
 * @author mzottner
 * 
 */
public class SimplePrimarySSBClientTest {

	@Test
	public void callSimplePrimarySSB() {

		Properties p = new Properties();
		p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		p.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
		p.put("remote.connection.default.host", "127.0.0.1");
		p.put("remote.connection.default.port", "4447");
		p.put("remote.connections", "default");

		// p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
		// "false");
		// p.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED",
		// "false");
		// p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS",
		// "JBOSS-LOCAL-USER");
		// p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT",
		// "false");
		// p.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS",
		// "true");

		// p.put("remote.connection.default.username", username);
		// p.put("remote.connection.default.password", password);

		p.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
		p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		p.put("jboss.naming.client.ejb.context", "true");

		for (SSBean31 ssBean31 : SSBean31.values()) {

			System.out.println("SSB " + ssBean31);

			try {
				Object sb = lookupRemoteStatelessSSB31(p, ssBean31.jndiName);
				for (Method method : ssBean31.interfaceClass.getMethods()) {

					try {
						System.out.println("Invoking " + ssBean31.interfaceClass.getName() + "." + method.getName() + "()");
						method.invoke(sb, (Object[]) null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Looks up and returns the proxy to remote stateless EJB.
	 * 
	 * @param name
	 *            The full JNDI name to lookup.
	 * @return
	 * @throws NamingException
	 */
	public static Object lookupRemoteStatelessSSB31(Properties properties, String name) throws NamingException {
		// jndiProperties.put("remote.connection.default.host", "127.0.0.1");
		final Context context = new InitialContext(properties);
		// let's do the lookup
		return context.lookup(name);
	}

}
