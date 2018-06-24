package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PasswordStringTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testPasswordString() {
		// given
		PasswordString l_test = new PasswordString("aPasswordOfNoImport");
		String l_string = "aPasswordOfNoImport";
		// when
		// then
		assertTrue(l_test.getPassword().equals(l_string));
	}

	@Test
	public final void testSetPassword() {
		// given
		PasswordString l_test = new PasswordString("aPasswordOfNoImport");
		String l_string = "aPasswordOfNoImport";
		// when
		l_test.setPassword("anotherPassword");
		// then
		assertFalse(l_test.getPassword().equals(l_string));
	}

	@Test
	public final void testEquals() {
		// given
		PasswordString l_test = new PasswordString("aPasswordOfNoImport");
		// when
		// then
		assertFalse(l_test.equals(null));
		assertFalse(l_test.equals(new PasswordString("a test")));
		assertTrue(l_test.equals(new PasswordString("aPasswordOfNoImport")));
	}
	
	@Test
	public final void testHash() {
		// given
		PasswordString l_test = new PasswordString("aPasswordOfNoImport");
		// when
		// then
		assertEquals(739777273, l_test.hashCode());
	}
}
