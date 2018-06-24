/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jon
 *
 */
public abstract class XMLInputComplex<T> {

	private String f_name;
	private String f_nameSpace;

	/**
	 *
	 */
	public XMLInputComplex() {
		super();
	}

	/**
	 * @param name
	 */
	public XMLInputComplex(String p_name) {
		super();
		this.f_name = p_name;
	}

	/**
	 * @param name
	 * @param nameSpace
	 */
	public XMLInputComplex(String p_name, String p_nameSpace) {
		super();
		this.f_name = p_name;
		this.f_nameSpace = p_nameSpace;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return f_name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String p_name) {
		this.f_name = p_name;
	}

	/**
	 * @return the nameSpace
	 */
	public String getNameSpace() {
		return f_nameSpace;
	}

	/**
	 * @param nameSpace
	 *            the nameSpace to set
	 */
	public void setNameSpace(String p_nameSpace) {
		this.f_nameSpace = p_nameSpace;
	}

	public List<T> read(Document p_doc) {
		return f_nameSpace == null ? read(p_doc.getElementsByTagName(f_name))
				: read(p_doc.getElementsByTagNameNS(f_nameSpace, f_name));
	}

	public List<T> read(NodeList listOfPairs) {
		final List<T> rval = new ArrayList<T>();

		for (int l_cnt = 0; l_cnt < listOfPairs.getLength(); l_cnt++) {

			final Node firstPersonNode = listOfPairs.item(l_cnt);
			if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element currentElement = (Element) firstPersonNode;
				final T newRecord = readComplex(currentElement);
				if (newRecord != null) {
					rval.add(newRecord);
				}
			}
		}
		return rval;
	}

	public abstract T readComplex(Element p_currentElement);

	public Date getXMLDate(Element p_firstPersonElement, String p_string, String p_dateFormat) throws ParseException {
		return getXMLDateNS(f_nameSpace, p_firstPersonElement, p_string, p_dateFormat);
	}

	public Date getXMLDateNS(String p_nameSpace1, Element p_firstPersonElement, String p_string, String p_dateFormat)
			throws ParseException {
		final String textList = getDefinition(p_nameSpace1, p_firstPersonElement, p_string);
		return textList == null ? null : processXMLDateNS(p_dateFormat, textList);
	}

	private Date processXMLDateNS(String p_dateFormat, String p_dateString) throws ParseException {
		final String l_dateFormat = p_dateFormat == null || p_dateFormat.isEmpty() ? "yyyy-MM-dd" : p_dateFormat;
		final SimpleDateFormat sdf = new SimpleDateFormat(l_dateFormat);
		return new Date(sdf.parse(p_dateString.trim()).getTime());
	}

	public boolean getXMLBoolean(Element firstPersonElement, String string) {
		return getXMLBooleanNS(f_nameSpace, firstPersonElement, string);
	}

	public boolean getXMLBooleanNS(String nameSpace1, Element firstPersonElement, String string) {
		final String l_stringToken = getDefinition(nameSpace1, firstPersonElement, string);
		return l_stringToken == null || l_stringToken.trim().isEmpty() ? false
				: Integer.parseInt(l_stringToken.trim()) != 0;
	}

	public float getXMLFloat(Element firstPersonElement, String string) {
		return getXMLFloatNS(f_nameSpace, firstPersonElement, string);
	}

	public float getXMLFloatNS(String nameSpace1, Element firstPersonElement, String string) {
		final String l_stringToken = getDefinition(nameSpace1, firstPersonElement, string);
		return l_stringToken == null || l_stringToken.trim().isEmpty() ? 0 : Float.parseFloat(l_stringToken.trim());
	}

	public String getXMLString(Element firstPersonElement, String string) {
		return getXMLStringNS(f_nameSpace, firstPersonElement, string);
	}

	public String getXMLStringNS(String nameSpace1, Element firstPersonElement, String string) {
		final String l_stringValue = getDefinition(nameSpace1, firstPersonElement, string);
		return l_stringValue == null ? "" : l_stringValue;
	}

	public int getXMLInteger(Element firstPersonElement, String string) {
		return getXMLInteger(f_nameSpace, firstPersonElement, string);
	}

	public int getXMLInteger(String nameSpace1, Element firstPersonElement, String string) {
		final String l_stringToken = getDefinition(nameSpace1, firstPersonElement, string);
		return l_stringToken == null || l_stringToken.trim().isEmpty() ? 0 : Integer.parseInt(l_stringToken.trim());
	}

	public String getDefinition(String nameSpace1, Element p_element, String p_token) {
		final NodeList list = getNodes(nameSpace1, p_element, p_token);
		String l_retVal = null;
		if (list != null) {
			final Element element = (Element) list.item(0);
			if (element != null) {
				final NodeList textList = element.getChildNodes();
				if (textList != null && textList.item(0) != null) {
					l_retVal = textList.item(0).getNodeValue();
				}
			}
		}
		return l_retVal;
	}

	public int getXMLAttrInteger(Element element, String string) {
		return getXMLAttrInteger(f_nameSpace, element, string);
	}

	public String getXMLAttrString(Element element, String string) {
		return getXMLAttrString(f_nameSpace, element, string);
	}

	public float getXMLAttrFloat(Element element, String string) {
		return getXMLAttrFloat(f_nameSpace, element, string);
	}

	private NodeList getNodes(String nameSpace1, Element firstPersonElement, String string) {
		return nameSpace1 != null ? firstPersonElement.getElementsByTagNameNS(nameSpace1, string)
				: firstPersonElement.getElementsByTagName(string);
	}

	private int getXMLAttrInteger(String nameSpace1, Element element, String string) {
		final String attrValue = element.getAttributeNS(nameSpace1, string);
		return Integer.parseInt(attrValue);
	}

	private String getXMLAttrString(String nameSpace1, Element element, String string) {
		return element.getAttributeNS(nameSpace1, string);
	}

	private float getXMLAttrFloat(String nameSpace1, Element element, String string) {
		final String attrValue = element.getAttributeNS(nameSpace1, string);
		return Float.parseFloat(attrValue);
	}
}
