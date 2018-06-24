package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockFieldTest {

	private BlockField<Object, IBlockData> f_testField;
	private BlockField<Object, IBlockData> f_testNullField;
	private IBlockData f_data;
	private BlockFieldFactory<IBlockData, Object> f_fieldFactory;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		// given
		f_data = mock(IBlockData.class);
		f_fieldFactory = mock(BlockFieldFactory.class);
		given(f_fieldFactory.getConverter()).willReturn(mock(IDatabaseConverter.class));
		given(f_fieldFactory.getField()).willReturn(0);
		f_testField = new BlockField<Object, IBlockData>(f_data, f_fieldFactory);
		f_testNullField = new BlockField<Object, IBlockData>(null, f_fieldFactory);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testHashCode() {
		// given
		given(f_data.tableName()).willReturn("aTable");
		given(f_fieldFactory.value(any(IBlockData.class))).willReturn(null,"aValue");
		
		// when
		// then
		// when
		assertEquals(29791, f_testNullField.hashCode());
		assertEquals(225133212, f_testField.hashCode());
	}

	@Test
	public final void testGetValue() {
		// given
		Object l_rVal = new Object();
		given(f_fieldFactory.value(any(IBlockData.class))).willReturn(l_rVal);
		// when
		// then
		assertEquals(l_rVal, f_testField.getValue());
	}

	@Test
	public final void testSetValue() {
		// given
		Object f_setData = new Object();
		// when
		f_testField.setValue(f_setData);
		// then
		verify(f_fieldFactory).modifyValue(f_data, f_setData);
	}

	@Test
	public final void testToSqlValue() {
		// given
		Object l_rVal = new Object();
		given(f_fieldFactory.value(any(IBlockData.class))).willReturn(l_rVal);
		given(f_fieldFactory.getConverter().toSql(any(IBlockData.class), any())).willReturn("passed");
		// when
		assertEquals("passed", f_testField.toSqlValue());
		// then
		verify(f_fieldFactory.getConverter()).toSql(any(IBlockData.class), eq(l_rVal));
	}
	
	@Test
	public final void testFromSqlValue() {
		// given
		Object l_rVal = new Object();
		try {
			given(f_fieldFactory.getConverter().fromSql(any(IBlockData.class),anyString())).willReturn(l_rVal);
		} catch (ParseException e) {
			assertTrue(false);
		}
		// when
		try {
			f_testField.fromSqlValue("aMockString");
		} catch (ParseException e) {
			assertTrue(false);
		}
		// then
		try {
			verify(f_fieldFactory.getConverter()).fromSql(any(IBlockData.class), eq("aMockString"));
		} catch (ParseException e) {
			assertTrue(false);
		}
		verify(f_fieldFactory).modifyValue(any(IBlockData.class), eq(l_rVal));
	}

	/**
	 * Paths
	 * <ul><li>eq obj</li>
	 * <li>!eq obj, obj == null</li>
	 * <li>!eq obj, class !=</li>
	 * <li>!eq obj, class ==, f_data == null, other.f_data != null
	 * <li>!eq obj, class ==, f_data == null, other.f_data == null
	 * <li>!eq obj, class ==, f_data != null, !samefield</li>
	 * <li>!eq obj, class ==, f_data != null, samefield, !sameData</li>
	 * <li>!eq obj, class ==, f_data != null, samefield, sameData</li>
	 * </ul>
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testEqualsObject() {
		// given
		BlockFieldFactory l_fieldFactory = mock(BlockFieldFactory.class);
		given(l_fieldFactory.getConverter()).willReturn(mock(IDatabaseConverter.class));
		given(l_fieldFactory.getField()).willReturn(1);
		BlockField<Object, IBlockData> l_testField2 = new BlockField<Object, IBlockData>(f_data, f_fieldFactory);
		BlockField<Object, IBlockData> l_testField3 = new BlockField<Object, IBlockData>(f_data, l_fieldFactory);
		BlockField<Object, IBlockData> l_testNullField1 = new BlockField<Object, IBlockData>(null, f_fieldFactory);
		given(f_fieldFactory.value(any(IBlockData.class))).willReturn("val2", "val1");
		given(l_fieldFactory.value(any(IBlockData.class))).willReturn("val1");
		given(f_data.tableName()).willReturn("testTable");
		
		// when
		// then
		assertTrue(f_testField.equals(f_testField));
		assertFalse(f_testField.equals(null));
		assertFalse(f_testField.equals("not a IBlockData"));
		assertFalse(f_testNullField.equals(f_testField));
		assertTrue(f_testNullField.equals(l_testNullField1));
		assertFalse(f_testField.equals(l_testField3));
		assertFalse(f_testField.equals(l_testField2));
		assertTrue(f_testField.equals(l_testField2));
	}
	
	/**
	 * Paths
	 * <ul><li>data==null, other.data==null</li>
	 * <li>data==null, other.data!=null</li>
	 * <li>data!=null, !data.equals(other.data)</li>
	 * <li>data!=null, data.equals(other.data)</li>
	 * </ul>
	 */
	@Test
	public final void testSameDataBlockField() {
		// given
		BlockField<Object, IBlockData> l_testField2 = new BlockField<Object, IBlockData>(f_data, f_fieldFactory);
		given(f_fieldFactory.value(any(IBlockData.class))).willReturn(null, null, null, new Object(), new Object(), new Object(), new Object());
		
		// when
		// then
		assertTrue(f_testField.sameData(l_testField2));
		assertFalse(f_testField.sameData(l_testField2));
		assertFalse(f_testField.sameData(l_testField2));
		assertTrue(f_testField.sameData(l_testField2));
		
	}

	/**
	 * Paths
	 * <ul><li>other.data == null</li>
	 * <li>other.data != null, tableName != other.data.tableName</li>
	 * <li>other.data != null, tableName == other.data.tableName, field <> other.f_field</li>
	 * <li>other.data != null, tableName == other.data.tableName, field == other.f_field</li>
	 * </ul>
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testSameFieldBlockField() {
		// given
		BlockFieldFactory l_fieldFactory = mock(BlockFieldFactory.class);
		given(l_fieldFactory.getConverter()).willReturn(mock(IDatabaseConverter.class));
		given(l_fieldFactory.getField()).willReturn(1);
		IBlockData l_data2 = mock(IBlockData.class);
		BlockField<Object, IBlockData> l_testField2 = new BlockField<Object, IBlockData>(l_data2, f_fieldFactory);
		@SuppressWarnings("unchecked")
		BlockField<Object, IBlockData> l_testField3 = new BlockField<Object, IBlockData>(l_data2, l_fieldFactory);
		given(f_data.tableName()).willReturn("testTable");
		given(l_data2.tableName()).willReturn("testTable2","testTable");
		
		// when
		// then
		assertFalse(f_testField.sameField(f_testNullField));
		assertFalse(f_testField.sameField(l_testField2));
		assertFalse(f_testField.sameField(l_testField3));
		assertTrue(f_testField.sameField(l_testField2));
	}
}
