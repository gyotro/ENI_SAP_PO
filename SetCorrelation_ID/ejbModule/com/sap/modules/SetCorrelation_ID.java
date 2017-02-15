package com.sap.modules;

/**
 * gdintrono
 */


import javax.ejb.Stateless;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Set;

// Librerias para EJB
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.TimedObject;
import javax.ejb.Timer;

// Librerias para manejo de Documentos XML
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
//import com.Endesa.util.XmlUtil;

// Librerias para creación y manejo de modulos

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.sap.aii.af.lib.mp.module.Module;
import com.sap.aii.af.lib.mp.module.ModuleContext;
import com.sap.aii.af.lib.mp.module.ModuleData;
import com.sap.aii.af.lib.mp.module.ModuleException;
import com.sap.engine.interfaces.messaging.api.Message;
import com.sap.engine.interfaces.messaging.api.MessageKey;
import com.sap.engine.interfaces.messaging.api.MessagePropertyKey;
import com.sap.engine.interfaces.messaging.api.Payload;
import com.sap.engine.interfaces.messaging.api.PublicAPIAccessFactory;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditAccess;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditLogStatus;
import com.sap.engine.interfaces.messaging.api.exception.InvalidParamException;
import com.sap.engine.interfaces.messaging.api.exception.MessagingException;
import com.sap.tc.logging.Location;


/**
 * Session Bean implementation class SetCorrelationID
 */
@Stateless
public class SetCorrelation_ID implements SessionBean, TimedObject
{

        // TODO Auto-generated constructor stub
//    	public XmlUtil xuUtil;
    	public Document docInput;
    	public Document docOutput;
    	static final long serialVersionUID = 7435850550539048631L;
    	String SIGNATURE = "process(ModuleContext moduleContext, ModuleData inputModuleData)";
    	public Location location = null;  
    	AuditAccess audit = null;
    	public Object obj = null;
    	public Message msg = null;
    	public MessageKey key = null;
    	public InputStream iInput;
    	public String sItem = "";
    	public String sMessID = "", sMessID_child = "";
    	public Payload pXml;
    	public Element eFile = null;
    	Node OutputRoot;
    	private final String auditString = "SetCorrelationId - ";
		private Object xpathFac;

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionBean#ejbActivate()
    	 */
    	public void ejbActivate() throws EJBException, RemoteException {
    		// TODO Auto-generated method stub

    	}

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionBean#ejbPassivate()
    	 */
    	public void ejbPassivate() throws EJBException, RemoteException {
    		// TODO Auto-generated method stub

    	}

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionBean#ejbRemove()
    	 */
    	public void ejbRemove() throws EJBException, RemoteException {
    		// TODO Auto-generated method stub

    	}

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
    	 */
    	public void setSessionContext(SessionContext arg0) throws EJBException,
    			RemoteException {
    		// TODO Auto-generated method stub

    	}
     
    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionSynchronization#afterBegin()
    	 */
    	public void afterBegin() throws EJBException, RemoteException {
    		// TODO Auto-generated method stub

    	}

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionSynchronization#afterCompletion(boolean)
    	 */
    	public void afterCompletion(boolean arg0) throws EJBException,
    			RemoteException {
    		// TODO Auto-generated method stub

    	}

    	/* (non-Javadoc)
    	 * @see javax.ejb.SessionSynchronization#beforeCompletion()
    	 */
    	public void beforeCompletion() throws EJBException, RemoteException {
    		// TODO Auto-generated method stub

    	}

    	public void ejbCreate() throws javax.ejb.CreateException 
    	{
    		
    	}

