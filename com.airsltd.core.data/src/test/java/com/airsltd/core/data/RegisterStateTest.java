package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.RegisterState;

public class RegisterStateTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		// given
		// when
		// then
		assertEquals(RegisterState.PASSWORDRESET, RegisterState.valueOf("PASSWORDRESET"));
		assertEquals(5, RegisterState.values().length);
	}

}
