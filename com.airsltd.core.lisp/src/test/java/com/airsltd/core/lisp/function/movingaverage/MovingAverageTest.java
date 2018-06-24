package com.airsltd.core.lisp.function.movingaverage;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.type.Type;
import com.airsltd.core.lisp.parser.LispParser;

public class MovingAverageTest {

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
	public void testEvaluate() {
		// given
		ISimpleValue l_value = LispParser.parseString("(let (x (movingaverage 3)) "
				+ "(memory x 3) (memory x 4) (memory x 5) (memory x 7) "
				+ "(aggregate x \"add\"))");
		ISimpleValue l_value2 = LispParser.parseString("(let (x (movingaverage 3)) "
				+ "(memory x 3) (memory x 4) (memory x 5) (memory x 7) "
				+ "(aggregate x \"multiply\"))");
		ISimpleValue l_value3 = LispParser.parseString("(let (x (movingaverage 3)) "
				+ "(memory x 3) (memory x 4) (memory x 5) (memory x 7) "
				+ "(aggregate x (lambda (total in) (multiply total (if (greaterthan in 4) in 1)))))");
		// when
		ISimpleValue l_retVal = null;
		ISimpleValue l_retVal2 = null;
		ISimpleValue l_retVal3 = null;
		l_retVal = l_value.evaluate(null);
		l_retVal2 = l_value2.evaluate(null);
		l_retVal3 = l_value3.evaluate(null);
		// then
		assertEquals(Type.functionInteger(17), l_retVal);
		assertEquals(Type.functionInteger(140), l_retVal2);
		assertEquals(Type.functionInteger(35), l_retVal3);
	}

	@Test
	public void testGetFunctionName() {
		// given
		// when
		// then
		assertEquals("Memory", AbstractFunction.getFunction("memory").getFunctionName());
	}

}
