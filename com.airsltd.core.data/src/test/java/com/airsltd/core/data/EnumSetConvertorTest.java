package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.converters.EnumSetConvertor;

public class EnumSetConvertorTest {

	enum TestEnum { FIRST, SECOND, THIRD };
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private EnumSetConvertor<TestEnum> f_convertor;

	@Before
	public void setUp() throws Exception {
		f_convertor = new EnumSetConvertor<TestEnum>(TestEnum.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSql() {
		// given
		PersistedData m_blockData = mock(PersistedData.class);
		// when
		String l_output = f_convertor.toSql(m_blockData, EnumSet.of(TestEnum.FIRST, TestEnum.SECOND));
		// then
		assertEquals("3", l_output);
	}

	@Test
	public void testFromSql() throws ParseException {
		// given
		PersistedData m_blockData = mock(PersistedData.class);
		// when
		Set<TestEnum> l_one = f_convertor.fromSql(m_blockData, "3");
		Set<TestEnum> l_two = f_convertor.fromSql(m_blockData, "4");
		// then
		assertEquals(EnumSet.of(TestEnum.FIRST, TestEnum.SECOND), l_one);
		assertEquals(EnumSet.of(TestEnum.THIRD), l_two);
	}

}
