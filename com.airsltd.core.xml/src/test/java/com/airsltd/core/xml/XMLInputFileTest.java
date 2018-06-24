/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.xml;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.beans.ExceptionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.airsltd.core.xml.XMLInputComplex;
import com.airsltd.core.xml.XMLInputFile;

/**
 * @author Jon
 *
 */
public class XMLInputFileTest {

	private InputStream m_inputStream;
	private String m_recordName;
	private XMLInputComplex<Object> m_complexInput;
	private XMLInputFile<Object, XMLInputComplex<Object>> f_tester;

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
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		m_complexInput = mock(XMLInputComplex.class);
		m_inputStream = mock(InputStream.class);
		m_recordName = "mockRecordName";
		f_tester = new XMLInputFile<Object, XMLInputComplex<Object>>(m_inputStream, m_recordName, m_complexInput);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputFile#XMLInputFile(java.io.InputStream, java.lang.String, com.airsltd.core.xml.XMLInputComplex)}.
	 */
	@Test
	public final void testXMLInputFile() {
		// given - see setup
		// when
		assertTrue(XMLInputFile.class.isInstance(f_tester));
		// then
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputFile#readNode(org.w3c.dom.Document)}.
	 */
	@Test
	public final void testReadNodeDocument() {
		// given - see setup()
		Document l_mockDocument = mock(Document.class);
		List<Object> l_listReturn = new ArrayList<Object>();
		l_listReturn.add(new Object());
		given(m_complexInput.read(l_mockDocument)).willReturn(l_listReturn );
		// when
		List<Object> r_val = f_tester.readNode(l_mockDocument);
		// then
		assertEquals(l_listReturn, r_val);
		verify(m_complexInput).setName("mockRecordName");
		verify(m_complexInput).read(l_mockDocument);
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputFile#read()}.
	 */
	@Test
	public final void testRead() {
		// given
		f_tester.setInputStream(new ByteArrayInputStream("<xml><mockRecordName>Test</mockRecordName></xml>".getBytes(Charset.defaultCharset())));
		// when
		List<Object> r_val = f_tester.read();
		// then
		assertEquals(0,r_val.size());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputFile#read()}.
	 */
	@Test
	public final void testReadException() {
		// given
		f_tester.setInputStream(mock(InputStream.class));
		f_tester.setExceptionListener(mock(ExceptionListener.class));
		try {
			given(f_tester.getInputStream().read()).willThrow(mock(IOException.class));
			given(f_tester.getInputStream().read(any(byte[].class))).willThrow(mock(IOException.class));
			given(f_tester.getInputStream().read(any(byte[].class), anyInt(), anyInt())).willThrow(mock(IOException.class));
		} catch (IOException e) {
		}
		// when
		List<Object> r_val = f_tester.read();
		// then
		assertTrue(r_val.isEmpty());
		// given
		// when
		f_tester.setExceptionListener(null);
		r_val = f_tester.read();
		// then
		assertTrue(r_val.isEmpty());
		
	}

}
