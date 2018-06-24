package com.airsltd.core.lisp.function.math;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.type.Type;

public class ThresholdTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Threshold f_threshold;

	@Before
	public void setUp() throws Exception {
		f_threshold = new Threshold();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluate() {
		// given
		// when
		// then
		assertEquals(.01f, (float)f_threshold.evaluate(Arrays.asList(Type.functionFloat(.01f)), null).getValue(null), .0001f);
		assertEquals(.01f, (float)f_threshold.evaluate(Arrays.asList(Type.functionFloat(.99f)), null).getValue(null), .0001f);
		assertEquals(0f, (float)f_threshold.evaluate(Arrays.asList(Type.functionFloat(1f)), null).getValue(null), .0001f);
		
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testProcessInteger() {
		// given
		// when
		// then
		f_threshold.processInteger(3, 5);
	}

	@Test
	public void testProcessFloat() {
		// given
		// when
		// then
		assertEquals(.05f, f_threshold.processFloat(.03f, .05f), .00001f);
		assertEquals(.05f, f_threshold.processFloat(.03f, .95f), .00001f);
		assertEquals(.01f, f_threshold.processFloat(.01f, .01f), .00001f);
		assertEquals(.01f, f_threshold.processFloat(.03f, .99f), .00001f);
	}

	@Test
	public void testProcessDouble() {
		// given
		// when
		// then
		assertEquals(.05d, f_threshold.processDouble(.03d, .05d), .00001d);
		assertEquals(.05d, f_threshold.processDouble(.03d, .95d), .00001d);
		assertEquals(.01d, f_threshold.processDouble(.01d, .01d), .00001d);
		assertEquals(.01d, f_threshold.processDouble(.03d, .99d), .00001d);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testProcessLong() {
		// given
		// when
		// then
		f_threshold.processInteger(3, 5);
	}

	@Test
	public void testGetFunctionName() {
		// given
		// when
		// then
		assertEquals("Threshold", f_threshold.getFunctionName());
	}

}
