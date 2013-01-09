package de.clb.jee.test.jee6.ejb;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;

import de.clb.jee.test.util.ContextDataType;

/**
 * This object models the result of an EJB method call.
 * 
 * @author mzottner
 * 
 */
public class EJBCallResult implements Serializable {

	/** Serial Id. */
	private static final long serialVersionUID = 201201091222L;

	/** Bean called. */
	final protected SSBean31 bean;

	/** Method called. */
	final protected String method;

	/** Contains an exception if the call failed. */
	protected Exception e;

	/** Result of the call. */
	protected Object result;

	/**
	 * Constructor
	 * 
	 * @param bean
	 * @param method
	 */
	public EJBCallResult(SSBean31 bean, String method) {
		this.bean = bean;
		this.method = method;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = isSuccessful() ? "SUCCESS" : "FAILURE";
		if (e != null) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			result += writer.toString();
		}
		return "\n============ " + result + " ============> " + bean.interfaceClass.getSimpleName() + "." + method + "()\n";
	}

	public SSBean31 getBean() {
		return this.bean;
	}

	public boolean isSuccessful() {

		boolean notNull = this.e == null && this.result != null;
		boolean notCdtException = true;
		if (notNull && result instanceof ContextDataType) {
			notCdtException = StringUtils.isEmpty(((ContextDataType) result).getException());
		}
		return notNull && notCdtException;
	}

	public Exception getE() {
		return this.e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
