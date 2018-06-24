package com.airsltd.core.lisp.function;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.type.Type;

public class EvaluationContextTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private EvaluationContext f_context;

	@Before
	public void setUp() throws Exception {
		f_context = new EvaluationContext(null);
	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetValue() {
		// given
		// when
		f_context.setValue("testExist", Type.TRUE);
		EvaluationContext l_context = new EvaluationContext(f_context);
		l_context.setValue("test", Type.FALSE);
		// then
		assertNull(f_context.getValue("test"));
		assertEquals(Type.TRUE, f_context.getValue("testExist"));
		assertEquals(Type.TRUE, l_context.getValue("testExist"));
		assertEquals(Type.FALSE, l_context.getValue("test"));
	}

	@Test
	public void testSetValue() {
		// given
		f_context.setValue("testExist", Type.TRUE);
		EvaluationContext l_context = new EvaluationContext(f_context);
		// when
		l_context.setValue("test", Type.FALSE);
		l_context.setValue("testExist", Type.functionInteger(3));
		// then
		assertNull(l_context.getValue("anotherValue"));
		assertEquals(Type.FALSE, l_context.getValue("test"));
		assertNull(f_context.getValue("test"));
		assertEquals(Type.functionInteger(3), l_context.getValue("testExist"));
		assertEquals(Type.functionInteger(3), f_context.getValue("testExist"));
	}

	@Test
	public void testCreateValue() {
		// given
		f_context.setValue("testExist", Type.TRUE);
		EvaluationContext l_context = new EvaluationContext(f_context);
		// when
		l_context.createValue("testExist", Type.FALSE);
		// then
		assertEquals(Type.TRUE, f_context.getValue("testExist"));
		assertEquals(Type.FALSE, l_context.getValue("testExist"));
	}


}
