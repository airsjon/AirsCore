package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;

public class ListTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private ListType f_typeList;

	@Before
	public void setUp() throws Exception {
		f_typeList = new ListType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvert() {
		// given
		ISimpleValue l_list = Type.functionList(Arrays.asList(Type.functionInteger(3)));
		// when
		// then
		assertEquals(l_list, f_typeList.convert(l_list, null));
	}
	
	@Test(expected=InvalidArgument.class)
	public void testConvert_throw() {
		// given
		// when
		// then
		f_typeList.convert(Type.functionInteger(3), null);
	}

}
