package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlockDataTest {

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
	public final void testToSqlInt() {
		assertEquals("5", BlockData.toSql(5));
		assertEquals("0", BlockData.toSql(0));
		assertEquals("-30", BlockData.toSql(-30));
	}

	@Test
	public final void testToSqlLong() {
		assertEquals("5", BlockData.toSql(5l));
		assertEquals("0", BlockData.toSql(0l));
		assertEquals("-30", BlockData.toSql(-30l));
		assertEquals("68719476736", BlockData.toSql(0x1000000000l));
	}

	@Test
	public final void testToSqlBoolean() {
		assertEquals("1",BlockData.toSql(true));
		assertEquals("0",BlockData.toSql(false));
	}

	@Test
	public final void testToSqlString() {
		// given
		String l_test = "A string of some length";
		assertEquals("''", BlockData.toSql(""));
		assertEquals("''", BlockData.toSql((String)null));
		assertEquals("'test'", BlockData.toSql("test"));
		assertEquals("'For''h'", BlockData.toSql("For'h"));
		assertEquals("'"+l_test.substring(0,10)+"'", BlockData.toSql(l_test,10));
		assertEquals("''", BlockData.toSql(null,10));
		assertEquals("'Small'", BlockData.toSql("Small",10));
	}
	
	@Test
	public final void testToSqlByteArray() {
		// given
		byte l_singleQuote = '\'';
		byte l_doubleQuote = '"';
		// when
		// then
		assertEquals("''", BlockData.toSql(new byte[] { 3, 4, 5} ));
		assertEquals("'''''@I'", BlockData.toSql(new byte[] { 3, l_singleQuote, l_singleQuote, 64, 73} ));
		assertEquals("'''\"AI'", BlockData.toSql(new byte[] { l_singleQuote, l_doubleQuote, 65, 73} ));
		assertEquals("'\0\0'", BlockData.toSql(new byte[] { 3, 0, 4, 0, 5 } ));
	}
	
	@Test
	public final void testFromSqlByteArray() {
		// given
		byte l_singleQuote = '\'';
		byte l_doubleQuote = '"';
		// when
		// then
		assertArrayEquals(new byte[] { 3, 4, 5}, BlockData.fromSqlByteArray(""));
		assertArrayEquals(new byte[] { 3, l_singleQuote, l_singleQuote, 64, 73}, BlockData.fromSqlByteArray("''@I"));
		assertArrayEquals(new byte[] { l_singleQuote, l_doubleQuote, 65, 73}, BlockData.fromSqlByteArray("'\"AI"));
		assertArrayEquals(new byte[] { 3, 0, 4, 0, 5}, BlockData.fromSqlByteArray("\0\0"));
	}
	
	@Test
	public final void testToSqlPasswordString() {
		// given
		PasswordString l_test = new PasswordString("A string of some length");
		assertEquals("''", BlockData.toSql(new PasswordString("")));
		assertEquals("''", BlockData.toSql((PasswordString)null));
		assertEquals("'A string of some length'", BlockData.toSql(l_test));
	}

	@Test
	public final void testToSqlTimestamp() {
		assertEquals("NULL", BlockData.toSql((Timestamp)null));
		assertEquals("'1969-12-31 16:16:23'", BlockData.toSql(new Timestamp(0xf0000)));
	}
	
	@Test
	public final void testToSqlChar() {
		assertEquals("'a'", BlockData.toSql('a'));
	}
	
	@Test
	public final void testToSqlFloat() {
		assertEquals("1.0", BlockData.toSql(1f));
		assertEquals("0.0", BlockData.toSql(0f));
		assertEquals("-3.14175", BlockData.toSql(-3.14175f));
		assertEquals("100.0", BlockData.toSql(100f));
		assertEquals("1.45671", BlockData.toSql(1.45671f));
	}
	
	@Test
	public final void testStringCompare() {
		// given
		String l_string1 = "A string of some length";
		String l_string2 = "A string";
		// when
		assertTrue(BlockData.stringCompare(l_string2, l_string1.substring(0,8)));
		assertFalse(BlockData.stringCompare(l_string1, l_string2));
		assertFalse(BlockData.stringCompare(l_string1, null));
		assertFalse(BlockData.stringCompare(null, l_string2));
		assertFalse(BlockData.stringCompare(l_string1, ""));
		assertFalse(BlockData.stringCompare("", l_string2));
		assertTrue(BlockData.stringCompare(null, null));
		assertTrue(BlockData.stringCompare(null, ""));
		assertTrue(BlockData.stringCompare("", null));
		// then
	}

	@Test
	public final void testObjectCompare() {
		// given
		Calendar c = new GregorianCalendar(1963, 8, 20);
		Date l_dateOne = c.getTime();
		c = new GregorianCalendar(1963, 8, 20);
		Date l_dateTwo = c.getTime();
		c = new GregorianCalendar(1964, 8, 20);
		Date l_dateThree = c.getTime();
		// when
		assertTrue(BlockData.objectCompare(l_dateOne, l_dateTwo));
		assertTrue(BlockData.objectCompare(null, null));
		assertFalse(BlockData.objectCompare(l_dateOne, l_dateThree));
		assertFalse(BlockData.objectCompare(l_dateTwo, l_dateThree));
		assertFalse(BlockData.objectCompare(l_dateOne, null));
		assertFalse(BlockData.objectCompare(null, l_dateThree));
		
		// then
	}

	@Test
	public final void testToSqlDate() {
		assertEquals("NULL", BlockData.toSql((Date)null));
		Calendar c = new GregorianCalendar(1963, 8, 20);
		assertEquals("'1963-09-20'", BlockData.toSql(c.getTime()));
	}

	@Test
	public final void testToSqlIPersistentId() {
		IPersistentId mockPersistentId = mock(IPersistentId.class);
		// given
		
		given(mockPersistentId.getPersistentID()).willReturn(52l, 13l, 18l);
		
		// when
		
		// then
		
		assertEquals("0", BlockData.toSql((IPersistentId)null));
		assertEquals("52", BlockData.toSql(mockPersistentId));
		assertEquals("13", BlockData.toSql(mockPersistentId));
		assertEquals("18", BlockData.toSql(mockPersistentId));
	}
	
	@Test
	public final void testFromSqlInt() {
		// given
		// when
		try {
			BlockData.fromSqlInt("test");
			assertTrue(false);
		} catch (NumberFormatException e) {
		}
		assertEquals(5, BlockData.fromSqlInt("5"));
		// then
	}

	@Test
	public final void testFromSqlLong() {
		// given
		// when
		try {
			BlockData.fromSqlLong("test");
			assertTrue(false);
		} catch (NumberFormatException e) {
		}
		assertEquals(5l, BlockData.fromSqlLong("5"));
		// then
	}

	@Test
	public final void testFromSqlFloat() {
		// given
		// when
		try {
			BlockData.fromSqlFloat("test");
			assertTrue(false);
		} catch (NumberFormatException e) {
		}
		assertEquals(5.342f, BlockData.fromSqlFloat("5.342"),.001);
		// then
	}
	
	@Test
	public final void testFromSqlString() {
		// given
		// when
		assertEquals("Test", BlockData.fromSqlString("'Test'"));
		assertEquals("'Test", BlockData.fromSqlString("'Test"));
		assertEquals("Test", BlockData.fromSqlString("Test"));
		assertEquals("", BlockData.fromSqlString(""));
		assertEquals(null, BlockData.fromSqlString(null));
	}

	@Test
	public final void testFromSqlBoolean() {
		// given
		// when
		assertEquals(true, BlockData.fromSqlBoolean("1"));
		assertEquals(false, BlockData.fromSqlBoolean("2"));
		assertEquals(false, BlockData.fromSqlBoolean("0"));
		assertEquals(false, BlockData.fromSqlBoolean(""));
		assertEquals(false, BlockData.fromSqlBoolean(null));
		// then
	}
	
	@Test
	public final void testFromSqlTimestamp() {
		// given
		Date l_date1 = new GregorianCalendar(1963, 8, 20, 0, 0).getTime();
		Date l_date2 = new GregorianCalendar(1963, 8, 20, 8, 30).getTime();
		boolean l_shouldFail = false;
		// when
		try {
			assertEquals(l_date1.getTime(), BlockData.fromSqlTimestamp("1963-09-20 00:00:00").getTime());
			assertEquals(l_date2.getTime(), BlockData.fromSqlTimestamp("1963-09-20 08:30:00").getTime());
			assertNull(BlockData.fromSqlTimestamp("0000-00-00"));
			l_shouldFail = true;
			assertNull(BlockData.fromSqlTimestamp("junk"));
		} catch (ParseException p_exception) {
			assertTrue(l_shouldFail);
		}
	}
	
	@Test
	public final void testIsEmptyString() {
		// given
		// when
		// then
		assertTrue(BlockData.isEmptyString(null));
		assertTrue(BlockData.isEmptyString(""));
		assertFalse(BlockData.isEmptyString("a"));
	}
	
	@Test
	public final void testToListStr() {
		// given
		List<MockStringKey> l_list = new ArrayList<>();
		l_list.add(new MockStringKey("test"));
		l_list.add(new MockStringKey("a"));
		l_list.add(new MockStringKey("of"));
		l_list.add(new MockStringKey("some"));
		l_list.add(new MockStringKey("import"));
		
		// when
		String l_retVal = BlockData.toListStr(l_list);
		// then
		assertEquals("(a,import,of,some,test)", l_retVal);
	}
	
	@Test
	public final void testUnquote() {
		// given
		String[] l_inString = new String[] { "'a'", "'b", "c", "", "d'", "'test'" };
		// when
		String[] l_outString = BlockData.unquote(l_inString);
		// then
		assertArrayEquals(new String[] {"a",  "'b", "c", "", "d'", "test"} , l_outString);
	}
	
	@Test
	public final void testStringTrimCompare() {
		// given
		// when
		// then
		assertTrue(BlockData.stringTrimCompare(null, null));
		assertFalse(BlockData.stringTrimCompare(null, "a"));
		assertFalse(BlockData.stringTrimCompare("a", null));
		assertTrue(BlockData.stringTrimCompare(" 			a ", "a     "));
		assertTrue(BlockData.stringTrimCompare("b", "b\n\n\n"));
		assertFalse(BlockData.stringTrimCompare("  a b	", "		ab	"));
	}
	
	/**
	 * Test method for {@link com.airsltd.core.AbstractBlockData#validateBoolean(java.lang.Object)}.
	 */
	@Test
	public final void testValidateBoolean() {
		// given
		// when
		assertTrue(BlockData.validateBoolean(Boolean.TRUE));
		assertTrue(BlockData.validateBoolean(Boolean.FALSE));
		assertTrue(BlockData.validateBoolean("1"));
		assertTrue(BlockData.validateBoolean("0"));
		assertFalse(BlockData.validateBoolean(null));
		assertFalse(BlockData.validateBoolean(3));
		assertFalse(BlockData.validateBoolean("True"));
		assertFalse(BlockData.validateBoolean("False"));
		assertFalse(BlockData.validateBoolean(new Object()));
		// then
	}

	
	/**
	 * Test method for {@link com.airsltd.core.AbstractBlockData#booleanValue(java.lang.Object)}.
	 */
	@Test
	public final void testBooleanValue() {
		// given
		// when
		assertTrue(BlockData.booleanValue(Boolean.TRUE));
		assertTrue(BlockData.booleanValue("1"));
		assertFalse(BlockData.booleanValue(Boolean.FALSE));
		assertFalse(BlockData.booleanValue("0"));
		// then
	}

}
