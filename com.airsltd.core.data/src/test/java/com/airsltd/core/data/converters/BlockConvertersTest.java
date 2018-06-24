/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.data.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.AutoIncrementField;
import com.airsltd.core.data.BlockDataTest;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.IPersistentId;
import com.airsltd.core.data.PasswordString;
import com.airsltd.core.data.AbstractBlockDataTest.LocalEnum;

/**
 * @author Jon Boley
 *
 */
public class BlockConvertersTest {

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
	public final void testConverters() throws ParseException {
		// given
		IPersistentId m_id = mock(IPersistentId.class);
		given(m_id.getPersistentID()).willReturn(1523l);
		Properties l_property = new Properties();
		l_property.put("here", "we");
		l_property.put("go", "again");
		l_property.put("old", "mc'donald");
		l_property.put("\"elf\"", "english");
		
		// when
		String l_propertyAsSql = BlockConverters.PROPERTYCONVERT.toSql(null,l_property);
		// then
		assertEquals("42", BlockConverters.LONGCONVERTER.toSql(null,42l));
		assertEquals(32l, BlockConverters.LONGCONVERTER.fromSql(null,"32").longValue());
		try {
			BlockConverters.LONGCONVERTER.fromSql(null,"32x5");
			assertTrue(false);
		} catch (ParseException e1) {
			assertEquals("Unable to parse 32x5 as a Long", e1.getMessage());
		}
		assertEquals("NULL", BlockConverters.DATECONVERTER.toSql(null,null));
		assertEquals("'1963-09-20'", BlockConverters.DATECONVERTER.toSql(null,new java.sql.Date(new GregorianCalendar(1963,8,20).getTime().getTime())));
		assertEquals(new java.sql.Date(new GregorianCalendar(2002,01,14).getTimeInMillis()),
				BlockConverters.DATECONVERTER.fromSql(null,"2002-02-14"));
		assertEquals("-5", BlockConverters.INTEGERCONVERT.toSql(null,-5));
		assertEquals(32, BlockConverters.INTEGERCONVERT.fromSql(null,"32").intValue());
		try {
			BlockConverters.INTEGERCONVERT.fromSql(null,"32x5");
			assertTrue(false);
		} catch (ParseException e1) {
			assertEquals("Unable to parse 32x5 as a Integer", e1.getMessage());
		}
		assertEquals("1", BlockConverters.BOOLEANCONVERTER.toSql(null,true));
		assertEquals("0", BlockConverters.BOOLEANCONVERTER.toSql(null,false));
		assertTrue(BlockConverters.BOOLEANCONVERTER.fromSql(null,"1"));
		assertFalse(BlockConverters.BOOLEANCONVERTER.fromSql(null,"0"));
		assertFalse(BlockConverters.BOOLEANCONVERTER.fromSql(null,null));
		assertFalse(BlockConverters.BOOLEANCONVERTER.fromSql(null,"eight"));
		assertFalse(BlockConverters.BOOLEANCONVERTER.fromSql(null,"TRUE"));
		assertEquals("'c'", BlockConverters.CHARACTERCONVERTER.toSql(null,'c'));
		assertEquals('t',BlockConverters.CHARACTERCONVERTER.fromSql(null,"true").charValue());
		assertEquals('f',BlockConverters.CHARACTERCONVERTER.fromSql(null,"false").charValue());
		assertNull(BlockConverters.CHARACTERCONVERTER.fromSql(null,null));
		assertNull(BlockConverters.CHARACTERCONVERTER.fromSql(null,""));
		EnumConverter<LocalEnum> l_enumConverter = new EnumConverter<LocalEnum>(LocalEnum.class);
		assertEquals("0",l_enumConverter.toSql(null,LocalEnum.ONE));
		assertEquals("1",l_enumConverter.toSql(null,LocalEnum.TWO));
		assertEquals(LocalEnum.ONE, l_enumConverter.fromSql(null,"0"));
		assertEquals(LocalEnum.TWO, l_enumConverter.fromSql(null,"1"));
		assertEquals(LocalEnum.THREE, l_enumConverter.fromSql(null,"2"));
		try {
			l_enumConverter.fromSql(null,"x");
			assertTrue(false);
		} catch (ParseException e) {
			assertEquals("Unable to parse enum from x", e.getMessage());
		}
		assertEquals("NULL", BlockConverters.TIMESTAMPCONVERT.toSql(null,null));
		assertEquals("'1963-09-20 00:00:00.0'", BlockConverters.TIMESTAMPCONVERT.toSql(null,new Timestamp(new GregorianCalendar(1963,8,20).getTime().getTime())));
		assertEquals(new Timestamp(new GregorianCalendar(1963,8,20,5,30,22).getTimeInMillis()), BlockConverters.TIMESTAMPCONVERT.fromSql(null,"1963-09-20 5:30:22"));
		assertEquals(new Timestamp(new GregorianCalendar(1963,8,20,14,30,22).getTimeInMillis()), BlockConverters.TIMESTAMPCONVERT.fromSql(null,"1963-09-20 14:30:22"));
		assertEquals(new Timestamp(new GregorianCalendar(1963,8,20).getTimeInMillis()), BlockConverters.TIMESTAMPCONVERT.fromSql(null,"1963-09-20 0:0:0"));
		assertNull(BlockConverters.TIMESTAMPCONVERT.fromSql(null,"0000-00-00"));
		assertEquals("4.23", BlockConverters.FLOATCONVERT.toSql(null,4.23f));	
		double l_floatValue = BlockConverters.FLOATCONVERT.fromSql(null,"3.2").doubleValue();
		assertEquals(3.2d, l_floatValue, 0.005d);
		try {
			BlockConverters.FLOATCONVERT.fromSql(null,"3.2x5");
			assertTrue(false);
		} catch (ParseException e1) {
			assertEquals("Unable to parse 3.2x5 as a Float", e1.getMessage());
		}
		l_propertyAsSql = l_propertyAsSql.replace("''", "'");
		assertEquals(l_property, BlockConverters.PROPERTYCONVERT.fromSql(null,l_propertyAsSql.substring(1,l_propertyAsSql.length()-1)));
		assertEquals(new Properties(), BlockConverters.PROPERTYCONVERT.fromSql(null,""));
		assertEquals(new Properties(), BlockConverters.PROPERTYCONVERT.fromSql(null,null));
		assertEquals("nothing done", BlockConverters.TEXTCONVERT.fromSql(null,"nothing done"));
		assertEquals(new PasswordString("test"), BlockConverters.PASSWORDCONVERT.fromSql(null,"test"));
	}
	@Test
	public final void testTextConvert() {
		// given
		// when
		// then
		assertEquals("'''test'''", BlockConverters.TEXTCONVERT.toSql(null,"'test'"));
	}

