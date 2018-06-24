package com.airsltd.core.json;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.CoreInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonComponentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private JsonComponent f_component;
	private JsonObject f_object;

	@Before
	public void setUp() throws Exception {
		new CoreInterface();
		f_object = new JsonObject();
		f_object.add("String", new JsonPrimitive("Test"));
		f_object.add("Boolean", new JsonPrimitive(true));
		f_object.add("Float", new JsonPrimitive(32.3f));
		f_object.add("Integer", new JsonPrimitive(42));
		f_object.add("Date", new JsonPrimitive("1963-09-20"));
		f_object.add("Char",  new JsonPrimitive('c'));
		f_object.add("Complex", new JsonArray());
		f_component = new JsonComponent();
		CoreInterface.getSystem().setLog(mock(Log.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDateFormat() {
		// given
		// when
		SimpleDateFormat l_dateFormat = f_component.getDateFormat();
		// then
		assertEquals(new SimpleDateFormat("yyyy-MM-dd"), l_dateFormat);
	}

	@Test
	public void testSetDateFormat() {
		// given
		// when
		f_component.setDateFormat(new SimpleDateFormat("MM/dd/yyyy"));
		SimpleDateFormat l_dateFormat = f_component.getDateFormat();
		// then
		assertEquals(new SimpleDateFormat("MM/dd/yyyy"), l_dateFormat);
	}

	@Test
	public void testConvertType() {
		// given
		// when
		assertEquals("T",f_component.convertType(f_object, "String"));
		assertEquals("c",f_component.convertType(f_object, "Char"));
		assertEquals("", f_component.convertType(f_object, "Another"));
		assertEquals("", f_component.convertType(f_object, "Complex"));
		// then
	}

	@Test
	public void testConvertDate() {
		// given
		// when
		assertNull(f_component.convertDate(f_object, "String"));
		assertNull(f_component.convertDate(f_object, "Char"));
		assertNull(f_component.convertDate(f_object, "Another"));
		assertNull(f_component.convertDate(f_object, "Complex"));
		assertEquals(new GregorianCalendar(1963, 8, 20).getTime(), f_component.convertDate(f_object,"Date"));
		// then
		verify(CoreInterface.getSystem().getLog()).error(eq("JSON Date Conversion Failure [String, Test]"), any(ParseException.class));
		verify(CoreInterface.getSystem().getLog()).error(eq("JSON Date Conversion Failure [Char, c]"), any(ParseException.class));
	}

	@Test
	public void testConvertFloat() {
		// given
		// when
		assertEquals(42f, f_component.convertFloat(f_object, "Integer"),.001f);
		assertEquals(32.3f, f_component.convertFloat(f_object, "Float"),.001f);
		assertEquals(0,f_component.convertFloat(f_object, "String"),.001f);
		assertEquals(0, f_component.convertFloat(f_object, "Another"),.001f);
		assertEquals(0, f_component.convertFloat(f_object, "Complex"),.001f);
		// then
		verify(CoreInterface.getSystem().getLog()).error(eq("JSON Float Conversion Failure [String, Test]"), any(ParseException.class));
	}

	@Test
	public void testConvertString() {
		// given
		// when
		assertEquals("Test",f_component.convertString(f_object, "String"));
		assertEquals("c",f_component.convertString(f_object, "Char"));
		assertEquals("", f_component.convertString(f_object, "Another"));
		assertEquals("", f_component.convertString(f_object, "Complex"));
		// then
	}

	@Test
	public void testConvertInt() {
		// given
		// when
		assertEquals(42, f_component.convertInt(f_object, "Integer"));
		assertEquals(32, f_component.convertInt(f_object, "Float"));
		assertEquals(0,f_component.convertInt(f_object, "String"));
		assertEquals(0, f_component.convertInt(f_object, "Another"));
		assertEquals(0, f_component.convertInt(f_object, "Complex"));
		// then
		verify(CoreInterface.getSystem().getLog()).error(eq("JSON Integer Conversion Failure [String, Test]"), any(ParseException.class));
		
	}

	@Test
	public void testConvertBoolean() {
		// given
		// when
		assertTrue(f_component.convertBoolean(f_object, "Boolean"));
		assertFalse(f_component.convertBoolean(f_object, "Float"));
		assertFalse(f_component.convertBoolean(f_object, "String"));
		assertFalse(f_component.convertBoolean(f_object, "Another"));
		assertFalse(f_component.convertBoolean(f_object, "Complex"));
		// then
	}

}
