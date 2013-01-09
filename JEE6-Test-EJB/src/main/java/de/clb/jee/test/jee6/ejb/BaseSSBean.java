package de.clb.jee.test.jee6.ejb;

import javax.ejb.SessionContext;

import de.clb.jee.test.util.ContextDataType;

/**
 * 
 * @author mzottner
 *
 */
public class BaseSSBean {

	protected ContextDataType getContextData(SessionContext ctx, Class<?> clazzName, String operationName) {
		ContextDataType result = new ContextDataType();

		result.setCallTime(System.currentTimeMillis());
		result.setCaller(ctx.getCallerPrincipal().getName());
		if (null != ctx.getUserTransaction()) {
			result.setHasTransaction(true);

		} else {
			result.setHasTransaction(false);
		}
		result.setEJBName(clazzName.getName());
		result.setOperation(operationName);
		return result;

	}

}
