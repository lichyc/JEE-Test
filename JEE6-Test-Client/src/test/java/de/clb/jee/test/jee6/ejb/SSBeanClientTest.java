package de.clb.jee.test.jee6.ejb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import de.clb.jee.test.util.CallSequenceType;

/**
 * This class calls all methods of all listed EJB 3.1.
 *  
 * @author mzottner
 * 
 */
public class SSBeanClientTest {

	/**
	 * True if the tests should be interrupted after the first exception occurring.
	 */
	protected boolean stopsAfterFirstException = false;

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

//		 p.put("remote.connection.default.username", "test1");
//		 p.put("remote.connection.default.password", "test1");

		p.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
		p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		p.put("jboss.naming.client.ejb.context", "true");
		
		p.put(Context.SECURITY_PRINCIPAL, "test1");
		p.put(Context.SECURITY_CREDENTIALS, "test1");

		// Initializes a result set.
		final List<EJBCallResult> results = new ArrayList<EJBCallResult>();

		// Iterates on all EJBs.
		loop: for (SSBean31 ssBean31 : SSBean31.values()) {

			// Iterates on all public methods.
			for (Method method : ssBean31.interfaceClass.getMethods()) {

				EJBCallResult result = new EJBCallResult(ssBean31, method.getName());
				results.add(result);
				try {

					// EJB lookup
					Object sb = lookupRemoteStatelessSSB31(p, ssBean31.jndiName);
					// Method invocation
					result.setResult(method.invoke(sb, (Object[]) null));
				} catch (Exception e) {
					result.setE(e);
				}

				if (this.stopsAfterFirstException && !result.isSuccessful()) {
					break loop;
				}
			}
		}

		// Display results of all calls.
		for (EJBCallResult callResult : results) {
			System.out.println(callResult);
			System.out.println(callResult.bean.jndiName);
			Object res = callResult.getResult();
			if (res != null) {
				// Show the content of the result
				if (res instanceof CallSequenceType) {
					CallSequenceType cst = (CallSequenceType) res;
					System.out.println(ToStringBuilder.reflectionToString(cst.getCallSequenceElement(), ToStringStyle.MULTI_LINE_STYLE, true, Object.class));
					System.out.println(ToStringBuilder.reflectionToString(cst.getContextDataElement(), ToStringStyle.MULTI_LINE_STYLE, true, Object.class));
				} else {
					System.out.println(ToStringBuilder.reflectionToString(res, ToStringStyle.MULTI_LINE_STYLE, true, Object.class));
				}

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
		return new InitialContext(properties).lookup(name);
	}

}
