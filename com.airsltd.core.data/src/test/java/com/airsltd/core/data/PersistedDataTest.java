package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.annotations.AirsPersistentField;
import com.airsltd.core.data.annotations.FieldStyle;

public class PersistedDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private MockPersistedData f_data;
	private MockPersistedData f_data1;
	private MockPersistedData f_data2;
	private MockPersistedData f_data3;

	@Before
	public void setUp() throws Exception {
		f_data = new MockPersistedData(); 
		f_data1 = new MockPersistedData(); 
		f_data2 = new MockPersistedData(); 
		f_data3 = new MockPersistedData(); 
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testToField() {
		// given
		// when
		IBlockField<AutoIncrementField> l_field = (IBlockField<AutoIncrementField>) f_data.toField(0);
		l_field.setValue(new AutoIncrementField(MockPersistedData.class, 5));
		IBlockField<Integer> l_field2 = (IBlockField<Integer>) f_data.toField(1);
		l_field2.setValue(52);
		IBlockField<String> l_field3 = (IBlockField<String>) f_data.toField(2);
		l_field3.setValue("A String of a bit to much data so it is truncated");
		// then
		assertEquals(5l, f_data.getId().getId());
		assertEquals(5l, l_field.getValue().getId());
		assertEquals(52, f_data.getGroup());
		assertEquals((Integer)52, l_field2.getValue());
		assertEquals("A String of a bit to much data so it is truncated", f_data.getName());
		assertEquals("A String of a bit to much data so it is truncated", l_field3.getValue());
	}

	@Test
	public void testTableName() {
		// given
		// when
		// then
		assertEquals("mockTable", f_data.tableName());
	}

	@Test
	public void testGetFieldNames() {
		// given
		String[] l_expected = new String[] {
				"Id", "GroupedId", "Name", "Enum"
		};
		// when
		// then
		assertArrayEquals(l_expected, f_data.getFieldNames());
	}

	@Test
	public void testOverrideFactory() {
		// given
		// when
		// then
		assertNull(f_data.overrideFactory(5));
	}

	@Test
	public void testOverrideConverter() {
		// given
		// when
		// then
		assertNull(f_data.overrideConverter(5, MockPersistedData.class, int.class, mock(AirsPersistentField.class)));
	}

	@Test
	public void testKeyFields() {
		// given
		// when
		// then
		assertEquals(6l, f_data.keyFields());
	}

	@Test
	public void testAutoIncrementField() {
		// given
		// when
		f_data.autoIncrementField(0, 72);
		// then
		assertEquals(72, f_data.getId().getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testToSql() {
		// given
		// when
		IBlockField<AutoIncrementField> l_field = (IBlockField<AutoIncrementField>) f_data.toField(0);
		l_field.setValue(new AutoIncrementField(MockPersistedData.class, 5));
		IBlockField<Integer> l_field2 = (IBlockField<Integer>) f_data.toField(1);
		l_field2.setValue(52);
		IBlockField<String> l_field3 = (IBlockField<String>) f_data.toField(2);
		l_field3.setValue("A String of a bit to much data so it is truncated");
		IBlockField<FieldStyle> l_field4 = (IBlockField<FieldStyle>) f_data.toField(3);
		l_field4.setValue(FieldStyle.CAPITALIZED);
		// then
		assertEquals("5", l_field.toSqlValue());
		assertEquals("52", l_field2.toSqlValue());
		assertEquals("'A String of a b'", l_field3.toSqlValue());
		assertEquals("2", l_field4.toSqlValue());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFromSql() throws ParseException {
		// given
		// when
		IBlockField<AutoIncrementField> l_field = (IBlockField<AutoIncrementField>) f_data.toField(0);
		l_field.setValue(new AutoIncrementField(MockPersistedData.class, 5));
		IBlockField<Integer> l_field2 = (IBlockField<Integer>) f_data.toField(1);
		l_field2.setValue(52);
		IBlockField<String> l_field3 = (IBlockField<String>) f_data.toField(2);
		l_field3.setValue("A String of a bit to much data so it is truncated");
		IBlockField<FieldStyle> l_field4 = (IBlockField<FieldStyle>) f_data.toField(3);
		l_field4.setValue(FieldStyle.CAPITALIZED);
		l_field.fromSqlValue("7");
		l_field2.fromSqlValue("54");
		l_field3.fromSqlValue("A simple string");
		l_field4.fromSqlValue("1");
		// then
		assertEquals(7l, f_data.getId().getId());
		assertEquals(54, f_data.getGroup());
		assertEquals("A simple string", f_data.getName());
		assertEquals(FieldStyle.UPPERCASE, f_data.getEnum());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCompareTo() throws ParseException {
		// given
		// when
		IBlockField<AutoIncrementField> l_field = (IBlockField<AutoIncrementField>) f_data.toField(0);
		l_field.setValue(new AutoIncrementField(MockPersistedData.class, 5));
		IBlockField<AutoIncrementField> l_field11 = (IBlockField<AutoIncrementField>) f_data1.toField(0);
		l_field11.setValue(new AutoIncrementField(MockPersistedData.class, 3));
		IBlockField<AutoIncrementField> l_field12 = (IBlockField<AutoIncrementField>) f_data2.toField(0);
		l_field12.setValue(new AutoIncrementField(MockPersistedData.class, 5));
		IBlockField<AutoIncrementField> l_field13 = (IBlockField<AutoIncrementField>) f_data3.toField(0);
		l_field13.setValue(new AutoIncrementField(MockPersistedData.class, 6));
		IBlockField<Integer> l_field2 = (IBlockField<Integer>) f_data.toField(1);
		l_field2.setValue(52);
		IBlockField<String> l_field3 = (IBlockField<String>) f_data.toField(2);
		l_field3.setValue("A String of a bit to much data so it is truncated");
		IBlockField<FieldStyle> l_field4 = (IBlockField<FieldStyle>) f_data.toField(3);
		l_field4.setValue(FieldStyle.CAPITALIZED);
		MockPersistedData l_mOne = new MockPersistedData(4, "A test");
		MockPersistedData l_mTwo = new MockPersistedData(5, "A test");
		MockPersistedData l_mThree = new MockPersistedData(3, "A test");
		MockPersistedData l_mFour = new MockPersistedData(4, "A test 2");
		MockPersistedData l_mFive = new MockPersistedData(4, "A tesm");
		MockPersistedData l_mSix = new MockPersistedData(4, "A test");
		// then
		assertEquals(-1,l_field.compareTo(l_field13));
		assertEquals(0,l_field.compareTo(l_field12));
		assertEquals(1,l_field.compareTo(l_field11));
		assertEquals(-1, l_mOne.compareTo(l_mTwo));
		assertEquals(1, l_mOne.compareTo(l_mThree));
		assertEquals(-2, l_mOne.compareTo(l_mFour));
		assertEquals(7, l_mOne.compareTo(l_mFive));
		assertEquals(0, l_mOne.compareTo(l_mSix));
	}

}
