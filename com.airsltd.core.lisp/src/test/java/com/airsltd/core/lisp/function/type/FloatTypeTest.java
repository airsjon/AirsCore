package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;

public class FloatTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private FloatType f_floatType;

	@Before
	public void setUp() throws Exception {
		f_floatType = new FloatType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvert() {
		// given
		ISimpleValue l_noConvertObject = Type.functionPojo(new Object());
		// when
		// then
		assertEquals(Type.functionFloat(0),f_floatType.convert(null, null));
		assertEquals(Type.functionFloat(1),f_floatType.convert(Type.TRUE, null));
		assertEquals(Type.functionFloat(0),f_floatType.convert(Type.FALSE, null));
		assertEquals(Type.functionFloat(3.0f), 
				f_floatType.convert(Type.functionInteger(3), null));
		assertEquals(Type.functionFloat(324),f_floatType.convert(Type.functionString("324"), null));
		assertEquals(Type.functionString("3de3"), f_floatType.convert(Type.functionString("3de3"), null));
		assertEquals(l_noConvertObject, f_floatType.convert(l_noConvertObject, null));
	}

}
