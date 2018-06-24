package com.airsltd.core.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AirsExtBlockInializationExceptionTest {

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
	public void testAirsExtBlockInializationException() {
		// given
		// when
		AirsExtBlockInializationException l_exception = new AirsExtBlockInializationException(MockExtData.class);
		// then
		assertEquals("Unable to find IComplexDataHooks for class: com.airsltd.core.data.MockExtData", l_exception.getMessage());
	}

}
