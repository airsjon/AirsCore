/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.xml;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;

/**
 * @author Jon Boley
 *
 */
public class XMLInputDocument<T> extends XMLInput<List<T>> {

	private final String f_recordName;
	private final XMLInputComplex<T> f_complexInput;

	/**
	 * @param inputStream
	 * @param recordName
	 * @param complexInput
	 */
	public XMLInputDocument(Document p_document, String p_recordName, XMLInputComplex<T> p_complexInput) {
		super(p_document);
		this.f_recordName = p_recordName;
		this.f_complexInput = p_complexInput;
	}

	public static Document loadDocument(InputStream p_inputStream) {
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(p_inputStream);
		} catch (final Exception e) {
			CoreInterface.getSystem().handleException("Unable to parse XML Document", e, NotificationStatus.BLOCK);
		}
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.xml.XMLInput#readFile(org.w3c.dom.Document)
	 */
	@Override
	public List<T> readNode(Document p_doc) {
		f_complexInput.setName(f_recordName);
		return f_complexInput.read(p_doc);
	}

}
