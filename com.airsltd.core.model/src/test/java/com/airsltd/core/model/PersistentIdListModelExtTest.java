package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.CoreInterface;

public class PersistentIdListModelExtTest {

	public class MockData2 extends MockDataSeg {

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		new CoreInterface();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private PersistentIdListModelExt<MockDataSeg, Object> f_model;

	@Before
	public void setUp() throws Exception {

		@SuppressWarnings("unchecked")
		BlockProvider<MockDataSeg> m_provider = mock(BlockProvider.class);
		f_model = new PersistentIdListModelExt<MockDataSeg, Object>(MockDataSeg.class, m_provider);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testLoadContent() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg(34, 56), new MockDataSeg(3, 4), new MockDataSeg(5, 6)));
		MockDataSeg l_new1 = new MockDataSeg(2,3);
		MockDataSeg l_new2 = new MockDataSeg(5,7);
		MockDataSeg l_new3 = new MockDataSeg(5,8);
		// when
		assertEquals(l_new1, f_model.loadContent(l_new1, false));
		assertEquals(l_new2, f_model.loadContent(l_new2, false));
		assertEquals(null, f_model.loadContent(l_new3, true));
		// then
		assertEquals(7, l_new2.someData);
		assertEquals(6, l_new3.someData);
	}

	@Test
	public void testGetElement() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg(34, 56)));
		// when
		assertTrue(f_model.getElement(null, 32)==null);
		assertFalse(f_model.getElement(null, 34)==null);
		// then
	}

	@Test
	public void testGetSelector() {
		// given
		// when
		assertEquals(null, f_model.getSelector(new MockDataSeg(3, 4)));
		// then
	}
	

}
