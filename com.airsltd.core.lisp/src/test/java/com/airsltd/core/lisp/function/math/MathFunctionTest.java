package com.airsltd.core.lisp.function.math;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;
import com.airsltd.core.lisp.parser.LispParser;

public class MathFunctionTest {

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
	public void test() {
		try {
			// given
			ISimpleValue l_bitwiseAnd = LispParser.parseString("(bitwiseand 77 136)");
			ISimpleValue l_bitwiseOr = LispParser.parseString("(bitwiseor 77 136)");
			ISimpleValue l_bitwiseXor = LispParser.parseString("(bitwisexor 77 136)");
			ISimpleValue l_add = LispParser.parseString("(add 77 136)");
			ISimpleValue l_subtract = LispParser.parseString("(subtract 77 136)");
			ISimpleValue l_multiply = LispParser.parseString("(multiply 77 136)");
			ISimpleValue l_divide = LispParser.parseString("(divide 77 136)");
			ISimpleValue l_increment = LispParser.parseString("(inc 77)");
			ISimpleValue l_decrement = LispParser.parseString("(dec 77)");
			ISimpleValue l_add2 = LispParser.parseString("(add 77.3 136)");
			ISimpleValue l_addDate = LispParser.parseString("(add \"1963-09-20\" 1)");
			ISimpleValue l_subtract2 = LispParser.parseString("(subtract 77 136.3)");
			ISimpleValue l_multiply2 = LispParser.parseString("(multiply 77.2 136.1)");
			ISimpleValue l_divide2 = LispParser.parseString("(divide 77.4 136.5)");
			ISimpleValue l_add3 = LispParser.parseString("(add \"77.3\" 136)");
			IFunction l_bitwiseAndFunction = AbstractFunction.getFunction("bitwiseand");
			IFunction l_bitwiseOrFunction = AbstractFunction.getFunction("bitwiseor");
			IFunction l_bitwiseXorFunction = AbstractFunction.getFunction("bitwisexor");
			IFunction l_addFunction = AbstractFunction.getFunction("add");
			IFunction l_multiplyFunction = AbstractFunction.getFunction("multiply");
			IFunction l_divideFunction = AbstractFunction.getFunction("divide");
			IFunction l_subtractFunction = AbstractFunction.getFunction("subtract");
			IFunction l_incrementFunction = AbstractFunction.getFunction("inc");
			IFunction l_decrementFunction = AbstractFunction.getFunction("dec");
			// when
			// then
			assertEquals("Bitwise And", l_bitwiseAndFunction.getFunctionName());
			assertEquals("Bitwise Or", l_bitwiseOrFunction.getFunctionName());
			assertEquals("Bitwise Xor", l_bitwiseXorFunction.getFunctionName());
			assertEquals("Add", l_addFunction.getFunctionName());
			assertEquals("Multiply", l_multiplyFunction.getFunctionName());
			assertEquals("Divide", l_divideFunction.getFunctionName());
			assertEquals("Subtract", l_subtractFunction.getFunctionName());
			assertEquals("Increment", l_incrementFunction.getFunctionName());
			assertEquals("Decrement", l_decrementFunction.getFunctionName());
			assertEquals(Type.functionInteger(8), l_bitwiseAnd.evaluate(null));
			assertEquals(Type.functionInteger(205), l_bitwiseOr.evaluate(null));
			assertEquals(Type.functionInteger(197), l_bitwiseXor.evaluate(null));
			assertEquals(Type.functionInteger(213), l_add.evaluate(null));
			assertEquals(Type.functionInteger(10472), l_multiply.evaluate(null));
			assertEquals(Type.functionInteger(0), l_divide.evaluate(null));
			assertEquals(Type.functionInteger(-59), l_subtract.evaluate(null));
			assertEquals(Type.functionInteger(78), l_increment.evaluate(null));
			assertEquals(Type.functionInteger(76), l_decrement.evaluate(null));
			assertEquals(Type.functionFloat(213.3f), l_add2.evaluate(null));
			assertEquals(Type.functionFloat(213.3f), l_add3.evaluate(null));
			assertEquals(Type.functionFloat(10506.92f), l_multiply2.evaluate(null));
			assertEquals(Type.functionFloat(0.567033f), l_divide2.evaluate(null));
			assertEquals(Type.functionFloat(-59.300003f), l_subtract2.evaluate(null));
			assertEquals("(BODY (INC 77))", l_increment.toString());
			assertEquals(new SimpleValue(-198262799999l, Type.LONG), l_addDate.evaluate(null));
		} catch (InvalidArgument | InvalidParameters e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public final void testLoadFunctionList() {
		// given
		// when
		ISimpleValue l_functionCall = LispParser.parseString("(bitwiseand 77 136)");
		/// then
		assertEquals("(BODY (BITWISEAND 77 136))", l_functionCall.toString());
	}

	@Test
	public final void testClosestType() {
		// given
		MathFunction l_addFunction = (MathFunction)AbstractFunction.getFunction("add");
		// when
		// the
		assertEquals(Type.INTEGER, l_addFunction.closestType(Type.functionString("32"), null));
		assertEquals(Type.LONG, l_addFunction.closestType(Type.functionString("-5000000000"), null));
		assertEquals(Type.FLOAT, l_addFunction.closestType(Type.functionString("3.45"), null));
		assertEquals(Type.DOUBLE, l_addFunction.closestType(Type.functionString("3.45E39"), null));
		assertEquals(Type.DATE, l_addFunction.closestType(Type.functionString("1963-09-20"), null));
	}
}
