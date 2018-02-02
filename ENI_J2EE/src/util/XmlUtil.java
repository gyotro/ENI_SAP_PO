package util;


import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.codec.binary.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.sap.aii.af.service.cpa.impl.util.Util;
import com.sap.aii.mapping.api.*;

/**
 * Package of utilities for managing XML Document
 * @author <a href="https://it.linkedin.com/">Giovanni D'Introno</a>
 */

@SuppressWarnings("unused")
public class XmlUtil{
	public final static int iALIGN_LEFT = 0;
	public final static int iALIGN_RIGHT = 1;

	public XmlUtil() {
	}

	public String formatField(String field, int length, int align, char padding) {
		String sResult = "";

		if (field == null) {
			// Create field with padding char

			for (int i = 0; i < length; i++) {
				sResult += padding;
			}
		} else {
			if (field.length() >= length) {
				sResult = getLeft(field, length);
			} else {
				int iPadPosition = length - field.length();

				if (align == iALIGN_RIGHT) {
					sResult = field;

					for (int i = 0; i < iPadPosition; i++) {
						sResult = padding + sResult;
					}
				} else {
					sResult = field;

					for (int i = 0; i < iPadPosition; i++) {
						sResult += padding;
					}
				}
			}
		}

		return (sResult);
	}

	public static InputStream byteToInputStream(byte[] bIn) {
		InputStream input;
		InputStream myInputStream = new ByteArrayInputStream(bIn);
		return (myInputStream);
	}

	public String getLeft(String field, int length) {
		String sResult = "";

		if (field != null) {
			if (field.length() > length) {
				sResult = field.substring(0, length);
			} else {
				sResult = field;
			}
		}

		return (sResult);
	}

	public String getRight(String field, int length) {
		String sResult = "";

		if (field != null) {
			if (field.length() > length) {
				sResult = field.substring(field.length() - length, field
						.length());
			} else {
				sResult = field;
			}
		}

		return (sResult);
	}

	public Element createNode(Document parent, String name) {
		return (createNode(parent, name, null, null, null, null, null));
	}

	public Element createNode(Document parent, String name, String text) {
		return (createNode(parent, name, null, null, text, null, null));
	}

	public Element createNode(Document parent, String name, String attname,
			String attvalue) {
		return (createNode(parent, name, attname, attvalue, null, null, null));
	}

	public Element createNode(Document parent, String name, String attname,
			String attvalue, String attname1, String attvalue1) {
		return (createNode(parent, name, attname, attvalue, null, attname1,
				attvalue1));
	}

	public Element createNode(Document parent, String name, String attname,
			String attvalue, String text, String attname1, String attvalue1) {
		Element eResult = null;

		if (name == null) {
			return (eResult);
		}

		eResult = parent.createElement(name);

		if (text != null) {
			eResult.appendChild(parent.createTextNode(text));
		}

		if (attname != null && attvalue != null) {
			eResult.setAttribute(attname, attvalue);
		}

		if (attname1 != null && attvalue1 != null) {
			eResult.setAttribute(attname1, attvalue1);
		}

		return (eResult);
	}

	public String getChildNodeText(Node node, String child) {
		String sResult = "";

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node tmp = node.getChildNodes().item(i);

			if (tmp.getNodeType() == Node.ELEMENT_NODE
					&& tmp.getNodeName().equals(child)) {
				for (int j = 0; j < tmp.getChildNodes().getLength(); j++) {
					Node tmp2 = tmp.getChildNodes().item(j);

					if (tmp2.getNodeType() == Node.TEXT_NODE) {
						sResult = tmp2.getNodeValue();
						break;
					}
				}

				break;
			}
		}

