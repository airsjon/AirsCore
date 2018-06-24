package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AutoIncrementFieldTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private AutoIncrementField f_autoIncrement;
	private AutoIncrementField f_autoIncrement2;
	private AutoIncrementField f_autoIncrement3;

	@Before
	public void setUp() throws Exception {
		AutoIncrementField.clear();
		f_autoIncrement = new AutoIncrementField(AutoIncrementFieldTest.class);
		f_autoIncrement2 = new AutoIncrementField(AutoIncrementFieldTest.class, -1);
		f_autoIncrement3 = new AutoIncrementField(AutoIncrementFieldTest.class, 5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		// given
		int l_currentHash = f_autoIncrement.hashCode();
		int l_currentHash2 = f_autoIncrement2.hashCode();
		int l_currentHash3 = f_autoIncrement3.hashCode();
		// when
		// then
		assertEquals(l_currentHash, f_autoIncrement.hashCode());
		assertEquals(l_currentHash2, f_autoIncrement2.hashCode());
		assertEquals(l_currentHash3, f_autoIncrement3.hashCode());
	}

	@Test
	public void testEqualsObject() {
		// given
		// when
		f_autoIncrement.setId(42);
		AutoIncrementField l_autoTemp = new AutoIncrementField(AutoIncrementFieldTest.class, 42);
		AutoIncrementField l_newTemp = new AutoIncrementField(AutoIncrementFieldTest.class);
		AutoIncrementField l_newTemp2 = new AutoIncrementField(AutoIncrementFieldTest.class);
		// then
		assertTrue(f_autoIncrement.equals( l_autoTemp));
		assertFalse(f_autoIncrement2.equals( l_autoTemp));
		assertFalse(f_autoIncrement3.equals( l_autoTemp));
		assertFalse(f_autoIncrement.equals( new Object()));
		assertTrue(f_autoIncrement.equals(f_autoIncrement));
		assertFalse(l_newTemp.equals(l_newTemp2));
		assertFalse(l_newTemp.equals(l_autoTemp));
		assertFalse(l_autoTemp.equals(l_newTemp));
		
	}

	@Test
	public void testGetId() {
		// given
		// when
		// then
		assertEquals(-1,f_autoIncrement.getId());
		assertEquals(-1,f_autoIncrement2.getId());
		assertEquals(5,f_autoIncrement3.getId());
		
	}

	@Test
	public void testSetId() {
		// given
		// when
		f_autoIncrement.setId(42);
		AutoIncrementField l_autoTemp = new AutoIncrementField(AutoIncrementFieldTest.class, 42);
		AutoIncrementField l_autoTemp2 = new AutoIncrementField(AutoIncrementFieldTest.class, 45);
		AutoIncrementField l_autoTemp3 = new AutoIncrementField(AutoIncrementFieldTest.class, 47);
		AutoIncrementField l_autoTemp4 = new AutoIncrementField(AutoIncrementFieldTest.class, 48);
		l_autoTemp2.setId(42);
		l_autoTemp3.setId(50);
		l_autoTemp4.setId(-1);
		// then
		assertEquals(l_autoTemp,f_autoIncrement);
		assertEquals(l_autoTemp2,f_autoIncrement);
		assertEquals(42, l_autoTemp.getId());
		assertEquals(42, l_autoTemp2.getId());
		
	}

	@Test
	public void testhashCode() {
		// given
		int l_hash = f_autoIncrement3.hashCode();
		// when
		AutoIncrementField l_autoTemp = new AutoIncrementField(AutoIncrementFieldTest.class, 5);
		// then
		assertEquals(l_hash, l_autoTemp.hashCode());
		
	}

	@Test
	public void testSetHash() {
		// given
		// when
		f_autoIncrement.setHash(42);
		// then
		assertEquals(42, f_autoIncrement.hashCode());
	}

	@Test
	public void testCompareTo() {
		// given
		AutoIncrementField l_autoInc1 = new AutoIncrementField(AutoIncrementFieldTest.class, 7);
		AutoIncrementField l_autoInc2 = new AutoIncrementField(AutoIncrementFieldTest.class, 5);
		// when
		// then
		assertEquals(0, f_autoIncrement.compareTo(f_autoIncrement));
		assertEquals(Integer.compare(f_autoIncrement.hashCode(), f_autoIncrement2.hashCode()), f_autoIncrement.compareTo(f_autoIncrement2));
		assertEquals(-1, f_autoIncrement.compareTo(f_autoIncrement3));
		assertEquals(-1, f_autoIncrement3.compareTo(l_autoInc1));
		assertEquals(0, f_autoIncrement3.compareTo(l_autoInc2));
	}
	
	@Test
	public void testToString() {
		// given
		// when
		// then
		assertEquals("-1", f_autoIncrement.toString());
		assertEquals("5", f_autoIncrement3.toString());
	}
	
}
