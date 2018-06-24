package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersistentIdConverterTest  {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private PersistentIdConverter<MockDataSeg> f_converter;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		PersistentIdListModel<MockDataSeg> l_model = mock(PersistentIdListModel.class);
		BlockModel.setFromIdModel(MockDataSeg.class, l_model);
		f_converter = new PersistentIdConverter<MockDataSeg>(MockDataSeg.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSql() {
		// given
		// when
		String l_output = f_converter.toSql(null, new MockDataSeg(5,4));
		String l_output2 = f_converter.toSql(null, null);
		// then
		assertEquals("5", l_output);
		assertEquals("0", l_output2);
	}

	@Test
	public void testFromSql() {
		try {
			// given
			MockDataSeg l_mockData = new MockDataSeg(5, 4);
			given(((PersistentIdListModel<MockDataSeg>)BlockModel.getFromIdModel(MockDataSeg.class)).getElement(5)).willReturn(l_mockData);
			// when
			MockDataSeg l_data = f_converter.fromSql(null, "5");
			MockDataSeg l_data2 = f_converter.fromSql(null, "0");
			// then
			assertEquals(l_mockData, l_data);
			assertEquals(null, l_data2);
		} catch (ParseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
