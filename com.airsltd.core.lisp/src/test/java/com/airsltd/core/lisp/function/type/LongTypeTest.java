package com.airsltd.core.lisp.function.type;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;

public class LongTypeTest {

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
	public void testConvert() {
		// given
		ISimpleValue l_pojo = Type.functionPojo(new Object());
		ISimpleValue[] l_toConvert = new ISimpleValue[] {
			null,
			Type.FALSE,
			Type.TRUE,
			Type.functionInteger(52),
			Type.functionFloat(3f),
			Type.functionFloat(3.2f),
			Type.functionDouble(3d),
			Type.functionDouble(3.2d),
			Type.functionString("523"),
			Type.functionString("nothing"),
			Type.functionString("1963-09-20"),
			Type.functionDate(new Date(new GregorianCalendar(1963, 8, 20).getTimeInMillis())),
			l_pojo
		};
		ISimpleValue[] l_expected = new ISimpleValue[] {
			Type.functionLong(0),
			Type.functionLong(0),
			Type.functionLong(1),
			Type.functionLong(52),
			Type.functionLong(3),
			Type.functionLong(3),
			Type.functionLong(3),
			Type.functionLong(3),
			Type.functionLong(523),
			Type.functionString("nothing"),
			Type.functionLong(-198262800000l),
			Type.functionLong(-198262800000l),
			l_pojo
		};
		// when
		LongType l_longType = new LongType();
		// then
		int index = 0;
		for (ISimpleValue l_value : l_toConvert) {
			assertEquals("@"+index, l_expected[index++], l_longType.convert(l_value, null));
		}
	}

	@Test
	public void testIsLong() {
		// given
		// when
		assertFalse(LongType.isLong(mock(ISimpleValue.class)));
		assertTrue(LongType.isLong(Type.functionLong(5)));
		assertFalse(LongType.isLong(Type.functionInteger(5)));
		// then
	}

	@Test
	public void testToLong() {
		// given
		// when
		// then
		assertEquals(52l, (long)LongType.toLong(Type.functionLong(52)));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testToLongUnsupported() throws Exception {
		//given
		// when
		// then
		LongType.toLong(Type.functionPojo(new Object()));
	}

}
