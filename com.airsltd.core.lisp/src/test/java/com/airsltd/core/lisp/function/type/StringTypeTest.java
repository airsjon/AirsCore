package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	private StringType f_stringType;

	@Before
	public void setUp() throws Exception {
		f_stringType = new StringType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvert() {
		// given
		// when
		// then
		assertEquals(Type.functionString("32"), f_stringType.convert(Type.functionInteger(32),null));
		assertEquals(Type.functionString("blaarg"), f_stringType.convert(Type.functionString("blaarg"),null));
	}

}
