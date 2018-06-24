package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BooleanTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private BooleanType f_booleanType;

	@Before
	public void setUp() throws Exception {
		f_booleanType = new BooleanType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvert() {
		// given
		// when
		// then
		assertEquals(Type.FALSE,f_booleanType.convert(null, null));
		assertEquals(Type.TRUE,f_booleanType.convert(Type.TRUE, null));
		assertEquals(Type.FALSE,f_booleanType.convert(Type.FALSE, null));
		assertEquals(Type.TRUE, 
				f_booleanType.convert(Type.functionFloat(3.2f), null));
		assertEquals(Type.FALSE, 
				f_booleanType.convert(Type.functionFloat(0f), null));
		assertEquals(Type.TRUE, 
				f_booleanType.convert(Type.functionInteger(5), null));
		assertEquals(Type.FALSE, 
				f_booleanType.convert(Type.functionInteger(0), null));
		assertEquals(Type.TRUE,f_booleanType.convert(Type.functionString("324"), null));
		assertEquals(Type.FALSE, f_booleanType.convert(Type.functionString("false"), null));
		assertEquals(Type.TRUE, f_booleanType.convert(Type.functionPojo(new Object()), null));
	}

}