	@Test
	public final void testPasswordConvert() {
		// given
		PasswordString l_ps = mock(PasswordString.class);
		given(l_ps.getPassword()).willReturn("alpha");
		// when
		// then
		assertEquals("'alpha'", BlockConverters.PASSWORDCONVERT.toSql(null,l_ps));
		assertEquals("''", BlockConverters.PASSWORDCONVERT.toSql(null,null));
	}
	
	@Test
	public final void testCallBackConvert() {
		// given
		// when
		// then
		assertEquals("?", BlockConverters.CALLBACKCONVERT.toSql(null,"'test'"));
		try {
			BlockConverters.CALLBACKCONVERT.fromSql(null,"nothing to convert");
			assertTrue(false);
		} catch (ParseException e) {
		}
	}

	@Test
	public final void testGetAutoIncrementConverter() {
		// given
		AutoIncrementField.clear();
		IBlockData l_parent = mock(IBlockData.class);
		AutoIncrementField l_field = new AutoIncrementField(BlockDataTest.class, 5);
		// when
		IDatabaseConverter<IBlockData, AutoIncrementField> l_converter = BlockConverters.getAutoIncrementConverter(BlockDataTest.class);
		
		// then
		assertEquals("5", l_converter.toSql(l_parent, l_field));
		try {
			assertEquals(l_field, l_converter.fromSql(l_parent, "5"));
			l_converter.fromSql(l_parent, "NotANumber");
			assertTrue(false);
		} catch (ParseException l_pe) {
		}
	}
	



}