		return (sResult);
	}

	public Vector<String> getChildNodesText(Node node, String child) {

		Vector<String> vResult = new Vector<String>();

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node tmp = node.getChildNodes().item(i);

			if (tmp.getNodeType() == Node.ELEMENT_NODE
					&& tmp.getNodeName().equals(child)) {
				for (int j = 0; j < tmp.getChildNodes().getLength(); j++) {
					Node tmp2 = tmp.getChildNodes().item(j);

					if (tmp2.getNodeType() == Node.TEXT_NODE) {
						vResult.add(tmp2.getNodeValue().toString());
					}
				}
			}
		}

		return (vResult);
	}

	public Node getFirstChildNode(Node node, String child) {
		Node nResult = null;

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node tmp = node.getChildNodes().item(i);

			if (tmp.getNodeType() == Node.ELEMENT_NODE
					&& tmp.getNodeName().equals(child)) {
				nResult = tmp;
				break;
			}
		}

		return (nResult);
	}

	// esta clase ha sido modificada porque daba un warning la version anterior
	// se comenta
	public Node[] getChildNodes(Node node, String child) {
		Vector<Node> vTmp = new Vector<Node>();

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node tmp = node.getChildNodes().item(i);

			if (tmp.getNodeType() == Node.ELEMENT_NODE
					&& tmp.getNodeName().equals(child)) {
				vTmp.add(tmp);
			}
		}

		Node nResult[] = new Node[vTmp.size()];

		for (int i = 0; i < vTmp.size(); i++) {
			nResult[i] = vTmp.get(i);
		}

		vTmp = null;

		return (nResult);
	}

	/*
	 * public Node[] getChildNodes( Node node , String child ) { Vector vTmp =
	 * new Vector();
	 * 
	 * for( int i = 0 ; i < node.getChildNodes().getLength() ; i++ ) { Node tmp =
	 * node.getChildNodes().item( i );
	 * 
	 * if( tmp.getNodeType() == Node.ELEMENT_NODE && tmp.getNodeName().equals(
	 * child ) ) { vTmp.add( tmp ); } }
	 * 
	 * Node nResult[] = new Node[ vTmp.size() ];
	 * 
	 * for( int i = 0 ; i < vTmp.size() ; i++ ) { nResult[ i ] = ( Node
	 * )vTmp.get( i ); }
	 * 
	 * vTmp = null;
	 * 
	 * return( nResult ); }
	 */
	public String lookUp(Node nodes[], String qualfkey, String qualfvalue,
			String field) {
		String sResult = null;

		for (int i = 0; i < nodes.length; i++) {
			if (getChildNodeText(nodes[i], qualfkey).trim().equals(
					qualfvalue.trim())) {
				sResult = getChildNodeText(nodes[i], field);
				break;
			}
		}

		return (sResult);
	}

	public void lookUpAndChange(NodeList nodes, String qualfkey,
			String qualfvalue, String field, String newValue) {

		for (int i = 0; i < nodes.getLength(); i++) {
			if (getChildNodeText(nodes.item(i), qualfkey).trim().equals(
					qualfvalue.trim())) {
				getFirstChildNode(nodes.item(i), field)
						.setTextContent(newValue);
				break;
			}
		}

	}

	public String lookUpAndTrim(Node nodes[], String qualfkey,
			String qualfvalue, String field) {
		String sResult = null;

		for (int i = 0; i < nodes.length; i++) {
			if (getChildNodeText(nodes[i], qualfkey).trim().equals(
					qualfvalue.trim())) {
				sResult = getChildNodeText(nodes[i], field);
				break;
			}
		}

		return (sResult == null ? "" : sResult.trim());
	}

	public String lookUp(Node nodes[], String qualfkeys[],
			String qualfvalues[], String field) {
		String sResult = null;
		boolean bFound = true;

		if (qualfkeys.length == qualfvalues.length) {
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < qualfkeys.length; j++) {
					if (!getChildNodeText(nodes[i], qualfkeys[j]).trim()
							.equals(qualfvalues[j].trim())) {
						bFound = false;
						break;
					} else {
						bFound = true;
					}
				}

				if (bFound) {
					sResult = getChildNodeText(nodes[i], field);
					break;
				}
			}
		}

		return (sResult);
	}

	public String roundNumber(double value, int decimal) {
		BigDecimal bdDecimal = new BigDecimal(value);
		bdDecimal.setScale(decimal, BigDecimal.ROUND_HALF_EVEN);

		return (bdDecimal.toString());
	}

	public String roundNumber(String value, int integer, int decimal) {
		return (roundNumber(new Double(value).doubleValue(), integer, decimal));
	}

	public String roundNumber(double value, int integer, int decimal) {
		String sInteger = "";
		String sDecimal = "";

		BigDecimal bdDecimal = new BigDecimal(value);
		bdDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		for (int i = 0; i < integer; i++) {
			sInteger += "#";
		}

		for (int i = 0; i < decimal; i++) {
			sDecimal += "0";
		}

		DecimalFormatSymbols dfsSymbol = new DecimalFormatSymbols();
		dfsSymbol.setDecimalSeparator('.');

		DecimalFormat dfNumber = new DecimalFormat(sInteger + "." + sDecimal);
		dfNumber.setDecimalFormatSymbols(dfsSymbol);

		return (dfNumber.format(value));
	}

	public String convertDateDDMMYYYY_(String yyyymmdd) {
		return (yyyymmdd.substring(6, 8) + yyyymmdd.substring(4, 6) + yyyymmdd
				.substring(0, 4));
	}

	public String convertDateYYYYMMDD_(String ddmmyyyy) {
		return (ddmmyyyy.substring(4, 8) + ddmmyyyy.substring(2, 4) + ddmmyyyy
				.substring(0, 2));
	}

	public String asHTML(String iSeparador) {
		char[] cSep = iSeparador.toCharArray();
		if (cSep[0] < 32) {
			/* control char */
			return "^" + (char) (cSep[0] + 64);
		} else if (cSep[0] > 126) {
			/* high ascii */
			return "&#" + Integer.toString(cSep[0]) + ";";
		} else {
			switch (cSep[0]) {
			case 32:
				return "&nbsp;";
			case 38:
				return "&amp;";
			case 34:
				return "&quot;";
			case 60:
				return "&lt;";
			case 62:
				return "&gt;";
				/* ordinary char */
			default:
				return String.valueOf(cSep);
			}
		}
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] barr = new byte[1024 * 3];
		while (true) {
			int r = in.read(barr);
			if (r <= 0) {
				break;
			}
			out.write(barr, 0, r);
		}

	}

	public static String isLetter(String var1) // funcion utilizada para
	// detectar si en un texto hay
	// solo numeros o letras tambien
	{
		int iLen = var1.length();
		String sValue = "0";
		char[] cValue = var1.toCharArray();
		for (int i = 0; i < iLen; i++) {
			if (Character.isLetter(cValue[i]))
				sValue = "1";
		}

		return sValue;
	}

	public static String FiletoBase64String(InputStream inputStream) {

		byte[] bPDF;

		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			try {
				copy(inputStream, buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bPDF = buffer.toByteArray();

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		byte[] encoded = Base64.encodeBase64(bPDF);
		String sPDF = "";
		try {
			sPDF = new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return sPDF;
	}

	public String BytetoBase64String(byte[] bInput) {

		byte[] encoded = Base64.encodeBase64(bInput);
		String sPDF = "";
		try {
			sPDF = new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return sPDF;
	}

	public static byte[] decodeBase64(String sPDF) {
		byte[] bDecoded = Base64.decodeBase64(sPDF);
		return bDecoded;
	}

	// Metodo para sumar dia a una fecha: segun lo que se quiera hacer hay cosas
	// que no sirven y cosas que se pueden a�adir
	public static Date dateSum(Date fecha, int diasSum) {
		// se pone la fecha de entrada en un tipo calendar para manipular fechas
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);

		// estas varables no se utilizan de momento pero podrian servir para
		// extender el metodo
		int dia = calendar.get(Calendar.DAY_OF_MONTH); // dia del mes
		int mes = calendar.get(Calendar.MONTH); // mes, de 0 a 11
		int year = calendar.get(Calendar.YEAR); // anno
		int hora24 = calendar.get(Calendar.HOUR_OF_DAY); // hora en formato
		// 24hs
		int minutos = calendar.get(Calendar.MINUTE);
		int segundos = calendar.get(Calendar.SECOND);
		int milisegundos = calendar.get(Calendar.MILLISECOND);

		calendar.add(Calendar.DAY_OF_MONTH, diasSum);

		// se vuelve a poner todo en un tipo Date
		fecha = calendar.getTime();

		return fecha;
	}

	public static Document bytesToXml(byte[] xml)
	// used to convert from byte to XML
	{
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			doc = builder.parse(new ByteArrayInputStream(xml));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}

	// Metodo para pasar de document a String
	public String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty("encoding", "iso-8859-1");
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// convert a document to Byte array
	public byte[] xmlToBytes(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(out);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return out.toByteArray();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Metodo que convierte a ZIP un vector de entrada, conteniendo los varios
	// ficheros que se quieren incluir en el ZIP.
	public byte[] createZIP(Vector<String> vData, Vector<String> vName)
			throws Exception {
		// vData e vName devono avere lo stesso numero di elementi
		String metodo = "createZIP";
		JarOutputStream newJar = null;
		ByteArrayOutputStream baosAux = null;
		byte[] exitBytes = null;
		byte[] inBytes = null;
		String sName = "";

		try {
			baosAux = new ByteArrayOutputStream();
			newJar = new JarOutputStream(baosAux);
			for (int i = 0; i < vData.size(); i++) {
				sName = (String) vName.get(i);

				inBytes = XmlUtil.decodeBase64(((String) vData.get(i)));
				if (inBytes != null) {
					JarEntry entryXML = new JarEntry(sName);
					newJar.putNextEntry(entryXML);
					newJar.write(inBytes);
				}
			}

		} catch (IOException e) {
			Logger.getLogger(Util.class.getName()).log(
					Level.SEVERE,
					"Error al crear el ZIP en metodo: " + metodo + " - "
							+ e.getMessage());
			throw new Exception(e.getMessage(), e);

		} finally {
			if (newJar != null) {
				try {
					newJar.close();
					exitBytes = baosAux.toByteArray();
					newJar = null;
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage(), e);
				}
			}
			if (baosAux != null) {
				try {
					baosAux.close();
					baosAux = null;
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage(), e);
				}
			}
		}

		return exitBytes;

	}

	public byte[] createZIP(Vector<String> vData) throws Exception {
		Vector<String> vName = new Vector<String>(vData.capacity());
		for (int i = 0; i < vData.size(); i++) {
			vName.add(i, "file" + i);
		}
		return this.createZIP(vData, vName);
	}

	public String createZIPinBase64(Vector<String> vData, Vector<String> vName)
			throws Exception {
		// vData e vName devono avere lo stesso numero di elementi
		String metodo = "createZIP";
		JarOutputStream newJar = null;
		ByteArrayOutputStream baosAux = null;
		byte[] exitBytes = null;
		byte[] inBytes = null;
		String sName = "";

		try {
			baosAux = new ByteArrayOutputStream();
			newJar = new JarOutputStream(baosAux);
			for (int i = 0; i < vData.size(); i++) {
				sName = (String) vName.get(i);

				inBytes = XmlUtil.decodeBase64(((String) vData.get(i)));
				if (inBytes != null) {
					JarEntry entryXML = new JarEntry(sName);
					newJar.putNextEntry(entryXML);
					newJar.write(inBytes);
				}
			}

		} catch (IOException e) {
			Logger.getLogger(Util.class.getName()).log(
					Level.SEVERE,
					"Error al crear el ZIP en metodo: " + metodo + " - "
							+ e.getMessage());
			throw new Exception(e.getMessage(), e);

		} finally {
			if (newJar != null) {
				try {
					newJar.close();
					exitBytes = baosAux.toByteArray();
					newJar = null;
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage(), e);
				}
			}
			if (baosAux != null) {
				try {
					baosAux.close();
					baosAux = null;
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage(), e);
				}
			}
		}

		return Base64.encodeBase64String(exitBytes);

	}

	public String convertByte2Base64String(byte[] dataIn) {
		return Base64.encodeBase64String(dataIn);
	}

	public String createZIPinBase64(Vector<String> vData) throws Exception {
		Vector<String> vName = new Vector<String>(vData.capacity());
		for (int i = 0; i < vData.size(); i++) {
			vName.add(i, "file" + i);
		}
		return createZIPinBase64(vData, vName);
	}

	// convierte y descodifica un String Base64 a un Byte[]
	public byte[] convertAndDecodeBase64StringToByte(String sBase64String) {
		byte[] bDecoded = Base64.decodeBase64(sBase64String);
		return bDecoded;
	}

	public Node nodeImport(Document parent, Node nNodeImport) {
		return parent.importNode(nNodeImport, true);
	}

	public Document changeContentTag(Document document, String tag,
			String newContent)
	/**
	 * Funziona solo con la JDK 5.0
	 */
	{
		Node node = document.getElementsByTagName(tag).item(0);
		if (node != null) {
			Node nodeText = node.getFirstChild();
			if (nodeText != null) {
				nodeText.setTextContent(newContent);
			} else {
				nodeText = document.createTextNode(newContent);
				node.appendChild(nodeText);
			}
		}
		return document;
	}

	public static String GUIDgenerate(boolean bHyphenMantain) {
		// Se il booleano bHyphenMantain � false il risultato conterr� i segni
		// '-' tipici di un GUID in SAP PI, altrimenti NO

		String sGUID = "";
		UUID idOne = UUID.randomUUID();

		if (bHyphenMantain)
			sGUID = idOne.toString().toUpperCase();
		else
			sGUID = idOne.toString().replaceAll("-", "").toUpperCase();

		return sGUID;
	}

	public HashMap XmlGrouping(NodeList nNodeToGroup, String sField) {
		Vector vData1;
		HashMap hmAux1 = new HashMap();
		for (int i = 0; i < nNodeToGroup.getLength(); i++) {

			String sAux = getChildNodeText(nNodeToGroup.item(i), sField).trim();
			if (hmAux1.containsKey(sAux)) {
				vData1 = (Vector) hmAux1.get(sAux);
				vData1.addElement(new Integer(i));
			} else {
				vData1 = new Vector();
				vData1.addElement(new Integer(i));
				hmAux1.put(sAux, vData1);
			}
		}

		/*
		 * Set stKeys1 = hmAux1.keySet(); Iterator itIter1 = stKeys1.iterator();
		 */
		return hmAux1;

		/**
		 * Una volta creata la Hash Map bisogna prendere uno alla volta tutti i
		 * valori suddivisi, si fa cosi
		 * 
		 * sKeys_tot = itIter1.next(); si raccoglie il valore del campo chiave)
		 * vData1 = ( Vector ) hmAux1.get( sKeys_tot ); si raccoglie il vettore,
		 * contenente gli indici dei nodi che contengono quel campo chiave nAux =
		 * nItem.item( Integer.parseInt( vData1.elementAt( 0 ).toString() ) );
		 * in questo modo si prende solo l'indice del primo nodo che contiene il
		 * campo chiave, se vogliamo raccogliere tutti gli indici bisogna fare
		 * un altro for sulla dimensione del vettore
		 */

	}

	public String inputStream2StringConverter(InputStream is) {
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"ISO-8859-1"));
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.getMessage();
			}
		}
		return "";
	}

	public Document fileContentConversion(InputStream is,
			ArrayList<String> sHeaderFields, ArrayList<String> sBodyFields,
			ArrayList<String> sTrailerFields, String sElementSeparator,
			String sFieldSeparator) {

		/**
		 * Questo metodo sostituisce il file content conversion di un file
		 * adapter: si utilizza solo in caso di conversione di un flat file (con
		 * un header, un body e opzionalmente un trailer), dati i nomi dei campi
		 * da convertire
		 */

		Document docOutput = null;
		try {
			docOutput = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		} catch (Exception e) {
			e.getMessage();
		}
		Node nHeader = createNode(docOutput, "Header");
		Node nBody = createNode(docOutput, "Body");
		Node nTrailer = createNode(docOutput, "Trailer");
		Node nFile = createNode(docOutput, "File");
		Node nField;

		String sFile = inputStream2StringConverter(is);

		StringTokenizer stElement = new StringTokenizer(sFile,
				sElementSeparator);
		String sField = "";
		int iElemnum = stElement.countTokens();
		short iFieldCount = 0;
		short iElemCount = 0;
		if (iElemnum == 2) {
			while (stElement.hasMoreTokens()) {
				if (iElemCount == 0) {
					sField = stElement.nextToken();
					StringTokenizer stField = new StringTokenizer(sField,
							sFieldSeparator);
					while (stField.hasMoreTokens()) {

						nField = createNode(docOutput, sHeaderFields
								.get(iFieldCount), stField.nextToken());
						nHeader.appendChild(nField);
						iFieldCount++;
					}
				}

				else if (iElemCount == 1) {
					sField = stElement.nextToken();
					StringTokenizer stField = new StringTokenizer(sField,
							sFieldSeparator);
					while (stField.hasMoreTokens()) {
						nField = createNode(docOutput, sBodyFields
								.get(iFieldCount), stField.nextToken());
						nBody.appendChild(nField);
						iFieldCount++;
					}

				}
				iFieldCount = 0;
				iElemCount++;
			}
		}
		if (iElemnum == 3) {
			while (stElement.hasMoreTokens()) {
				if (iElemCount == 0) {
					sField = stElement.nextToken();
					StringTokenizer stField = new StringTokenizer(sField,
							sFieldSeparator);
					while (stField.hasMoreTokens()) {
						nField = createNode(docOutput, sHeaderFields
								.get(iFieldCount), stField.nextToken());
						nHeader.appendChild(nField);
						iFieldCount++;
					}
				}

				else if (iElemCount == 1) {
					sField = stElement.nextToken();
					StringTokenizer stField = new StringTokenizer(sField,
							sFieldSeparator);
					while (stField.hasMoreTokens()) {
						nField = createNode(docOutput, sBodyFields
								.get(iFieldCount), stField.nextToken());
						nBody.appendChild(nField);
						iFieldCount++;
					}

				} else if (iElemCount == 2) {
					sField = stElement.nextToken();
					StringTokenizer stField = new StringTokenizer(sField,
							sFieldSeparator);
					while (stField.hasMoreTokens()) {
						nField = createNode(docOutput, sTrailerFields
								.get(iFieldCount), stField.nextToken());
						nTrailer.appendChild(nField);
						iFieldCount++;
					}

				}
				iFieldCount = 0;
				iElemCount++;
			}
		}
		nFile.appendChild(nHeader);
		nFile.appendChild(nBody);
		nFile.appendChild(nTrailer);
		docOutput.appendChild(nFile);
		return docOutput;
	}

	public static byte[] InputStreamToByteConverter(InputStream is)
			throws IOException {
		/**
		 * Per fare questa conversione si potrebbe anche utilizzare il package
		 * Apache: commons-io, Classe IOUtils:
		 * 
		 * InputStream is; byte[] bytes = IOUtils.toByteArray(is);
		 * 
		 */
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] byteRes = new byte[16384];
		int nRead;
		while ((nRead = is.read(byteRes, 0, byteRes.length)) != -1) {
			buffer.write(byteRes, 0, nRead);
		}

		buffer.flush();
		return buffer.toByteArray();
	}

	public OutputStream ConvertByteToOutputStream(byte[] bPDF,
			OutputStream outputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			baos.write(bPDF);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			baos.writeTo(outputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return outputStream;
	}

	public OutputStream Base64ToFile(InputStream input, OutputStream output) {
		String sBase64In = this.inputStream2StringConverter(input);
		byte[] bDecoded = this.convertAndDecodeBase64StringToByte(sBase64In);
		try {
			output = this.ConvertByteToOutputStream(bDecoded, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public byte[] Unzip(InputStream input) {
// 		Metodo Funzionante
//		Se ci fossero più file in un solo zip allora bisognerebbe fare un ciclo su tutte le zipEntry e scrivere i byte in un Array
//		OutputStream out = null;
//		Vector<byte[]> vOut = new Vector<byte[]>();
//		BufferedOutputStream bout = null;
		int byteRead;
//		JarInputStream newJar;
		
//		http://stackoverflow.com/questions/26960997/convert-outputstream-to-bytearrayoutputstream		
		
		int BUFFER = 2048;
		try {
			ZipInputStream  zin = new ZipInputStream(new BufferedInputStream(input));
			ZipEntry ze = null;

			// FileOutputStream ots = new FileOutputStream(ze.getName());
			byte[] data = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			out = new BufferedOutputStream(ots);
//			long size = ze.getSize();
			int n = 0;
			long count = 0;
			 while((ze = zin.getNextEntry()) != null)
			{
				 byteRead=0; 
				 data = new byte[BUFFER]; 
				 while ((byteRead = zin.read(data,0,BUFFER)) != -1) 
				 { 
					 bos.write(data,0,byteRead); 
				 } 
			}
		return	bos.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public void Unzip(InputStream input, OutputStream out) {
 
//		OutputStream out = null;
//		Vector<byte[]> vOut = new Vector<byte[]>();
//		BufferedOutputStream bout = null;
		int byteRead;
		
		int BUFFER = 2048;
		try {
			ZipInputStream  zin = new ZipInputStream(new BufferedInputStream(input));
			ZipEntry ze = null;

			// FileOutputStream ots = new FileOutputStream(ze.getName());
			byte[] data = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			out = new BufferedOutputStream(ots);
//			long size = ze.getSize();
			int n = 0;
			long count = 0;
			 while((ze = zin.getNextEntry()) != null)
				{
				 byteRead=0; 
				 data = new byte[BUFFER]; 
				 while ((byteRead = zin.read(data,0,BUFFER)) != -1) 
				 { 
					 bos.write(data,0,byteRead); 
				 } 

			}
			out.write(bos.toByteArray());
			out.flush();
			out.close();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    /**
     * Returns the same input string but converted in XML Format
     * @author <a href="">gdintrono</a>
     * @param xmlSource the input String
     * @param codif if true, the codification will be inserted
     * 
     * @return the same as the input, but in XML Format 
     *      
     */
	public Document stringToDocument(String xmlSource, boolean codif)   
    throws SAXException, ParserConfigurationException, IOException 
    {  
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder = factory.newDocumentBuilder();  
	    // Es obligatorio añadir la stringa de codificacion al principio del documento 
	    if(codif)
	    	xmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xmlSource;
	    return builder.parse(new InputSource(new StringReader(xmlSource)));  
    } 

	public Document stringToDocument(String xmlSource)   
    throws SAXException, ParserConfigurationException, IOException 
    {  
		// Version without the codification string <?xml version=\"1.0\" encoding=\"UTF-8\"?>
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder = factory.newDocumentBuilder();  
	    return builder.parse(new InputSource(new StringReader(xmlSource)));  
    } 
	
	public String getActualDate() {
		String DATE_FORMAT_NOW = "yyyyMMdd";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public String getActualDate(String dateFormat) {
		String DATE_FORMAT_NOW = dateFormat;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	static public InputStream getInputStreamFromByte(byte[] myByte)
	{
		InputStream myInputStream = new ByteArrayInputStream(myByte); 
		return myInputStream;
	}
	/**
	 * @param dateFormat
	 *            String formato in cui si desidera visualizzare la data
	 */

	// Funzione aggiunta nel 2016
	public String makeXMLString(Document doc) {
        String xmlString = "";
        if (doc != null) {
            try {
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                StringWriter sw = new StringWriter();
                StreamResult result = new StreamResult(sw);
                DOMSource source = new DOMSource(doc);
                trans.transform(source, result);
                xmlString = sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return xmlString;
    }
}