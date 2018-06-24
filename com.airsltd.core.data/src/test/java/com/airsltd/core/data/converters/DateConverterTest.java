package com.airsltd.core.data.converters;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.sql.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;

public class DateConverterTest {

	private static final Date EXPECTED = new Date(new GregorianCalendar(1963,8,20).getTimeInMillis());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private DateConverter f_converter;

	@Before
	public void setUp() throws Exception {
		f_converter = new DateConverter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSql() {
		// given
		// when
		// then
		assertEquals("NULL", f_converter.toSql((IBlockData)null, null));
		assertEquals("'1963-09-20'", f_converter.toSql((IBlockData)null, EXPECTED));
	}

	@Test
	public void testFromSql() throws ParseException {
		// given
		// when
		// then
		assertNull(f_converter.fromSql(null, ""));
		assertEquals(EXPECTED,
				f_converter.fromSql(null, "1963-09-20"));
	}

	@Test
	public void testIsNullString() {
		// given
		// when
		// then
		assertTrue(f_converter.isNullString(null));
		assertTrue(f_converter.isNullString(""));
		assertTrue(f_converter.isNullString(BlockData.OUTNULLDATESTRING));
		assertFalse(f_converter.isNullString("test"));
	}

}
