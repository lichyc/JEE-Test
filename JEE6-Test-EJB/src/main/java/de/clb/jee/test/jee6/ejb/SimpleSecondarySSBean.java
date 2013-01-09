package de.clb.jee.test.jee6.ejb;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
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

/**
 * Session Bean implementation class SimpleSecondarySSBBean
 */
@Stateless
@LocalBean
@DeclareRoles({"guestRole", "userRole", "adminRole"})
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SimpleSecondarySSBean extends BaseSSBean implements SimpleSecondarySSBeanRemote {
	
	@Resource
	private SessionContext sessionContext;
	
	@Resource
	TransactionSynchronizationRegistry txReg;

    /**
     * Default constructor. 
     */
    public SimpleSecondarySSBean() {
        // TODO Auto-generated constructor stub
    }

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
		
		result.getContextDataElement().add(getContextData(sessionContext, txReg, "simpleSecuredDelegate"));
		
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
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
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

}
