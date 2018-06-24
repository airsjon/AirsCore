package com.airsltd.core.lisp.parser;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

public class LispParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private LispParser f_parser;

	@Before
	public void setUp() throws Exception {
		f_parser = new LispParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLispParser() {
		// given
		String l_stringOne = "(test this out \"a string\" 34 3.2)";
		String l_stringTwo = "((52) -32 +33 -3.4f +5f)";
		String l_stringThree = "(if (lessthan game_PLAYERRATING system_rank) 0"
				+ "                             (add system_accumIntegerB 1))";
		// when
		String[] l_retOne = f_parser.getTokensAsStrings(l_stringOne);
		String[] l_retTwo = f_parser.getTokensAsStrings(l_stringTwo);
		String[] l_retThree = f_parser.getTokensAsStrings(l_stringThree);
		String[] l_expectedOne = new String[] {
				":list", ":token", "test", ":token", "this", ":token", "out", ":string",
				"a string", ":integer", "34", ":float", "3.2", ":eoList"
			};
		String[] l_expectedTwo = new String[] {
				":list", ":list", ":integer", "52", ":eoList", ":integer", "-32",
				":integer", "+33", ":float", "-3.4f", ":float", "+5f", ":eoList"
			};
		String[] l_expectedThree = new String[] {
				":list", ":token", "if", ":list", ":token", "lessthan", ":token",
				"game_PLAYERRATING", ":token", "system_rank", ":eoList", ":integer", "0", ":list",
				":token", "add", ":token", "system_accumIntegerB", ":integer", "1", ":eoList", ":eoList"
			};
		// then
		assertArrayEquals(l_expectedOne, l_retOne);
		assertArrayEquals(l_expectedTwo, l_retTwo);
		assertArrayEquals(l_expectedThree, l_retThree);
	}

	@Test
	public void testParseString() {
		// given
		// when
		ISimpleValue l_retVal = LispParser.parseString("(add 3 4)");
		ISimpleValue l_retVal2 = LispParser.parseString("(let (x 3 y 4) (add x y))");
		ISimpleValue l_retVal3 = LispParser.parseString("(add \"3\" 4)");
		try {
			ISimpleValue l_realVal = l_retVal.evaluate(null);
			ISimpleValue l_realVal2 = l_retVal2.evaluate(null);
			ISimpleValue l_realVal3 = l_retVal3.evaluate(null);
			
			// then
			assertEquals(7, (int)Type.toType(Integer.class, l_realVal, null));
			assertEquals(7, (int)Type.toType(Integer.class, l_realVal2, null));
			assertEquals(7, (int)Type.toType(Integer.class, l_realVal3, null));
		} catch (InvalidArgument | InvalidParameters e) {
			e.printStackTrace();
			assertTrue(false);
		}
		// then
	}
	
	// test (and system_rankObtained (lessthan game_PLAYERRATING (subtract system_rank 1)))
	@Test
	public void testRankFailTest1() {
		// given
		EvaluationContext l_context = new EvaluationContext(null);
		ISimpleValue l_valueUnderTest = LispParser.parseString("(and system_rankObtained (lessthan game_PLAYERRATING (subtract system_rank 1)))");
		try {
			// when
			l_context.setValue("system_rankObtained", Type.functionInteger(0));
			l_context.setValue("game_PLAYERRATING", Type.functionFloat(3.2f));
			l_context.setValue("system_rank", Type.functionInteger(4));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			// when
			l_context.setValue("system_rankObtained", Type.functionInteger(10));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			// when
			l_context.setValue("game_PLAYERRATING", Type.functionFloat(2.3f));
			// then
			assertEquals(Type.TRUE, l_valueUnderTest.evaluate(l_context));
		} catch (InvalidArgument | InvalidParameters e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	// test (and system_rankObtained (lessthan game_PLAYERRATING (subtract system_rank 1)))
	@Test
	public void testFalsePositiveTest1() {
		// given
		EvaluationContext l_context = new EvaluationContext(null);
		ISimpleValue l_valueUnderTest = LispParser.parseString("(body (set system_accumIntegerB (if (lessthan game_PLAYERRATING system_rank) 0"
				+ "                             (add system_accumIntegerB 1)))"
				+ "      (and (not (equals 0 system_previousRankObtained))"
				+ "      	  (greaterthan system_accumIntegerB 10)"
				+ "           (greaterthanequal system_gameIndex system_previousRankObtained)))");
		try {
			// when
			l_context.setValue("system_accumIntegerB", Type.functionInteger(0));
			l_context.setValue("system_previousRankObtained", Type.functionInteger(0));
			l_context.setValue("game_PLAYERRATING", Type.functionFloat(4.2f));
			l_context.setValue("system_rank", Type.functionInteger(4));
			l_context.setValue("system_gameIndex", Type.functionInteger(52));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)1, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerB"), l_context));
			// when
			l_context.setValue("system_previousRankObtained", Type.functionInteger(53));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)2, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerB"), l_context));
			// when
			l_context.setValue("system_accumIntegerB", Type.functionInteger(9));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)10, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerB"), l_context));
			// when
			l_context.setValue("system_gameIndex", Type.functionInteger(53));
			// then
			assertEquals(Type.TRUE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)11, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerB"), l_context));
			// when
			l_context.setValue("game_PLAYERRATING", Type.functionFloat(3.2f));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)0, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerB"), l_context));
		} catch (InvalidArgument | InvalidParameters e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public final void testRankObtained1() {
		// given
		EvaluationContext l_context = new EvaluationContext(null);
		ISimpleValue l_valueUnderTest = LispParser.parseString("(body (let (newValue system_accumIntegerA)" +
		   "(if (equals game_Result 0)" +
		       "(if (greaterthanequal game_QUALITY system_rank) (set newValue (add newValue 1)))" +
		       "(if (and (equals game_Result 1) (lessthanequal game_QUALITY system_rank)) (set newValue 0)))" +
		   "(set system_accumIntegerA newValue))" +
		  "(greaterthanequal system_accumIntegerA 4))");
		try {
			// when
			l_context.setValue("system_accumIntegerA", Type.functionInteger(3));
			l_context.setValue("game_Result", Type.functionInteger(0));
			l_context.setValue("game_QUALITY", Type.functionFloat(4.2f));
			l_context.setValue("system_rank", Type.functionFloat(4));
			// then
			assertEquals(Type.TRUE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)4, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerA"), l_context));
			// when
			l_context.setValue("system_accumIntegerA", Type.functionInteger(0));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)1, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerA"), l_context));
			// when
			l_context.setValue("game_QUALITY", Type.functionFloat(3.2f));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)1, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerA"), l_context));
			// when
			l_context.setValue("game_Result", Type.functionInteger(1));
			l_context.setValue("game_QUALITY", Type.functionFloat(5.2f));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)1, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerA"), l_context));
			// when
			l_context.setValue("game_QUALITY", Type.functionFloat(3.2f));
			// then
			assertEquals(Type.FALSE, l_valueUnderTest.evaluate(l_context));
			assertEquals((int)0, (int)Type.toType(Integer.class, l_context.getValue("system_accumIntegerA"), l_context));
		} catch (InvalidArgument | InvalidParameters e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	

}

