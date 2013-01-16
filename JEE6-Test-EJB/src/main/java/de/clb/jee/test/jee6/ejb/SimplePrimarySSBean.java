package de.clb.jee.test.jee6.ejb;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;
import de.clb.jee.test.util.GenericEJBClient;

@DeclareRoles({"guestRole", "userRole", "adminRole"})
@TransactionManagement(TransactionManagementType.CONTAINER)
public @Stateless class SimplePrimarySSBean extends BaseSSBean implements SimplePrimarySSBeanRemote{
	
	public static final String REMOTE_EAP6_SERVER_ID = "partnerEAP6";
	public static final String REMOTE_EAP5_SERVER_ID = "partnerEAP5";
	
	@Resource
	private SessionContext sessionContext;
	
	@Resource
	TransactionSynchronizationRegistry txReg;
	
	@EJB
	SimpleSecondarySSBeanRemote secondBean;

	@PermitAll
	public ContextDataType simpleReply() {
		
		return getContextData(sessionContext, txReg, "simpleReply");
	}

	@RolesAllowed({"userRole", "adminRole"})
	public ContextDataType simpleSecuredReply() {
		
		return getContextData(sessionContext, txReg, "simpleSecuredReply");
	}

	@PermitAll
	public CallSequenceType simpleDelegate() {
		CallSequenceType result = new CallSequenceType();
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "simpleDelegate"));
		try {
			//ejb:JEE6-Test/JEE6-Test-EJB/SimplePrimarySSBean!de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", null, "SimplePrimarySSBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
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
		
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "simpleSecuredDelegate"));
		
		try {
			SimplePrimarySSBeanRemote remoteInterface = (SimplePrimarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", null, "SimplePrimarySSBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleSecuredReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@PermitAll
	public CallSequenceType delegate2SecondaryReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "delegate2SecondaryReply"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", null, "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
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
	public CallSequenceType delegate2SecuredSecondaryReply() {
		CallSequenceType result = new CallSequenceType();
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "delegate2SecuredSecondaryReply"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", null, "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
			CallSequenceType subResult = new CallSequenceType();
			result.getCallSequenceElement().add(subResult);
			subResult.getContextDataElement().add(remoteInterface.simpleSecuredReply());
		} catch (NamingException e) {
			result.getException().add(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	@PermitAll
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ContextDataType replyWithSetRollbackOnly() {
		ContextDataType result = getContextData(sessionContext, txReg, "replyWithSetRollbackOnly");
		
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

	@PermitAll
	public CallSequenceType delegate2RemoteReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "delegate2RemoteReply"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31(REMOTE_EAP6_SERVER_ID,"JEE6-Test", "JEE6-Test-EJB", null, "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
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
	public CallSequenceType delegate2SecuredRemoteReply() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "delegate2SecuredRemoteReply"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31(REMOTE_EAP6_SERVER_ID,"JEE6-Test", "JEE6-Test-EJB", null, "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
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
