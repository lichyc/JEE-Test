package de.clb.jee.test.jee6.ejb;

import javax.ejb.Remote;

import de.clb.jee.test.util.CallSequenceType;
import de.clb.jee.test.util.ContextDataType;

@Remote
public interface SimplePrimarySSBeanRemote {
	
	public ContextDataType simpleReply();
	
	public ContextDataType simpleSecuredReply();
	
	public CallSequenceType simpleDelegate();
	
	public CallSequenceType simpleSecuredDelegate();
	
	public CallSequenceType delegate2SecondaryReply();
	
	public CallSequenceType delegate2SecuredSecondaryReply();
	
	public ContextDataType replyWithSetRollbackOnly();
	
	public CallSequenceType delegate2RemoteReply();
	
	public CallSequenceType delegate2SecuredRemoteReply();

}
