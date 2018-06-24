package com.airsltd.core.data;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.data.converters.StringSQLConvert;

public class StringSQLConvertTest {

	private StringSQLConvert f_stringConverter;

	@Before
	public void setUp() throws Exception {
		f_stringConverter = new StringSQLConvert(10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testToSql() {
		// given
		// when
		// then
		assertEquals("''", f_stringConverter.toSql(null,null));
		assertEquals("'test'", f_stringConverter.toSql(null,"test"));
		assertEquals("'test this '", f_stringConverter.toSql(null,"test this and other strings"));
		assertEquals("'test ''this'", f_stringConverter.toSql(null,"test 'this' and other strings"));
		
	}
	
	@Test
	public final void testFromSql() {
		// given
		// when
		String l_fromSql = "";
		String l_fromSql2 = "";
		String l_fromSql3 = "";
		try {
			l_fromSql = new StringSQLConvert(10).fromSql(null,"A Rather");
			l_fromSql2  = new StringSQLConvert(5).fromSql(null,"AStringOfSomeLength");
			l_fromSql3 = new StringSQLConvert(4).fromSql(null,null);
		} catch (ParseException e) {
			assertTrue(false);
		}
		// then
		assertEquals("A Rather", l_fromSql);
		assertEquals("AStri", l_fromSql2);
		assertEquals(null, l_fromSql3);
	}

}
