package de.clb.jee.test.jee6.ejb;

import javax.naming.Context;

import org.junit.Assert;
import org.junit.Test;

import de.clb.jee.test.util.GenericEJBClient;

/**
 * 
 * @author mzottner
 * 
 */
public class SimplePrimarySSBClientTest {

	@Test
	public void callSimplePrimarySSB() {

		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
		System.setProperty(Context.PROVIDER_URL, "remote://localhost:4447");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		System.setProperty("jboss.naming.client.ejb.context", "true");

		// env.put( "jboss.naming.client.ejb.context", true );
		// env.put( Context.SECURITY_PRINCIPAL, USER );
		// env.put( Context.SECURITY_CREDENTIALS, PASS );

		try {
			SimplePrimarySSBeanRemote spSB = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("primaryEAP6", "JEE6-Test", "JEE6-Test-EJB", "", "SimplePrimarySSBean",
					"de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");

			System.out.println("simpleReply");
			spSB.simpleReply();

			System.out.println("simpleDelegate");
			spSB.simpleDelegate();

		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertNull(e);
		}

	}

}
