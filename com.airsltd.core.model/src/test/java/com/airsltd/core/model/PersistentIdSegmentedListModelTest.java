package com.airsltd.core.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;

public class PersistentIdSegmentedListModelTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private PersistentIdSegmentedListModel<MockDataSeg, Object> f_listModel;

	@Before
	public void setUp() throws Exception {
		f_listModel = new PersistentIdSegmentedListModel<MockDataSeg, Object>(MockDataSeg.class);
		// cc
		new PersistentIdSegmentedListModel<MockDataSeg, Object>(MockDataSeg.class, new BlockProvider<>(MockDataSeg.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadContent() {
		// given
		MockDataSeg l_data = new MockDataSeg(2, 3);
		MockDataSeg l_data2 = new MockDataSeg(4, 5);
		MockDataSeg l_toLookUp = new MockDataSeg(4, 7);
		MockDataSeg l_toLookUp2 = new MockDataSeg(5, 7);
		MockDataSeg l_toLookUp3 = new MockDataSeg(2, 4);
		f_listModel.addModelData(Arrays.asList(l_data, l_data2));
		// when
		MockDataSeg l_retObject = f_listModel.loadContent(l_toLookUp3, false);
		MockDataSeg l_retObject2 = f_listModel.loadContent(l_toLookUp, true);
		MockDataSeg l_retObject3 = f_listModel.loadContent(l_toLookUp2, true);
		// then
		assertEquals(l_data, l_retObject);
		assertEquals(l_data2, l_retObject2);
		assertEquals(5, l_toLookUp.someData);
		assertEquals(l_toLookUp2, l_retObject3);
	}

	@Test
	public void testGetElement() {
		// given
		MockDataSeg l_data = new MockDataSeg(2, 3);
		MockDataSeg l_data2 = new MockDataSeg(4, 5);
		f_listModel.addModelData(Arrays.asList(l_data));
		// when
		MockDataSeg l_retObject = f_listModel.getElement(l_data.toSegment(), 2);
		MockDataSeg l_retObject2 = f_listModel.getElement(l_data2.toSegment(), 5);
		// then
		assertEquals(l_data, l_retObject);
		assertNull(l_retObject2);
	}

	@Test
	public void testRemModelData() throws Exception {
		//given
		MockDataSeg l_mockData3 = new MockDataSeg(3, 5);
		MockDataSeg l_mockData4 = new MockDataSeg(4, 6);
		// when
		f_listModel.addModelData(Arrays.asList(l_mockData3, l_mockData4));
		f_listModel.remModelData(Arrays.asList(l_mockData4));
		// then
		assertNull(f_listModel.getElement(4));
		assertEquals(l_mockData3, f_listModel.getElement(3));
	}
	
	@Test
	public void testCopyData() throws Exception {
		//given
		MockDataSeg l_mockDataOld = new MockDataSeg(2, 5);
		MockDataSeg l_mockDataNew = new MockDataSeg(2, 6);
		MockDataSeg l_mockDataOld3 = new MockDataSeg(3, 5);
		MockDataSeg l_mockDataNew4 = new MockDataSeg(4, 6);
		// when
		f_listModel.addModelData(Arrays.asList(l_mockDataOld3));
		f_listModel.copyData(l_mockDataOld, l_mockDataNew);
		f_listModel.copyData(l_mockDataOld3, l_mockDataNew4);
		// then
		assertEquals(6, l_mockDataOld.someData);
		assertEquals(6, f_listModel.getElement(4).someData);
	}
	
	@Test
	public void testAddContent() throws Exception {
		//given
		MockDataSeg l_mockData1 = new MockDataSeg(1, 5);
		MockDataSeg l_mockData2 = new MockDataSeg(2, 5);
		MockDataSeg l_mockData3 = new MockDataSeg(3, 5);
		MockDataSeg l_mockData4 = new MockDataSeg(4, 5);
		MockDataSeg l_mockData5 = new MockDataSeg(5, 5);
		// when
		try {
			f_listModel.startBlock();
			f_listModel.addContent(l_mockData1);
			f_listModel.addContent(l_mockData2);
			f_listModel.addContent(l_mockData3);
			f_listModel.addContent(l_mockData4);
			f_listModel.addContent(l_mockData5);
			// then
			assertEquals(5, f_listModel.getBlockProvider().getBlockData().size());
		} finally {
			f_listModel.cancelBlock();
		}
	}
	
}
