package com.airsltd.core.lisp.function.comparison;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.type.Type;

public class ComparisonTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	private GreaterThan f_greaterThan;
	private LessThan f_lessThan;
	private LessThanEqual f_lessThanEqual;
	private LispEquals f_lispEquals;
	private Near f_near;
	private GreaterThanEqual f_greaterThanEqual;

	@Before
	public void setUp() throws Exception {
		f_greaterThan = new GreaterThan();
		f_greaterThanEqual = new GreaterThanEqual();
		f_lessThan = new LessThan();
		f_lessThanEqual = new LessThanEqual();
		f_lispEquals = new LispEquals();
		f_near = new Near();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFloats() {
		// given
		// when
		// then
		assertEquals(1.0f, f_greaterThan.processFloat(3.2f, 1), .0001f);
		assertEquals(1.0f, f_greaterThanEqual.processFloat(3.2f, 1), .0001f);
		assertNull(f_lessThan.processFloat(3.2f, 1));
		assertNull(f_lessThanEqual.processFloat(3.2f, 1));
		assertNull(f_lispEquals.processFloat(3.2f, 1));
		assertNull(f_near.processFloat(3.2f, 1));

		assertNull(f_greaterThan.processFloat(1, 3.2f));
		assertNull(f_greaterThanEqual.processFloat(1, 3.2f));
		assertEquals(3.2f, f_lessThan.processFloat(1, 3.2f), .0001f);
		assertEquals(3.2f, f_lessThanEqual.processFloat(1, 3.2f), .0001f);
		assertNull(f_lispEquals.processFloat(1, 3.2f));
		assertNull(f_near.processFloat(1, 3.2f));

		assertNull(f_greaterThan.processFloat(3.2f, 3.2f));
		assertEquals(3.2f, f_greaterThanEqual.processFloat(3.2f, 3.2f), .0001f);
		assertNull(f_lessThan.processFloat(3.2f, 3.2f));
		assertEquals(3.2f, f_lessThanEqual.processFloat(3.2f, 3.2f), .0001f);
		assertEquals(3.2f, f_lispEquals.processFloat(3.2f, 3.2f), .0001f);
		assertEquals(3.2f, f_near.processFloat(3.2f, 3.2f), .0001f);

	}
	
	@Test
	public void testIntegers() {
		// given
		// when
		// then
		assertEquals(1, (int)f_greaterThan.processInteger(4, 1));
		assertEquals(1, (int)f_greaterThanEqual.processInteger(4, 1));
		assertNull(f_lessThan.processInteger(4, 1));
		assertNull(f_lessThanEqual.processInteger(4, 1));
		assertNull(f_lispEquals.processInteger(4, 1));
		assertNull(f_near.processInteger(4, 1));

		assertNull(f_greaterThan.processInteger(1, 4));
		assertNull(f_greaterThanEqual.processInteger(1, 4));
		assertEquals(4, (int)f_lessThan.processInteger(1, 4));
		assertEquals(4, (int)f_lessThanEqual.processInteger(1, 4));
		assertNull(f_lispEquals.processInteger(1, 4));
		assertNull(f_near.processInteger(1, 4));

		assertNull(f_greaterThan.processInteger(4, 4));
		assertEquals(4, (int)f_greaterThanEqual.processInteger(4, 4));
		assertNull(f_lessThan.processInteger(4, 4));
		assertEquals(4, (int)f_lessThanEqual.processInteger(4, 4));
		assertEquals(4, (int)f_lispEquals.processInteger(4, 4));
		assertEquals(4, (int)f_near.processInteger(4, 4));

	}
	
	@Test
	public void testNames() {
		// given
		// when
		// then
		assertEquals("Greater Than", f_greaterThan.getFunctionName());
		assertEquals("Greater Than Equal", f_greaterThanEqual.getFunctionName());
		assertEquals("Less Than", f_lessThan.getFunctionName());
		assertEquals("Less Than Equal", f_lessThanEqual.getFunctionName());
		assertEquals("Equals", f_lispEquals.getFunctionName());
		assertEquals("Near", f_near.getFunctionName());
	}
	
	@Test
	public void testComparisonLispCode() {
		// given
		List<ISimpleValue> l_values = Arrays.asList(
				Type.functionString("2014-05-08"),
				Type.functionString("2015-01-01"),
				Type.functionString("2015-07-14"));
		List<IFunction> l_functions = Arrays.asList(
				AbstractFunction.getFunction("lessthan"),
				AbstractFunction.getFunction("lessthanequal"),
				AbstractFunction.getFunction("greaterthan"),
				AbstractFunction.getFunction("greaterthanequal"),
				AbstractFunction.getFunction("equal"),
				AbstractFunction.getFunction("near")
				);
		// when
		// then
		// TODO
	}

}
