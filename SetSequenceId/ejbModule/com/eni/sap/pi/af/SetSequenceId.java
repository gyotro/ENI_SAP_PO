package com.eni.sap.pi.af;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import com.sap.aii.af.lib.mp.module.Module;
import com.sap.aii.af.lib.mp.module.ModuleContext;
import com.sap.aii.af.lib.mp.module.ModuleData;
import com.sap.aii.af.lib.mp.module.ModuleException;
import com.sap.engine.interfaces.messaging.api.DeliverySemantics;
import com.sap.engine.interfaces.messaging.api.Message;
import com.sap.engine.interfaces.messaging.api.MessageKey;
import com.sap.engine.interfaces.messaging.api.PublicAPIAccessFactory;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditAccess;
import com.sap.engine.interfaces.messaging.api.auditlog.AuditLogStatus;
import com.sap.engine.interfaces.messaging.api.exception.InvalidParamException;
import com.sap.engine.interfaces.messaging.api.exception.MessagingException;
import com.sun.org.apache.xpath.internal.XPathAPI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.*;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

/**
 * @author Administrator
 * 
 * @Notes: PARAMETERS TABLE PARAMETER OPTIONAL DESCRIPTION maxQueueNameLenght
 *         YES Imposta la massima lunghezza del nome della coda. Se il parametro
 *         non viene specificato utilizza il valore di default 16. xpath.0 NO Si
 *         aspetta come valore un espressione XPATH che indica da quale campo
 *         deve estrarre il valore per creare la coda EOIO. xpath.1 YES Si
 *         possono specificare n campi a piacere per creare la coda EOIO. La
 *         coda viene creata concatenando i valori dei campi specificati da
 *         tutti i parametri â€œxpath.â€ separator YES Se Ã¨ stato utilizzato
 *         piÃ¹ di un parametro â€œxpath.â€, i valori trovati per ognuna delle
 *         espressioni XPATH sono concatenati utilizzando come separatore il
 *         carattere specificato da questo p240\m prefix YES Imposta il
 *         prefisso della coda, e.g. XBQIFB
 */
public class SetSequenceId implements SessionBean, TimedObject {

	private static final long serialVersionUID = 2747804335018512584L;
	private ModuleContext mc;
	private SessionContext myContext;
	private MessageKey amk;
	private AuditAccess Audit;

	private final String auditString = "com.eni.sap.pi.af.SetSequenceId - ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.TimedObject#ejbTimeout(javax.ejb.Timer)
	 */
	public void ejbTimeout(Timer arg0) {
		// TODO Auto-generated method stub

	}

	public void ejbCreate() throws javax.ejb.CreateException {

	}

	private String mpget(String pname) {
		return mc.getContextData(pname);
	}

	private String surroundString(String value) {
		if (value.equals(""))
			return value;
		else
			return "''" + value + "''";
	}

	public ModuleData process(ModuleContext moduleContext,
			ModuleData inputModuleData) throws ModuleException {
		String val = "";
		boolean bOld = true;
		// Inizialize global fields
		mc = moduleContext;
		amk = null;

		// Module Data
		String defaultQueueName = "";
		Message msg;
		String msgGuid;

		// Module Parameters
		String maxQueueLenghtValue;
		Vector<String> xpathVector = new Vector<String>();
		int maxQueueLenght = 16;

		// Read Module data
		msg = (Message) inputModuleData.getPrincipalData();
		msgGuid = msg.getMessageId();
		amk = new MessageKey(msgGuid, msg.getMessageDirection());

		try {
			Audit = PublicAPIAccessFactory.getPublicAPIAccess()
					.getAuditAccess();
		} catch (MessagingException e) {
			Audit.addAuditLogEntry(amk, AuditLogStatus.ERROR, auditString
					+ "Unable to access the audit log API.");
		}

		// we check the quality of service and we get the default queue name for
		// EOIO
		if (msg.getDeliverySemantics().equals(
				DeliverySemantics.ExactlyOnceInOrder))
			defaultQueueName = msg.getSequenceId();

		// Get Module Parameters
		maxQueueLenghtValue = mpget("maxQueueNameLenght");
		if (maxQueueLenghtValue != null)
			try {
				maxQueueLenght = Integer.parseInt(maxQueueLenghtValue);
				Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS, auditString
						+ "The queue lenght is now: "
						+ surroundString(Integer.toString(maxQueueLenght)));
			} catch (NumberFormatException e) {
				Audit.addAuditLogEntry(
								amk,
								AuditLogStatus.ERROR,
								auditString
										+ "Unable to convert the queue lenght paremeter, using default value: "
										+ surroundString(Integer
												.toString(maxQueueLenght)));
			}
		else
			Audit.addAuditLogEntry(
							amk,
							AuditLogStatus.SUCCESS,
							auditString
									+ "The queue lenght parameter is not given it will be used the default value: "
									+ surroundString(Integer
											.toString(maxQueueLenght)));

		String sep = mpget("separator");
		String qPrefix = mpget("prefix");
		String qid = null;
		if (qPrefix == null)
			qid = new String();
		else
			qid = qPrefix;

