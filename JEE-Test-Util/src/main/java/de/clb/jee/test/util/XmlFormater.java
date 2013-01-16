/**
 * 
 */
package de.clb.jee.test.util;

import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author clichybi
 *
 */
public class XmlFormater {
	
	private static Marshaller contextDataTypeMarshaller = null;
	private static Marshaller callSequenceTypeMarshaller = null;
    private static Unmarshaller contextDataTypeUnmarshaller = null;
    private static Unmarshaller callSequenceTypeUnmarshaller = null;

    static {
        try {
            JAXBContext contextDataTypeContext = JAXBContext.newInstance(de.clb.jee.test.util.ContextDataType.class);
            contextDataTypeMarshaller = contextDataTypeContext.createMarshaller();
            contextDataTypeMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            contextDataTypeMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            
            JAXBContext callSequenceTypeContext = JAXBContext.newInstance(de.clb.jee.test.util.CallSequenceType.class);
            callSequenceTypeMarshaller = callSequenceTypeContext.createMarshaller();
            callSequenceTypeMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            callSequenceTypeMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            contextDataTypeUnmarshaller = contextDataTypeContext.createUnmarshaller();
            callSequenceTypeUnmarshaller = callSequenceTypeContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("There was a problem creating a JAXBContext object for formatting the object to XML.");
        }
    }

    public void marshal(ContextDataType obj, OutputStream os) throws Exception {
        try {
            contextDataTypeMarshaller.marshal(obj, os);
        } catch (JAXBException jaxbe) {
            throw new Exception(jaxbe);
        }
    }

    public String marshalToString(ContextDataType obj) throws Exception {
        try {
            StringWriter sw = new StringWriter();
            contextDataTypeMarshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException jaxbe) {
            throw new Exception(jaxbe);
        }
    }
    
    public void marshal(CallSequenceType obj, OutputStream os) throws Exception {
        try {
            callSequenceTypeMarshaller.marshal(obj, os);
        } catch (JAXBException jaxbe) {
            throw new Exception(jaxbe);
        }
    }

    public String marshalToString(CallSequenceType obj) throws Exception {
        try {
            StringWriter sw = new StringWriter();
            callSequenceTypeMarshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException jaxbe) {
            throw new Exception(jaxbe);
        }
    }

}
