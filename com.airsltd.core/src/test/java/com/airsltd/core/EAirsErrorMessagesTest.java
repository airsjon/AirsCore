package com.airsltd.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EAirsErrorMessagesTest {

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
	public void testGetId() {
		// given
		// when
		// then
		assertEquals(1000, EAirsErrorMessages.PASSWORDINVALIDLENGTHID.getId());
	}

	@Test
	public void testGetDescription() {
		// given
		// when
		// then
		assertEquals("1001:Password Input Error:Your new password does not match your verified password", EAirsErrorMessages.PASSWORDSDONOTMATCHID.getDescription());
	}

}
