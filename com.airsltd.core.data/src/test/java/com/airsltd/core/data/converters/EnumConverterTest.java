package com.airsltd.core.data.converters;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.annotations.FieldStyle;

public class EnumConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private EnumConverter<FieldStyle> f_converter;
	private EnumConverter<TestNamedStyle> f_converterNamed;

	@Before
	public void setUp() throws Exception {
		f_converter = new EnumConverter<FieldStyle>(FieldStyle.class);
		f_converterNamed = new EnumConverter<TestNamedStyle>(TestNamedStyle.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSql() {
		// given
		// when
		// then
		assertEquals("-1", f_converter.toSql(null, null));
		assertEquals("3", f_converter.toSql(null, FieldStyle.ASIS));
		assertEquals("ONE", f_converterNamed.toSqlNamed(null, TestNamedStyle.ONE));
		assertEquals("TWO", f_converterNamed.toSqlNamed(null, TestNamedStyle.TWO));
		assertEquals("THREE", f_converterNamed.toSqlNamed(null, TestNamedStyle.THREE));
		assertEquals("", f_converterNamed.toSqlNamed(null, null));
	}

	@Test
	public void testFromSql() throws ParseException {
		// given
		// when
		// then
		assertEquals(FieldStyle.CAPITALIZED, f_converter.fromSql(null, "2"));
		assertEquals(null, f_converter.fromSql(null, "-1"));
		assertEquals(TestNamedStyle.ONE, f_converterNamed.fromSql(null, "ONE"));
		assertEquals(TestNamedStyle.TWO, f_converterNamed.fromSql(null, "TWO"));
		assertEquals(TestNamedStyle.THREE, f_converterNamed.fromSql(null, "THREE"));
		assertEquals(null, f_converterNamed.fromSql(null, ""));
		assertEquals(null, f_converterNamed.fromSql(null, "BLARG"));
	}

	@Test(expected=ParseException.class)
	public void testFromSqlThrow() throws ParseException {
		// given
		// when
		// then
		assertEquals(FieldStyle.CAPITALIZED, f_converter.fromSql(null, "ab"));
	}

}
