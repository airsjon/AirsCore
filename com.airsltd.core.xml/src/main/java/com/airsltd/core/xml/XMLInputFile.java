/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * @author Jon
 * @param <T>
 *
 */
public class XMLInputFile<T, U extends XMLInputComplex<T>> extends XMLInput<List<T>> {

	private InputStream f_inputStream;
	private final String f_recordName;
	private final U f_complexInput;

	/**
	 * @param inputStream
	 * @param recordName
	 * @param complexInput
	 */
	public XMLInputFile(InputStream p_inputStream, String p_recordName, U p_complexInput) {
		super();
		this.setInputStream(p_inputStream);
		this.f_recordName = p_recordName;
		this.f_complexInput = p_complexInput;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.xml.XMLInput#read()
	 */
	@Override
	public List<T> read() {
		final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(getInputStream());
			setDocument(doc);
			return super.read();
		} catch (final Exception e) {
			if (getExceptionListener() != null) {
				getExceptionListener().exceptionThrown(e);
			}
		}
		return new ArrayList<T>();
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return f_inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public final void setInputStream(InputStream p_inputStream) {
		this.f_inputStream = p_inputStream;
	}

}
