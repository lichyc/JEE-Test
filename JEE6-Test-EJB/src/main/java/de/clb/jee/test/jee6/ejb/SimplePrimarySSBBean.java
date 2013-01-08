package de.clb.jee.test.jee6.ejb;

import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;
import de.clb.jee.test.util.GenericEJBClient;

//@SecurityDomain("mySecurityDomain")
@DeclareRoles({"guestRole", "userRole", "adminRole"})
public @Stateless class SimplePrimarySSBBean implements SimplePrimarySSB {
	
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
			SimplePrimarySSB remoteInterface = (SimplePrimarySSB) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSB");
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
			SimplePrimarySSB remoteInterface = (SimplePrimarySSB) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "de.clb.jee.test.jee6.ejb.SimplePrimarySSBBean", "de.clb.jee.test.jee6.ejb.SimplePrimarySSB");
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
