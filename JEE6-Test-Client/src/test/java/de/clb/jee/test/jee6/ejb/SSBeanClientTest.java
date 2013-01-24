package de.clb.jee.test.jee6.ejb;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;
import de.clb.jee.test.util.XmlFormater;

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
	
	private static final Logger log = Logger.getLogger("SSBeanClientTest");
	
	SimplePrimarySSBeanRemote primarySSB = null;
	
	Properties contextProps = null;
	
	XmlFormater xmlFormater = new XmlFormater();
	
//	private static final String USER = "clichybi";
//	private static final String PASSWORD =  "redhat";
//	private static final String PASSWORD =  "306fcb7b465f6a6018a20a187e9d766c";
	private static final String USER = "test01";
	private static final String PASSWORD =  "785a26aa9cd5ec07d98709dbf1261974";

	
	
	@Before
	public void setUp() throws Exception {
		contextProps = new Properties();
		contextProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		contextProps.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
		contextProps.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
//		contextProps.put(Context.PROVIDER_URL, "remote://10.32.69.198:4447");
		contextProps.put("jboss.naming.client.ejb.context", true);
		
		contextProps.put(Context.SECURITY_PRINCIPAL, USER);
		contextProps.put(Context.SECURITY_CREDENTIALS, PASSWORD);
		
		
		contextProps.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","false");
		contextProps.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS","false");
    	contextProps.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
//    	contextProps.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_MECHANISMS", "PLAIN SASL");       
		contextProps.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");

// settings without influence, because we use: contextProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//		contextProps.put(org.xnio.Options.SSL_ENABLED, "false");
//		contextProps.put(org.xnio.Options.SASL_POLICY_NOANONYMOUS, "false");
//		contextProps.put(org.xnio.Options.SASL_DISALLOWED_MECHANISMS,"JBOSS-LOCAL-USER");
//		contextProps.put(org.xnio.Options.SASL_POLICY_PASS_CREDENTIALS, "true");
//		contextProps.put(org.xnio.Options.SASL_POLICY_NOPLAINTEXT, "false");
//		contextProps.put(org.xnio.Options.SASL_MECHANISMS, "PLAIN,DIGEST-MD5");
//		contextProps.put(org.xnio.Options.SECURE, false);
		
			
		
//		primarySSB = (SimplePrimarySSBeanRemote)  InitialContext.doLookup("JEE6-Test/JEE6-Test-EJB/SimplePrimarySSBean!de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote"); 
		
		primarySSB = (SimplePrimarySSBeanRemote)   new InitialContext(contextProps).lookup("JEE6-Test/JEE6-Test-EJB/SimplePrimarySSBean!de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
		log.info("EJB Remoteinterface instanciated");
	}
	
	@Test
	public void connectTest() throws Exception {
		
		String clearTextPassword=USER+":"+"JEE-Test-Realm"+":"+PASSWORD;
		
		log.info(org.jboss.crypto.CryptoUtil.createPasswordHash("MD5", "hex", null, null, clearTextPassword));
		
		log.info("Before EJB Call");
		ContextDataType response = primarySSB.simpleReply();
	
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertNull(response.getException());
		
		assertEquals("Expected Caller", USER, response.getCaller());
	
	}
	
	@Test
	public void connectSecuredOperationTest() throws Exception {
		
		log.info("Before EJB Call");
		ContextDataType response = primarySSB.simpleSecuredReply();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertNull(response.getException());
		
		assertEquals("Expected Caller", USER, response.getCaller());
		
	}
	
	@Test
	public void delegate2SecuredSecondaryReplyTest() throws Exception {
		
		log.info("Before EJB Call");
		CallSequenceType response = primarySSB.delegate2SecuredSecondaryReply();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertEquals(0, response.getException().size());
		
		assertNotNull(response.getContextDataElement());
		
		assertNotNull(response.getContextDataElement().get(0));
		
		assertEquals("Expected Caller", USER, response.getContextDataElement().get(0).getCaller());
	
	}
	
	private void check2dnServerAvailable()  throws Exception {
		log.info("Check 2nd server available");
		Properties contextProps2 = new Properties();
		
		contextProps2.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		contextProps2.put(Context.INITIAL_CONTEXT_FACTORY, org.jboss.naming.remote.client.InitialContextFactory.class.getName());
		contextProps2.put(Context.PROVIDER_URL, "remote://gpsc-eap6-cluster.coe.muc.redhat.com:4447");
		contextProps2.put("jboss.naming.client.ejb.context", false);	
		contextProps2.put(Context.SECURITY_PRINCIPAL, USER);
		contextProps2.put(Context.SECURITY_CREDENTIALS, PASSWORD);		
		contextProps2.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","false");
		contextProps2.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS","false");
    	contextProps2.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");      
		contextProps2.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
	
		SimplePrimarySSBeanRemote primarySSB2 = (SimplePrimarySSBeanRemote)   new InitialContext(contextProps2).lookup("JEE6-Test-Remote/JEE6-Test-EJB/SimplePrimarySSBean!de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
		log.info("EJB Remoteinterface instanciated");
		ContextDataType response2 = primarySSB2.simpleReply();
		
		assertNotNull(response2);
		log.info("EJB Response: "+xmlFormater.marshalToString(response2));
		
		assertNull(response2.getException());
		
		assertEquals("Expected Caller", USER, response2.getCaller());
		
		log.info("2nd Server up and running! -> Start real test");
	}
	
	@Test
	public void delegate2RemoteReplyTest() throws Exception {
		
//		check2dnServerAvailable();
		
		log.info("Before EJB Call");
		CallSequenceType response = primarySSB.delegate2RemoteReply();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertEquals(0, response.getException().size());
		
		assertNotNull(response.getContextDataElement());
		
		assertNotNull(response.getContextDataElement().get(0));
		
		assertEquals("Expected Caller", USER, response.getContextDataElement().get(0).getCaller());
	
	}

	
	@Test
	public void delegate2SecuredRemoteReplyTest() throws Exception {
		
//		check2dnServerAvailable();
		
		log.info("Before EJB Call");
		CallSequenceType response = primarySSB.delegate2SecuredRemoteReply();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertEquals(0, response.getException().size());
		
		assertNotNull(response.getContextDataElement());
		
		assertNotNull(response.getContextDataElement().get(0));
		
		assertEquals("Expected Caller", USER, response.getContextDataElement().get(0).getCaller());
	
	}
	
	@Test
	public void multipleCallLocalTest() throws Exception {
		
//		check2dnServerAvailable();
		
		log.info("Before EJB Call");
		CallSequenceType response = primarySSB.multipleCallLocal();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertEquals(0, response.getException().size());
		
		assertNotNull(response.getContextDataElement());
		
		assertNotNull(response.getContextDataElement().get(0));
		
		assertEquals("Expected Caller", USER, response.getContextDataElement().get(0).getCaller());
	
	}
	
	@Test
	public void multipleCallRemoteTest() throws Exception {
		
//		check2dnServerAvailable();
		
		log.info("Before EJB Call");
		CallSequenceType response = primarySSB.multipleCallRemote();
		
		assertNotNull(response);
		log.info("EJB Response: "+xmlFormater.marshalToString(response));
		
		assertEquals(0, response.getException().size());
		
		assertNotNull(response.getContextDataElement());
		
		assertNotNull(response.getContextDataElement().get(0));
		
		assertEquals("Expected Caller", USER, response.getContextDataElement().get(0).getCaller());
	
	}
	
	
//	@Test
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
		return InitialContext.doLookup(name);
//		return new InitialContext(properties).lookup(name);
	}

}
