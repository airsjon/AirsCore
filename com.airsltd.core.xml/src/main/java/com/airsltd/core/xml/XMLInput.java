/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.xml;

import java.beans.ExceptionListener;
import org.w3c.dom.Document;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;

/**
 *
 * provide the framework for easy reading and writing of a complex data type
 * from an XML file
 *
 * @author Jon Boley
 *
 */
public abstract class XMLInput<T> {

	// bean fields

	private Document f_document;
	private ExceptionListener f_exceptionListener;

	// internal fields

	// constructors

	/**
	 * Default constructor. Without proper initialization of <i>inputStream</i>
	 * the XMLInput reader will fail.
	 */
	public XMLInput() {
	}

	/**
	 * Create an XMLInput based on the <i>inputStream</i>
	 * 
	 * @param inputStream
	 *            the stream to read
	 */
	public XMLInput(Document document) {
		this.f_document = document;
	}

	/**
	 * document is the document to read the XML data from
	 *
	 * @return the inputStream
	 */
	public Document getDocument() {
		return f_document;
	}

	/**
	 * exceptionListener provides a means to control exception handling by the
	 * consumer of this XML file
	 *
	 * @return the exceptionListener
	 */
	public ExceptionListener getExceptionListener() {
		return f_exceptionListener;
	}

	/**
	 * read the xml data from the input stream and return <i>T</i>
	 *
	 * @return the data stored in the xml file
	 */
	public T read() {
		T l_retVal = null;
		try {

			// normalize text representation
			f_document.getDocumentElement().normalize();
			l_retVal = readNode(f_document);
		} catch (final Exception e) {
			if (f_exceptionListener != null) {
				f_exceptionListener.exceptionThrown(e);
			} else {
				CoreInterface.getSystem().handleException("Unable to read", e, NotificationStatus.BLOCK);
			}
		}
		return l_retVal;
	}

	/**
	 * this method does the actual work of making <i>T</i> from the XML data.
	 *
	 * @param doc
	 *            document to be converted
	 * @return
	 */
	public abstract T readNode(Document doc);

	/**
	 * document is the document to read the XML data from
	 *
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setDocument(Document document) {
		this.f_document = document;
	}

	/**
	 * exceptionListener provides a means to control exception handling by the
	 * consumer of this XML file
	 * 
	 * @param exceptionListener
	 *            the exceptionListener to set
	 */
	public void setExceptionListener(ExceptionListener exceptionListener) {
		this.f_exceptionListener = exceptionListener;
	}

}
