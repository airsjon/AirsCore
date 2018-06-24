package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;

public class IntegerTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private IntegerType f_integerType;

	@Before
	public void setUp() throws Exception {
		f_integerType = new IntegerType();
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
		assertEquals(Type.functionInteger(0),f_integerType.convert(null, null));
		assertEquals(Type.functionInteger(1),f_integerType.convert(Type.TRUE, null));
		assertEquals(Type.functionInteger(0),f_integerType.convert(Type.FALSE, null));
		assertEquals(Type.functionInteger((int)3.2f), 
				f_integerType.convert(Type.functionFloat(3.2f), null));
		assertEquals(Type.functionInteger(324),f_integerType.convert(Type.functionString("324"), null));
		assertEquals(Type.functionString("3de3"), f_integerType.convert(Type.functionString("3de3"), null));
		assertEquals(l_noConvertObject, f_integerType.convert(l_noConvertObject, null));
	}
	
}
