/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.xml;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.beans.ExceptionListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;

import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.xml.XMLInput;

/**
 * @author Jon
 *
 */
public class XMLInputTest {

	@Mock	Document f_document;
	XMLInput<Object>	f_testerOne;
	XMLInput<Object>	f_testerTwo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		new CoreInterface();
		MockitoAnnotations.initMocks(this);
		f_testerOne = new XMLInput<Object>() {

			@Override
			public Object readNode(Document p_doc) {
				return new Object();
			}
		};
		f_testerTwo = new XMLInput<Object>(f_document) {
			@Override
			public Object readNode(Document p_doc) {
				return new Object();
			}
		};
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#XMLInput()}.
	 */
	@Test
	public final void testXMLInput() {
		// given - setup()
		// when
		// then
		assertNull(f_testerOne.getExceptionListener());
		assertNull(f_testerOne.getDocument());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#XMLInput(org.w3c.dom.Document)}.
	 */
	@Test
	public final void testXMLInputDocument() {
		// given - setup()
		// when
		// then
		assertNull(f_testerTwo.getExceptionListener());
		assertEquals(f_document, f_testerTwo.getDocument());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#getInputStream()}.
	 */
	@Test
	public final void testGetInputStream() {
		// given - setup()
		// when
		// then
		assertNull(f_testerOne.getDocument());
		assertEquals(f_document, f_testerTwo.getDocument());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#setDocument(org.w3c.dom.Document)}.
	 */
	@Test
	public final void testSetDocument() {
		// given - setup()
		// when
		f_testerTwo.setDocument(f_document);
		// then
		assertEquals(f_document, f_testerTwo.getDocument());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#getExceptionListener()}.
	 */
	@Test
	public final void testGetExceptionListener() {
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#setExceptionListener(java.beans.ExceptionListener)}.
	 */
	@Test
	public final void testSetExceptionListener() {
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInput#read()}.
	 */
	@Test
	public final void testRead() {
		// given - setup()
		f_testerOne.setDocument(mock(Document.class));
		f_testerOne.setExceptionListener(mock(ExceptionListener.class));
		given(f_testerOne.getDocument().getDocumentElement()).willThrow(mock(RuntimeException.class));
		// when
		assertNull(f_testerOne.read());
		// then
		verify(f_testerOne.getExceptionListener()).exceptionThrown(any(Exception.class));
		// when
		f_testerOne.setExceptionListener(null);
		// then
		assertNull(f_testerOne.read());
	}

}
