package com.airsltd.core.lisp.function.logical;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;
import com.airsltd.core.lisp.parser.LispParser;

public class LogicalTests {

	String[] f_code = new String[] {
			"(and 3 aValue)",
			"(if aValue 4 5)",
			"(if aValue 4)",
			"(xor aValue 4)",
			"(xor aValue false)",
			"(or aValue false)",
			"(not true)", 
			"(not false)"
	};
	
	ISimpleValue[][] f_expected = {
			{
				Type.functionInteger(4), 
				Type.TRUE,
				Type.functionInteger(4),
				Type.functionInteger(4),
				Type.FALSE,
				Type.TRUE,
				Type.TRUE,
				Type.FALSE,
				Type.TRUE
			},
			{
				Type.FALSE,
				Type.FALSE,
				Type.functionInteger(5),
				null,
				Type.TRUE,
				Type.FALSE,
				Type.FALSE,
				Type.FALSE,
				Type.TRUE
			},
			{
				Type.TRUE,
				Type.TRUE,
				Type.functionInteger(4),
				Type.functionInteger(4),
				Type.FALSE,
				Type.TRUE,
				Type.TRUE,
				Type.FALSE,
				Type.TRUE
			}
	};

	private String[] f_badCode = {
			"(if 3)",
			"(xor 1)",
			"(not 3 4)",
			"(or 2)"

	};
	
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
		// given
		EvaluationContext l_context = new EvaluationContext(null);
		ISimpleValue m_andValue = Type.functionInteger(4);
		l_context.createValue("aValue", m_andValue);
		// when
		// then
		List<ISimpleValue> l_codeList = new ArrayList<ISimpleValue>();
		for (String l_codeString : f_code) {
			l_codeList.add(LispParser.parseString(l_codeString));
		}
		for (ISimpleValue[] l_expectedArray : f_expected) {
			boolean firstp = true;
			int l_codeIndex = 0;
			for (ISimpleValue l_simpleValue : l_expectedArray) {
				if (firstp) {
					l_context.setValue("aValue", l_simpleValue);
					firstp = false;
				} else {
					ISimpleValue l_value = l_codeList.get(l_codeIndex).evaluate(l_context);
					assertEquals("Failed at "+f_code[l_codeIndex]+" expecting "+l_simpleValue+" returned "+l_value,
							l_simpleValue, l_value);
					l_codeIndex++;
				}
			}
		}
	}

	@Test
	public final void testGetFunctionNames() {
		// given
		new LispParser();
		// when
		IFunction l_andFunction = AbstractFunction.getFunction("and");
		IFunction l_ifFunction = AbstractFunction.getFunction("if");
		IFunction l_xorFunction = AbstractFunction.getFunction("xor");
		IFunction l_orFunction = AbstractFunction.getFunction("or");
		IFunction l_notFunction = AbstractFunction.getFunction("not");
		
		// then
		assertEquals("And", l_andFunction.getFunctionName());
		assertEquals("If", l_ifFunction.getFunctionName());
		assertEquals("Xor", l_xorFunction.getFunctionName());
		assertEquals("Or", l_orFunction.getFunctionName());
		assertEquals("Not", l_notFunction.getFunctionName());

	}
	
	@Test
	public final void testBadParameters() {
		// given
		// when
		for (String l_badCode : f_badCode ) {
			try {
				LispParser.parseString(l_badCode).evaluate(null);
				assertTrue(l_badCode + " did not fail", false);
			} catch (InvalidParameters l_ip) {
			}
		}

	}
	
}
