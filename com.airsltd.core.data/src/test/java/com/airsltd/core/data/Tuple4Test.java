package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.Tuple4;

public class Tuple4Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Tuple4<Integer, Double, Float, Boolean> f_tuple;
	private Tuple4<Integer, Double, Float, Boolean> f_tuple2;

	@Before
	public void setUp() throws Exception {
		f_tuple = new Tuple4<Integer, Double, Float, Boolean>();
		f_tuple2 = new Tuple4<Integer, Double, Float, Boolean>(3, 4d, 5f, true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		// given
		// when
		assertEquals(1456758517, f_tuple.hashCode());
		assertEquals(1333754221, f_tuple2.hashCode());
		// then
	}

	@Test
	public void testEqualsObject() {
		// given
		// when
		// then
		assertFalse(f_tuple.equals(f_tuple2));
		// when
		f_tuple.setPrimary(3);
		f_tuple.setSecondary(4d);
		f_tuple.setTertiary(5f);
		f_tuple.setQuadinary(true);
		// then
		assertTrue(f_tuple.equals(f_tuple2));
		assertTrue(f_tuple.equals(f_tuple));
		assertFalse(f_tuple.equals(new Object()));
		
	}
	
	@Test
	public void testToString() {
		// given
		// when
		// then
		assertEquals("Tuple4 [f_primary=null, f_secondary=null, f_tertiary=null, f_quadinary=null]", f_tuple.toString());
		assertEquals("Tuple4 [f_primary=3, f_secondary=4.0, f_tertiary=5.0, f_quadinary=true]", f_tuple2.toString());
	}

}
