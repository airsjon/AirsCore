package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.Tuple3;

public class Tuple3Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Tuple3<Integer, Integer, Integer> f_tuple;
	private Tuple3<Integer, Integer, Integer> f_tuple2;

	@Before
	public void setUp() throws Exception {
		f_tuple = new Tuple3<Integer, Integer, Integer>(1, 2, 3);
		f_tuple2 = new Tuple3<Integer, Integer, Integer>(4, 5, 6);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEquals() {
		// given
		// when
		// then
		assertFalse(f_tuple.equals(f_tuple2));
		// when
		f_tuple.setPrimary(4);
		f_tuple.setSecondary(5);
		f_tuple.setTertiary(6);
		// then
		assertTrue(f_tuple.equals(f_tuple2));
		assertTrue(f_tuple.equals(f_tuple));
		assertFalse(f_tuple.equals(new Object()));
	}
	
	@Test
	public void testHashCode() {
		// when
		// given
		// then
		assertEquals(30817, f_tuple.hashCode());
		assertEquals(33796, f_tuple2.hashCode());
		assertEquals(711413769, new Tuple3<Integer, Integer, Integer>().hashCode());
	}

	@Test
	public void testToString() {
		// given
		// when
		// then
		assertEquals("Tuple3 [f_primary=1, f_secondary=2, f_tertiary=3]", f_tuple.toString());
		assertEquals("Tuple3 [f_primary=4, f_secondary=5, f_tertiary=6]", f_tuple2.toString());
	}

}