    	public ModuleData process(ModuleContext moduleContext, ModuleData inputModuleData) throws ModuleException
    	{
    		// creamos el objeto que apunta a la clase XML Util
 //   		xuUtil = new XmlUtil();  
    		
    		// se recoge el objeto de entrada
    		obj = inputModuleData.getPrincipalData();
    		msg = (Message) obj;
    		key = new MessageKey(msg.getMessageId(), msg.getMessageDirection());
    		MessagePropertyKey pKey = new MessagePropertyKey("correlationId", "http://sap.com/xi/XI/System/Messaging");
    		
    		// se crea el Audit Log
    		try
    		{
    			audit = PublicAPIAccessFactory.getPublicAPIAccess().getAuditAccess();
    		} 
    		catch (MessagingException e) 
    		{
    			e.printStackTrace();
    		}
    		audit.addAuditLogEntry(key, AuditLogStatus.SUCCESS, auditString + ": Module called");
    		
    		pXml = msg.getMainPayload();
    		
    		// Convertimos el Input Payload en un Documento XML
    		iInput = pXml.getInputStream();
    		
    		try
    		{
    		    docInput = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( iInput );
    		}
    		catch ( Exception e )
    		{
    			throw new ModuleException("Unable to create convert Message Payload to an XML document - " + e.getMessage());
    		} 
    		
    		
    		// Desde la pestaña module configuration, buscamos la ubicación en el XML del MessageID
    		//sItem = moduleContext.getContextData("XML.NodoPadre");
    		sMessID_child = moduleContext.getContextData("xpath");
    		
    		audit.addAuditLogEntry(key, AuditLogStatus.SUCCESS, auditString + ": Input Parameters correctly obtained");
    		
    		// Buscamos el Message ID
    		/*
    		OutputRoot = docInput.getDocumentElement().getElementsByTagName(sItem).item(0);
    		Node nEntrada = xuUtil.getFirstChildNode(OutputRoot, "I_ENTRADA");
    		Node nItem = xuUtil.getFirstChildNode(nEntrada, "ITEMS");
    		Node nSubItem = xuUtil.getFirstChildNode(nItem, "item");
    		Node nHeader = xuUtil.getFirstChildNode(nSubItem, "HEADER");
    		sMessID = xuUtil.getChildNodeText(nHeader, "sMessID_child");
    		*/
    		XPath xpath = XPathFactory.newInstance().newXPath();
    		String msgUUID = "";
    		try {
				 msgUUID = xpath.evaluate(sMessID_child, docInput);
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			this.audit.addAuditLogEntry(key, AuditLogStatus.SUCCESS, auditString + ":Message ID: " +  msgUUID);
			
			// Convert message ID to UUID format
			if(!(msgUUID.length() > 32))
				msgUUID = convertMessageIDToUUID(msgUUID);
			else 
				msgUUID = msgUUID;
			
			audit.addAuditLogEntry(key, AuditLogStatus.SUCCESS, auditString + ":Convert to UUID: " +  msgUUID);
			
			// Set UUID as message correlation ID    		
    		try 
    		{
    			msg.setCorrelationId(msgUUID);
    		}
    		catch (Exception e) 
    		{	
    			throw new ModuleException("Unable to set the CorrelationID - " + e.getMessage());
    		}
    		audit.addAuditLogEntry(key, AuditLogStatus.SUCCESS, auditString + ": Correlation ID correctly setted");

    		try 
    		{
    			msg.setMessageProperty(pKey, sMessID);
    		} 
    		catch (InvalidParamException e) 
    		{
    			e.printStackTrace();
    		}
    		
    		inputModuleData.setPrincipalData(msg);
    		return inputModuleData;
    	}
    	public static String convertMessageIDToUUID(String messageID) {
    		if(messageID.length()!= 32) {
    			throw new IllegalArgumentException("Invalid message ID - length not 32");
    		}
    		String timeLow = messageID.substring(0, 8);  
    		String timeMid = messageID.substring(8, 12);  
    		String timeHighAndVersion = messageID.substring(12, 16);  
    		String clockSeqAndReserved = messageID.substring(16, 18);  
    		String clockSeqLow = messageID.substring(18, 20);  
    		String node = messageID.substring(20, 32);  
    		String msgUUID = timeLow + "-" + timeMid + "-" + timeHighAndVersion + "-" + clockSeqAndReserved + clockSeqLow + "-" + node;
    		return msgUUID;
    	}

		@Override
		public void ejbTimeout(Timer arg0) {
			// TODO Auto-generated method stub
			
		}
    }

