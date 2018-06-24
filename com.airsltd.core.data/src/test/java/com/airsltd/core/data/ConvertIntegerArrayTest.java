package com.airsltd.core.data;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.converters.ConvertIntegerArray;

public class ConvertIntegerArrayTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private int[] f_test;
	private ConvertIntegerArray f_convertor;

	@Before
	public void setUp() throws Exception {
		f_test = new int[] { 3, 4, 5 };
		f_convertor = new ConvertIntegerArray();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSql() {
		// given
		// when
		// then
		assertEquals( "[3, 4, 5]", f_convertor.toSql(null, f_test));
	}

	@Test
	public void testFromSql() {
		// given
		// when
		// then
		try {
			assertArrayEquals(f_test, f_convertor.fromSql(null, "[3, 4, 5]"));
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
