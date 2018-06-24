package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;

public class NamedListModelTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private NamedListModel<MockDataSeg, Object> f_model;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		f_model = new NamedListModel<MockDataSeg, Object>(mock(BlockProvider.class)); 
		MockDataSeg l_data1 = new MockDataSeg(2,3);
		f_model.getData().add(l_data1);
		MockDataSeg l_data2 = new MockDataSeg(4, 5);
		f_model.getData().add(l_data2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetElement() {
		// given
		// when
		// then
		assertEquals(new MockDataSeg(4,5), f_model.getElement("4", null));
	}

}
