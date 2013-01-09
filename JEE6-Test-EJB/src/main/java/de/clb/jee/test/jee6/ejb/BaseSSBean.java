package de.clb.jee.test.jee6.ejb;

import javax.ejb.SessionContext;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;

import de.clb.jee.test.util.ContextDataType;

/**
 * 
 * @author mzottner
 *
 */
public class BaseSSBean {

	protected ContextDataType getContextData(SessionContext ctx,TransactionSynchronizationRegistry txReg, String operationName) {
		ContextDataType result = new ContextDataType();

		result.setCallTime(System.currentTimeMillis());
		result.setCaller(ctx.getCallerPrincipal().getName());
		if (Status.STATUS_NO_TRANSACTION != txReg.getTransactionStatus()) {
			result.setHasTransaction(true);

		} else {
			result.setHasTransaction(false);
		}
		result.setTransactionStatus(txReg.getTransactionStatus());
		result.setEJBName(this.getClass().getName());
		result.setOperation(operationName);
		return result;

	}

}
