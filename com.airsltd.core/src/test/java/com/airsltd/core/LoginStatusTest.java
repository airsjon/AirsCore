package com.airsltd.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginStatusTest {

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
	public void test() {
		// given
		LoginStatus l_login = LoginStatus.CANCELED;
		// when
		// then
		assertEquals(1, l_login.ordinal());
		assertEquals(l_login, LoginStatus.valueOf("CANCELED"));
	}

}
