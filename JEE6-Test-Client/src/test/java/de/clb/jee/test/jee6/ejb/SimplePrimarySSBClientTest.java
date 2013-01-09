package de.clb.jee.test.jee6.ejb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

	final boolean stopsAfterFirstException = true;

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

		final List<EJBCallResult> results = new ArrayList<EJBCallResult>();

		// Iterates on all EJBs.
		loop: for (SSBean31 ssBean31 : SSBean31.values()) {

			// Iterates on all public methods.
			for (Method method : ssBean31.interfaceClass.getMethods()) {

				// Initializes a result set.
				EJBCallResult result = new EJBCallResult(ssBean31, method.getName());
				results.add(result);
				try {

					// EJB lookup
					Object sb = lookupRemoteStatelessSSB31(p, ssBean31.jndiName);
					// Method invocation
					method.invoke(sb, (Object[]) null);

				} catch (Exception e) {
					result.setE(e);
					if (stopsAfterFirstException) {
						break loop;
					}
				}
			}
		}

		// Display results
		for (EJBCallResult result : results) {
			System.out.println(result);
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
