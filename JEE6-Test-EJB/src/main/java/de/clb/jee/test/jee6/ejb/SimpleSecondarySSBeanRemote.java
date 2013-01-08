package de.clb.jee.test.jee6.ejb;

import javax.ejb.Remote;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;

@Remote
public interface SimpleSecondarySSBeanRemote {
	
	public ContextDataType simpleReply();
	
	public ContextDataType simpleSecuredReply();
	
	public CallSequenceType simpleDelegate();
	
	public CallSequenceType simpleSecuredDelegate();
	
	public ContextDataType replyWithSetRollbackOnly();

}
