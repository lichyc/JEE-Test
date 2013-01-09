package de.clb.jee.test.jee6.ejb;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

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
		String result = isSuccessful() ? "success" : "failure \n";
		if (!isSuccessful()) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			result += writer.toString();
		}
		return "===================> " + bean.interfaceClass.getSimpleName() + "." + method + "()  -  " + result;
	}

	public SSBean31 getBean() {
		return bean;
	}

	public boolean isSuccessful() {
		return e == null;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

}
