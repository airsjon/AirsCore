package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.Tuple;

public class TupleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Tuple<Integer, Integer> f_tuple1;
	private Tuple<Integer, Integer> f_tuple2;
	private Tuple<Integer, Integer> f_tuple3;
	private Tuple<Integer, Float> f_tuple4;
	private Tuple<Integer, Integer> f_tuple5;
	private Tuple<Integer, Integer> f_tuple6;
	private Tuple<Integer, Float> f_tuple7;
	private Tuple<Integer, Integer> f_tuple8;

	@Before
	public void setUp() throws Exception {
		f_tuple1 = new Tuple<Integer, Integer>(5, 7);
		f_tuple2 = new Tuple<Integer, Integer>(7, 8);
		f_tuple3 = new Tuple<Integer, Integer>(5, 7);
		f_tuple4 = new Tuple<Integer, Float>(5, 7f);
		f_tuple5 = new Tuple<Integer, Integer>(5, null);
		f_tuple6 = new Tuple<Integer, Integer>(null, 5);
		f_tuple7 = new Tuple<Integer, Float>(null, null);
		f_tuple8 = new Tuple<Integer, Integer>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		// given
		// when
		// then
		assertEquals(1123, f_tuple1.hashCode());
		assertEquals(1088423004, f_tuple4.hashCode());
		assertEquals(-805099907, f_tuple5.hashCode());
		assertEquals(1435912210, f_tuple6.hashCode());
	}

	@Test
	public void testGetPrimary() {
		// given
		// when
		// then
		assertEquals(5, (int)f_tuple1.getPrimary());
		assertEquals(7, (int)f_tuple2.getPrimary());
		assertEquals(5, (int)f_tuple3.getPrimary());
		assertEquals(5, (int)f_tuple4.getPrimary());
	}

	@Test
	public void testSetPrimary() {
		// given
		// when
		assertTrue(f_tuple1.equals(f_tuple3));
		f_tuple1.setPrimary(6);
		// then
		assertFalse(f_tuple1.equals(f_tuple3));
	}

	@Test
	public void testGetSecondary() {
		// given
		// when
		// then
		assertEquals(7, (int)f_tuple1.getSecondary());
		assertEquals(8, (int)f_tuple2.getSecondary());
		assertEquals(7, (int)f_tuple3.getSecondary());
		assertTrue(Math.abs((float)f_tuple4.getSecondary() - 7f)<.0001f);
	}

	@Test
	public void testSetSecondary() {
		// given
		// when
		assertTrue(f_tuple1.equals(f_tuple3));
		f_tuple1.setSecondary(6);
		// then
		assertFalse(f_tuple1.equals(f_tuple3));
	}

	@Test
	public void testToString() {
		// given
		// when
		// then
		assertEquals("Tuple [f_primary=5, f_secondary=7]", f_tuple1.toString());
	}

	@Test
	public void testEqualsObject() {
		// given
		// when
		// then
		assertTrue(f_tuple1.equals(f_tuple1));
		assertTrue(f_tuple1.equals(f_tuple3));
		assertFalse(f_tuple1.equals(new Object()));
		assertFalse(f_tuple1.equals(f_tuple2));
		assertFalse(f_tuple1.equals(f_tuple4));
		assertFalse(f_tuple1.equals(f_tuple5));
		assertFalse(f_tuple1.equals(f_tuple6));
		assertFalse(f_tuple5.equals(f_tuple1));
		assertFalse(f_tuple6.equals(f_tuple1));
		assertTrue(f_tuple7.equals(f_tuple8));
	}

}
