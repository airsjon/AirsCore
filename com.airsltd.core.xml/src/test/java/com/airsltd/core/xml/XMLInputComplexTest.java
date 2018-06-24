/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.xml;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.airsltd.core.xml.XMLInputComplex;

/**
 * @author Jon
 *
 */
public class XMLInputComplexTest {

	/**
	 * @author Jon
	 *
	 */
	public class TestInputComplex extends XMLInputComplex<Object> {

		
		private int f_readIndex;
		private Object[] f_returns = { new Object(), new Object(), null, new Object() };

		public TestInputComplex() {
			super();
		}

		public TestInputComplex(String p_name, String p_nameSpace) {
			super(p_name, p_nameSpace);
		}

		public TestInputComplex(String p_name) {
			super(p_name);
		}

		/* (non-Javadoc)
		 * @see com.airsltd.core.xml.XMLInputComplex#readComplex(org.w3c.dom.Element)
		 */
		@Override
		public Object readComplex(Element p_currentElement) {
			int l_readIndex = f_readIndex;
			if (f_readIndex>=f_returns.length) {
				f_readIndex = f_returns.length-1;
				l_readIndex = f_readIndex;
			} else f_readIndex++;
			return f_returns[l_readIndex];
		}

	}

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getName()}.
	 */
	@Test
	public final void testGetName() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		// then
		assertEquals("name", l_testOne.getName());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#setName(java.lang.String)}.
	 */
	@Test
	public final void testSetName() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name");
		// when
		l_testOne.setName("name2");
		// then
		assertEquals("name2", l_testOne.getName());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getNameSpace()}.
	 */
	@Test
	public final void testGetNameSpace() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		// then
		assertEquals("nameSpace", l_testOne.getNameSpace());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#setNameSpace(java.lang.String)}.
	 */
	@Test
	public final void testSetNameSpace() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name");
		// when
		l_testOne.setNameSpace("nameSpace2");
		// then
		assertEquals("nameSpace2", l_testOne.getNameSpace());
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#read(org.w3c.dom.Document)}.
	 */
	@Test
	public final void testReadDocument() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		XMLInputComplex<Object> l_testTwo = new TestInputComplex("name");
		Document mockDocument = mock(Document.class);
		NodeList mockList = mock(NodeList.class);
		Element mockNode = mock(Element.class);
		given(mockDocument.getElementsByTagName(anyString())).willReturn(mockList);
		given(mockDocument.getElementsByTagNameNS(anyString(), anyString())).willReturn(mockList);
		given(mockList.getLength()).willReturn(6);
		given(mockList.item(anyInt())).willReturn(mockNode);
		given(mockNode.getNodeType()).willReturn(Node.COMMENT_NODE, Node.ELEMENT_NODE, Node.DOCUMENT_NODE, Node.ELEMENT_NODE);
		// when
		List<Object> rVal = l_testOne.read(mockDocument);
		List<Object> rVal2 = l_testTwo.read(mockDocument);
		// then
		assertEquals(3, rVal.size());
		assertEquals(5, rVal2.size());
		
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#read(org.w3c.dom.NodeList)}.
	 */
	@Test
	public final void testReadNodeList() {
		// given
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		NodeList mockList = mock(NodeList.class);
		Element mockNode = mock(Element.class);
		given(mockList.getLength()).willReturn(6);
		given(mockList.item(anyInt())).willReturn(mockNode);
		given(mockNode.getNodeType()).willReturn(Node.COMMENT_NODE, Node.ELEMENT_NODE, Node.DOCUMENT_NODE, Node.ELEMENT_NODE);
		// when
		List<Object> rVal = l_testOne.read(mockList);
		// then
		assertEquals(3, rVal.size());
		
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLDate(org.w3c.dom.Element, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLDate() {
		// given
		Element m_element = mock(Element.class);
		Element m_subElement = mock(Element.class);
		NodeList m_nodeList = mock(NodeList.class);
		NodeList m_subNodeList = mock(NodeList.class);
		Node m_node = mock(Node.class);
		given(m_element.getElementsByTagName(anyString())).willReturn(null,m_nodeList);
		given(m_element.getElementsByTagNameNS(anyString(), anyString())).willReturn(null,m_nodeList);
		given(m_nodeList.item(0)).willReturn(null,m_subElement);
		given(m_subElement.getChildNodes()).willReturn(null,m_subNodeList);
		given(m_subNodeList.item(0)).willReturn(null,m_node);
		given(m_node.getNodeValue()).willReturn("","2010-10-25","1963-09-20","2000-01-01","1964-10-23","1974-02-28",null,"test6");
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		try {
			assertNull(l_testOne.getXMLDate(m_element, "field1", null));
			assertNull(l_testOne.getXMLDate(m_element, "field1a", null));
			assertNull(l_testOne.getXMLDate(m_element, "field1b", null));
			assertNull(l_testOne.getXMLDate(m_element, "field1c", null));
			try {
				l_testOne.getXMLDate(m_element, "field1d", null);
				assertTrue(false);
			} catch (ParseException e) {};
			assertEquals(new GregorianCalendar(2010, 9, 25).getTime(),l_testOne.getXMLDate(m_element, "field2", "yyyy-MM-dd"));
			assertEquals(new GregorianCalendar(1963, 8, 20).getTime(),l_testOne.getXMLDate(m_element, "field3", null));
			assertEquals(new GregorianCalendar(2000, 0, 01).getTime(),l_testOne.getXMLDate(m_element, "field4", ""));
			assertEquals(new GregorianCalendar(1964, 9, 23).getTime(),l_testOne.getXMLDate(m_element, "field5", "yyyy-MM-dd"));
			assertEquals(new GregorianCalendar(1974, 1, 28).getTime(),l_testOne.getXMLDate(m_element, "field6", "yyyy-MM-dd"));
			assertNull(l_testOne.getXMLDate(m_element, "field7", "yyyy-MM-dd"));
			l_testOne.getXMLDate(m_element, "field8", "yyyy-MM-dd");
			assertTrue(false);
		} catch (DOMException e1) {
		} catch (ParseException e1) {
		} catch (NumberFormatException e) {};
		// then
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1a");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1b");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1c");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1d");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field2");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field3");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field4");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field5");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field6");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field7");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field8");
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLBoolean(org.w3c.dom.Element, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLBoolean() {
		// given
		Element m_element = mock(Element.class);
		Element m_subElement = mock(Element.class);
		NodeList m_nodeList = mock(NodeList.class);
		NodeList m_subNodeList = mock(NodeList.class);
		Node m_node = mock(Node.class);
		given(m_element.getElementsByTagName(anyString())).willReturn(null,m_nodeList);
		given(m_element.getElementsByTagNameNS(anyString(), anyString())).willReturn(null,m_nodeList);
		given(m_nodeList.item(0)).willReturn(null,m_subElement);
		given(m_subElement.getChildNodes()).willReturn(null,m_subNodeList);
		given(m_subNodeList.item(0)).willReturn(null,m_node);
		given(m_node.getNodeValue()).willReturn("1","0",null,"","test");
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		assertFalse(l_testOne.getXMLBoolean(m_element, "field1"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field2"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field3"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field4"));
		assertTrue(l_testOne.getXMLBoolean(m_element, "field5"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field6"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field7"));
		assertFalse(l_testOne.getXMLBoolean(m_element, "field8"));
		try {
			l_testOne.getXMLBoolean(m_element, "field9");
			assertTrue(false);
		} catch (NumberFormatException e) {};
		// then
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field2");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field3");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field4");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field5");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field6");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field7");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field8");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field9");
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLFloat(org.w3c.dom.Element, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLFloat() {
		// given
		Element m_element = mock(Element.class);
		Element m_subElement = mock(Element.class);
		NodeList m_nodeList = mock(NodeList.class);
		NodeList m_subNodeList = mock(NodeList.class);
		Node m_node = mock(Node.class);
		given(m_element.getElementsByTagName(anyString())).willReturn(null,m_nodeList);
		given(m_element.getElementsByTagNameNS(anyString(), anyString())).willReturn(null,m_nodeList);
		given(m_nodeList.item(0)).willReturn(null,m_subElement);
		given(m_subElement.getChildNodes()).willReturn(null,m_subNodeList);
		given(m_subNodeList.item(0)).willReturn(null,m_node);
		given(m_node.getNodeValue()).willReturn("","1","1.2","1.23","1.234","1.2345",null,"test6");
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		assertEquals(0,l_testOne.getXMLFloat(m_element, "field1"),.0001);
		assertEquals(0,l_testOne.getXMLFloat(m_element, "field1a"),.0001);
		assertEquals(0,l_testOne.getXMLFloat(m_element, "field1b"),.0001);
		assertEquals(0,l_testOne.getXMLFloat(m_element, "field1c"),.0001);
		assertEquals(0,l_testOne.getXMLFloat(m_element, "field1d"),.0001);
		assertEquals(1.0f,l_testOne.getXMLFloat(m_element, "field2"),.0001);
		assertEquals(1.2f,l_testOne.getXMLFloat(m_element, "field3"),.0001);
		assertEquals(1.23f,l_testOne.getXMLFloat(m_element, "field4"),.0001);
		assertEquals(1.234f,l_testOne.getXMLFloat(m_element, "field5"),.0001);
		assertEquals(1.2345f,l_testOne.getXMLFloat(m_element, "field6"),.0001);
		assertEquals(0f,l_testOne.getXMLFloat(m_element, "field7"),.0001);
		try {
			l_testOne.getXMLFloat(m_element, "field8");
			assertTrue(false);
		} catch (NumberFormatException e) {};
		// then
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1a");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1b");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1c");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1d");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field2");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field3");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field4");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field5");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field6");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field7");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field8");
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLString(org.w3c.dom.Element, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLString() {
		// given
		Element m_element = mock(Element.class);
		Element m_subElement = mock(Element.class);
		NodeList m_nodeList = mock(NodeList.class);
		NodeList m_subNodeList = mock(NodeList.class);
		Node m_node = mock(Node.class);
		given(m_element.getElementsByTagName(anyString())).willReturn(null,m_nodeList);
		given(m_element.getElementsByTagNameNS(anyString(), anyString())).willReturn(null,m_nodeList);
		given(m_nodeList.item(0)).willReturn(null,m_subElement);
		given(m_subElement.getChildNodes()).willReturn(null,m_subNodeList);
		given(m_subNodeList.item(0)).willReturn(null,m_node);
		given(m_node.getNodeValue()).willReturn("","test1","test2","test3","test4","test5",null,"test6");
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		// when
		assertEquals("",l_testOne.getXMLString(m_element, "field1"));
		assertEquals("",l_testOne.getXMLString(m_element, "field2"));
		assertEquals("",l_testOne.getXMLString(m_element, "field3"));
		assertEquals("",l_testOne.getXMLString(m_element, "field4"));
		assertEquals("",l_testOne.getXMLString(m_element, "field5"));
		assertEquals("test1",l_testOne.getXMLString(m_element, "field6"));
		assertEquals("test2",l_testOne.getXMLString(m_element, "field7"));
		// then
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field2");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field3");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field4");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field5");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field6");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field7");
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLInteger(org.w3c.dom.Element, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLIntegerElementString() {
		// given
		Element m_element = mock(Element.class);
		Element m_subElement = mock(Element.class);
		NodeList m_nodeList = mock(NodeList.class);
		NodeList m_subNodeList = mock(NodeList.class);
		Node m_node = mock(Node.class);
		given(m_element.getElementsByTagName(anyString())).willReturn(null,m_nodeList);
		given(m_element.getElementsByTagNameNS(anyString(), anyString())).willReturn(null,m_nodeList);
		given(m_nodeList.item(0)).willReturn(null,m_subElement);
		given(m_subElement.getChildNodes()).willReturn(null,m_subNodeList);
		given(m_subNodeList.item(0)).willReturn(null,m_node);
		given(m_node.getNodeValue()).willReturn("","1","2","3","4","5",null,"test6","1.7");
		XMLInputComplex<Object> l_testOne = new TestInputComplex("name", "nameSpace");
		XMLInputComplex<Object> l_testTwo = new TestInputComplex();
		// when
		assertEquals(0,l_testOne.getXMLInteger(m_element, "field1"));
		assertEquals(0,l_testOne.getXMLInteger(m_element, "field1a"));
		assertEquals(0,l_testOne.getXMLInteger(m_element, "field1b"));
		assertEquals(0,l_testOne.getXMLInteger(m_element, "field1c"));
		assertEquals(0,l_testOne.getXMLInteger(m_element, "field1d"));
		assertEquals(0,l_testTwo.getXMLInteger(m_element, "field1e"));
		assertEquals(1,l_testOne.getXMLInteger(m_element, "field2"));
		assertEquals(2,l_testTwo.getXMLInteger(m_element, "field3"));
		assertEquals(3,l_testOne.getXMLInteger(m_element, "field4"));
		assertEquals(4,l_testTwo.getXMLInteger(m_element, "field5"));
		assertEquals(5,l_testOne.getXMLInteger(m_element, "field6"));
		assertEquals(0,l_testTwo.getXMLInteger(m_element, "field7"));
		try {
			l_testOne.getXMLInteger(m_element, "field8");
			assertTrue(false);
		} catch (NumberFormatException e) {};
		try {
			l_testOne.getXMLInteger(m_element, "field9");
			assertTrue(false);
		} catch (NumberFormatException e) {};
		// then
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1a");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1b");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1c");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field1d");
		verify(m_element).getElementsByTagName("field1e");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field2");
		verify(m_element).getElementsByTagName("field3");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field4");
		verify(m_element).getElementsByTagName("field5");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field6");
		verify(m_element).getElementsByTagName("field7");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field8");
		verify(m_element).getElementsByTagNameNS("nameSpace", "field9");
	}

	/**
	 * Test method for {@link com.airsltd.core.xml.XMLInputComplex#getXMLAttrInteger(org.w3c.dom.Element, java.lang.String)}.
	 */
	@Test
	public final void testGetXMLAttrInteger() {
		// given
		Element m_element = mock(Element.class);
		given(m_element.getAttributeNS(anyString(), anyString())).willReturn(
				"2", "3", "", "notanInteger", "5");
		XMLInputComplex<Object> l_testOne = new TestInputComplex();
		XMLInputComplex<Object> l_testTwo = new TestInputComplex("name", "nameSpace");
		// when
		assertEquals(2,l_testOne.getXMLAttrInteger(m_element, "field"));
		assertEquals(3,l_testTwo.getXMLAttrInteger(m_element, "field"));
		try {
			assertEquals(0,l_testOne.getXMLAttrInteger(m_element, "field"));
			assertTrue(false);
		} catch (NumberFormatException e) {};
		try {
			assertEquals(0,l_testOne.getXMLAttrInteger(m_element, "field"));
			assertTrue(false);
		} catch (NumberFormatException e) {};
		assertEquals(5, l_testOne.getXMLAttrInteger(m_element,  "field"));
		// then
	}

	@Test
	public final void testGetXMLAttrStr() {
		// given
		Element m_element = mock(Element.class);
		given(m_element.getAttributeNS(anyString(), anyString())).willReturn(
				"blarg");
		XMLInputComplex<Object> l_testOne = new TestInputComplex();
		XMLInputComplex<Object> l_testTwo = new TestInputComplex("name", "nameSpace");
		// when
		assertEquals("blarg",l_testOne.getXMLAttrString(m_element, "field"));
		assertEquals("blarg",l_testTwo.getXMLAttrString(m_element, "field2"));
	}
	
	@Test
	public final void testGetXMLAttrFloat() {
		Element m_element = mock(Element.class);
		given(m_element.getAttributeNS(anyString(), anyString())).willReturn(
				"2.3", "3", "", "notanInteger", "5.23");
		XMLInputComplex<Object> l_testOne = new TestInputComplex();
		XMLInputComplex<Object> l_testTwo = new TestInputComplex("name", "nameSpace");
		// when
		assertTrue(Math.abs(2.3f - l_testOne.getXMLAttrFloat(m_element, "field")) < .001f);
		assertTrue(Math.abs(3f - l_testTwo.getXMLAttrFloat(m_element, "field"))<.001f);
		try {
			assertEquals(0f,l_testOne.getXMLAttrFloat(m_element, "field"),.001f);
			assertTrue(false);
		} catch (NumberFormatException e) {};
		try {
			assertEquals(0f,l_testOne.getXMLAttrFloat(m_element, "field"),.001f);
			assertTrue(false);
		} catch (NumberFormatException e) {};
		assertTrue(Math.abs(5.23f - l_testOne.getXMLAttrFloat(m_element,  "field")) < .001f);
		// then
		
	}
}
