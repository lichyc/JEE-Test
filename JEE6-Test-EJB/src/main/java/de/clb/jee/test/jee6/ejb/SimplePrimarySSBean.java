package de.clb.jee.test.jee6.ejb;

import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;
import de.clb.jee.test.util.GenericEJBClient;

//@SecurityDomain("mySecurityDomain")
@DeclareRoles({"guestRole", "userRole", "adminRole"})
public @Stateless class SimplePrimarySSBean implements SimplePrimarySSBeanRemote {
	
	public static final String REMOTE_EAP6_SERVER_ID = "partnerEAP6";
	public static final String REMOTE_EAP5_SERVER_ID = "partnerEAP5";
	
	@Resource
	private SessionContext sessionContext;
	
	
	private ContextDataType getContextData(SessionContext ctx, String clazzName, String operationName) {
		ContextDataType result = new ContextDataType();
		
		try {
			result.setCallTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			result.setException(e.getMessage());
			e.printStackTrace();
		}
		result.setCaller(ctx.getCallerIdentity().getName());
		if(null != ctx.getUserTransaction()) {
			result.setHasTransaction(true);
			
		} else {
			result.setHasTransaction(false);
		}
		result.setEJBName(clazzName);
		result.setOperation(operationName);		
		return result;
		
	}

	@PermitAll
	public ContextDataType simpleReply() {
		
		return getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "simpleReply");
	}

	@RolesAllowed({"userRole", "adminRole"})
	public ContextDataType simpleSecuredReply() {
		
		return getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "simpleSecuredReply");
	}

	@PermitAll
	public CallSequenceType simpleDelegate() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "simpleDelegate"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@RolesAllowed({"userRole", "adminRole"})
	public CallSequenceType simpleSecuredDelegate() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "simpleSecuredDelegate"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleSecuredReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	@PermitAll
	public CallSequenceType delegate2SecondaryReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "delegate2SecondaryReply"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	@RolesAllowed({"userRole", "adminRole"})
	public CallSequenceType delegate2SecuredSecondaryReply() {
CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "delegate2SecondaryReply"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleSecuredReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	@PermitAll
	public ContextDataType replyWithSetRollbackOnly() {
		ContextDataType result = getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "replyWithSetRollbackOnly");
		
		try {
			sessionContext.getUserTransaction().setRollbackOnly();
		} catch (IllegalStateException e) {
			result.setException(e.getMessage());
			e.printStackTrace();
		} catch (SystemException e) {
			result.setException(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@PermitAll
	public CallSequenceType delegate2RemoteReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "delegate2RemoteReply"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31(REMOTE_EAP6_SERVER_ID,"JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@Override
	@RolesAllowed({"userRole", "adminRole"})
	public CallSequenceType delegate2SecuredRemoteReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "delegate2RemoteReply"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31(REMOTE_EAP6_SERVER_ID,"JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleSecuredReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}
	
}
