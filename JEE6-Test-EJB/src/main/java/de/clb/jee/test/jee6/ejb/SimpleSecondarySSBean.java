package de.clb.jee.test.jee6.ejb;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.transaction.SystemException;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;
import de.clb.jee.test.util.GenericEJBClient;

/**
 * Session Bean implementation class SimpleSecondarySSBBean
 */
@Stateless
@LocalBean
@DeclareRoles({"guestRole", "userRole", "adminRole"})
@TransactionManagement(TransactionManagementType.BEAN)
public class SimpleSecondarySSBean implements SimpleSecondarySSBeanRemote {
	
	@Resource
	private SessionContext sessionContext;

    /**
     * Default constructor. 
     */
    public SimpleSecondarySSBean() {
        // TODO Auto-generated constructor stub
    }
    
    private ContextDataType getContextData(SessionContext ctx, String clazzName, String operationName) {
		ContextDataType result = new ContextDataType();
		
		result.setCallTime(System.currentTimeMillis());
		result.setCaller(ctx.getCallerPrincipal().getName());
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
		
		return getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "simpleReply");
	}

	@RolesAllowed({"userRole", "adminRole"})
	public ContextDataType simpleSecuredReply() {
		
		return getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "simpleSecuredReply");
	}

	@PermitAll
	public CallSequenceType simpleDelegate() {
		CallSequenceType result = new CallSequenceType();
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "simpleDelegate"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
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
		
		result.getContextDataElement().add(getContextData(sessionContext, "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBean", "simpleSecuredDelegate"));
		
		try {
			SimpleSecondarySSBeanRemote remoteInterface = (SimpleSecondarySSBeanRemote) GenericEJBClient.lookupRemoteStatelessSSB31("JEE6-Test", "JEE6-Test-EJB", "", "SimpleSecondarySSBean", "de.clb.jee.test.jee6.ejb.SimpleSecondarySSBeanRemote");
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

}
