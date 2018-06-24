package com.airsltd.core.lisp.function.flow;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.type.Type;
import com.airsltd.core.lisp.parser.LispParser;

public class LambdaTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluate() {
		// given
		ISimpleValue l_value = LispParser.parseString("(let (x (movingaverage 4)) "
				+ "(memory x 3) (memory x 4) (memory x 5) (memory x 7) "
				+ "(aggregate x (lambda (x1 x2) (if (greaterthan x2 4) (add x1 1) x1))))");
		// when
		ISimpleValue l_retVal = null;
		l_retVal = l_value.evaluate(null);
		// then
		assertEquals(Type.functionInteger(3), l_retVal);
	}

}
