package com.airsltd.core.data.converters;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArrayConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private ArrayConverter<Integer> f_integerConverter;
	private ArrayConverter<Integer> f_intConverter;

	@Before
	public void setUp() throws Exception {
		f_integerConverter = ArrayConverter.getConverter(Integer.class);
		f_intConverter = ArrayConverter.getConverter(int.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConverter() {
		// given
		// when
		// then
	}

	@Test
	public void testToSql() {
		// given
		// when
		String l_retVal = f_intConverter.toSql(null, new Integer[] {3, 4, 5,6});
		// then
		assertEquals("3,4,5,6", l_retVal);
	}

	@Test
	public void testFromSql() throws ParseException {
		// given
		// when
		Integer[] l_retVal = f_integerConverter.fromSql(null, "3,4,5,7,23,15");
		// then
		assertArrayEquals(new Integer[] { 3, 4, 5, 7, 23, 15}, l_retVal);
	}

	@Test
	public void testBreakApartArray() {
		// given
		// when
		String[] l_retVal = f_integerConverter.breakApartArray("3,4,5,7,23,15");
		// then
		assertArrayEquals(new String[] { "3", "4", "5", "7", "23", "15"}, l_retVal);
	}

}
