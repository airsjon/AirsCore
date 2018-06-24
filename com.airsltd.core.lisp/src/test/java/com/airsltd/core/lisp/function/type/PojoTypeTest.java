package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PojoTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private PojoType f_pojoType;

	@Before
	public void setUp() throws Exception {
		f_pojoType = new PojoType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvert() {
		// given
		Object l_object = new Object();
		// when
		// then
		assertEquals(Type.functionPojo(l_object), f_pojoType.convert(Type.functionPojo(l_object), null));
		assertEquals(Type.functionPojo(3), f_pojoType.convert(Type.functionInteger(3), null));
	}

}