		String sComparePath = mpget("ComparePath");

		// create a DOM rappresentation of the payload
		org.w3c.dom.Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		factory.setNamespaceAware(true);
		factory.setValidating(false);

		if (sComparePath != null) {
			Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS, auditString
					+ "Version 2 started...");
			Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS,
					auditString + "ComparePath inserito: " + sComparePath);
			// Initialize XPath opject
			XPath xpath = XPathFactory.newInstance().newXPath();
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.parse(new ByteArrayInputStream(msg.getDocument()
						.getContent()));
				val = xpath.evaluate(sComparePath, doc);
			} catch (Exception e) {
				Audit.addAuditLogEntry(amk, AuditLogStatus.ERROR, auditString
						+ "Couldn't parse input Document: " + e.getMessage()
						+ " It will be used the default queueID: "
						+ surroundString(defaultQueueName));
			}
			if (val.equals("")) {
				Audit
						.addAuditLogEntry(
								amk,
								AuditLogStatus.WARNING,
								auditString + "ComparePath non valorizzato nel documento, si ritorna al caso di default.. ");
				// caso analogo alla vecchia versione
				bOld = true;
			} else {
				Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS,
						auditString + "ComparePath valorizzato con: " + val);
				qid = qPrefix + val;
				bOld = false;
			}
		}

		// initial version
		if (sComparePath == null || bOld) {
			Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS, auditString
					+ "Initial version started...");
			// we create the xpath expression
			String xpstr = null;
			int cnt = 0;
			do {
				xpstr = mpget("xpath." + new Integer(cnt));
				if (xpstr != null)
					xpathVector.add(xpstr);
				cnt++;
			} while (xpstr != null);
			if (xpathVector.size() == 0) {
				Audit.addAuditLogEntry(amk, AuditLogStatus.WARNING, auditString
						+ "No XPath expressions found. Nothing to do!");
				return inputModuleData;
			}
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.parse(new ByteArrayInputStream(msg.getDocument()
						.getContent()));
			} catch (Exception e) {
				Audit.addAuditLogEntry(amk, AuditLogStatus.ERROR, auditString
						+ "Couldn't parse input Document: " + e.getMessage()
						+ " It will be used the default queueID: "
						+ surroundString(defaultQueueName));
			}

			// we traverse the document searching the values of the xpath
			// exrpessions
			// if we found a not valid value we leave do not nothing and leave
			// the default value
			Vector<String> valvct = new Vector<String>();
			for (int i = 0; i < xpathVector.size(); i++) {
				NodeIterator nl = null;
				try {
					nl = XPathAPI.selectNodeIterator(doc, xpathVector
							.get(i));
				} catch (TransformerException e1) {
					e1.printStackTrace();
				}
				Node node;
				for (val = null; (node = nl.nextNode()) != null; val = node
						.getFirstChild().getNodeValue())
					;
				if (val != null) {
					Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS,
							auditString
									+ "Found the value: "
									+ surroundString(val)
									+ " for the xpath expression: "
									+ surroundString( xpathVector
											.get(i)));
					try {
						int intValue = Integer.parseInt(val);
						val = String.valueOf(intValue);
						Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS,
								auditString
										+ "The int value was converted in: "
										+ surroundString(val)
										+ " for the xpath expression: "
										+ surroundString( xpathVector
												.get(i)));
					} catch (NumberFormatException e) {
					}
				}
				valvct.add(val);
			}

			// if we didn't find any value we leave and left the defualt value
			// of queue
			if (valvct.size() == 0) {
				Audit
						.addAuditLogEntry(
								amk,
								AuditLogStatus.ERROR,
								auditString
										+ "No suitable values found for given XPath expression(s)"
										+ "It will be used the default queueID "
										+ surroundString(defaultQueueName));
				return inputModuleData;
			}
			// concatenate the values using the value of the separator parameter
			for (int i = 0; i < valvct.size(); i++) {
				qid = qid + valvct.get(i);
				if (sep != null && i != valvct.size() - 1)
					qid = qid + sep;
			}
		}

		// if the queue is too long we truncate it
		if (qid.length() > maxQueueLenght) {
			String oldQid = qid;
			qid = qid.substring(0, maxQueueLenght);
			Audit
					.addAuditLogEntry(
							amk,
							AuditLogStatus.WARNING,
							auditString
									+ "Couldn't set the queueID: "
									+ surroundString(oldQid)
									+ " because it is too lenght. It will be truncated into queueID: "
									+ surroundString(qid));
		}
		try {
			msg.setDeliverySemantics(DeliverySemantics.ExactlyOnceInOrder);
			msg.setSequenceId(qid);
			Audit.addAuditLogEntry(amk, AuditLogStatus.SUCCESS, auditString
					+ "Set the queueID: " + surroundString(qid));
		} catch (InvalidParamException e) {
			Audit.addAuditLogEntry(amk, AuditLogStatus.ERROR, auditString
					+ "Couldn't set the queueID: " + surroundString(qid)
					+ e.getMessage());
		}

		return inputModuleData;
	}

}
