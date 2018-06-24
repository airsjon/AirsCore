/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.xml;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author Jon
 *
 */
public class XMLInputDocumentTest extends MockSystemSetup {

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private XMLInputDocument<Object> f_inputDocument;
	private Document f_document;
	private XMLInputComplex<Object> f_inputComplex;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		f_document = mock(Document.class);
		f_inputComplex = mock(XMLInputComplex.class);
		f_inputDocument = new XMLInputDocument<Object>(f_document, "testRecord", f_inputComplex);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputDocument#read()}.
	 */
	@Test
	public final void testRead() {
		// given
		List<Object> l_list = new ArrayList<Object>();
		given(f_inputComplex.read(any(Document.class))).willReturn(l_list);
		// when
		assertEquals(l_list, f_inputDocument.readNode(f_document));
		// then
		verify(f_inputComplex).setName("testRecord");
		verify(f_inputComplex).read(any(Document.class));
	}
	
	@Test
	public final void testLoadDocument() {
		// given
		String l_xmlString = ""
				+ "<html>"
				+ "<body>"
				+ "</body></html>";
		String l_xmlBadString = ""
				+ "<html>"
				+ "<body>"
				+ "</blargbody></html>";
		InputStream l_stream = new ByteArrayInputStream(l_xmlString.getBytes(StandardCharsets.UTF_8));
		InputStream l_streamBad = new ByteArrayInputStream(l_xmlBadString.getBytes(StandardCharsets.UTF_8));
		// when
		Document l_document = XMLInputDocument.loadDocument(l_stream);
		try {
			XMLInputDocument.loadDocument(l_streamBad);
		} catch (RuntimeException l_re) {
			assertEquals("Unable to parse XML Document", l_re.getMessage());
		}
		// then
		assertTrue(l_document instanceof Document);
	}

}
