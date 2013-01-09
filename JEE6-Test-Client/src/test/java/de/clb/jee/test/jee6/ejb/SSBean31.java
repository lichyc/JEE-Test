package de.clb.jee.test.jee6.ejb;

/**
 * Enumertion containing all available Stateless Session Beans 3.1.
 * 
 * @author mzottner
 *
 */
public enum SSBean31 {

	SimplePrimary("JEE6-Test/JEE6-Test-EJB//SimplePrimarySSBean", SimplePrimarySSBeanRemote.class),
	
	SimpleSecondary("JEE6-Test/JEE6-Test-EJB//SimpleSecondarySSBean", SimpleSecondarySSBeanRemote.class);
	
	final public String jndiName;
	
	final public Class<?> interfaceClass;

	/**
	 * Constructor 
	 * 
	 * @param jndiShortName
	 * @param interfaceClass
	 */
	private SSBean31(String jndiShortName, Class<?> interfaceClass) {
		this.jndiName = "ejb:"+jndiShortName+"!"+interfaceClass.getName();
		this.interfaceClass = interfaceClass;
	}
	
}
