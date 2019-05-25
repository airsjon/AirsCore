package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;

public class SegmentedListModelExtTest extends MockSystemSetup {

	private SegmentedListModel<MockDataSeg, Object> f_byClass;
	private SegmentedListModel<MockDataSeg, Object> f_byProvider;
	private Set<MockDataSeg> f_dataList;
	private Set<MockDataSeg> f_dataList2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		f_byClass = new SegmentedListModel<MockDataSeg, Object>(MockDataSeg.class) {

			/* (non-Javadoc)
			 * @see com.airsltd.core.model.BlockModel#getSelectionQuery(java.lang.Object)
			 */
			@Override
			protected String getSelectionQuery(Object p_qualifier) {
				getData().put(p_qualifier, f_dataList);
				return "";
			}

		};
		f_byProvider = new SegmentedListModel<MockDataSeg, Object>(mock(BlockProvider.class));
		f_dataList = new HashSet<MockDataSeg>();
		f_dataList.add(new MockDataSeg(1,1));
		f_dataList.add(new MockDataSeg(2,4));
		f_dataList.add(new MockDataSeg(3,9));
		f_dataList2 = new HashSet<MockDataSeg>();
		f_dataList2.add(new MockDataSeg(4,16));
		f_dataList2.add(new MockDataSeg(5,25));
		f_dataList2.add(new MockDataSeg(6,36));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#addModelData(java.util.List)}.
	 */
	@Test
	public void testAddModelData() {
		// given
		// when
		f_byProvider.addModelData(f_dataList);
		f_byProvider.addModelData(f_dataList2);
		// then
		assertEquals(6, f_byProvider.getContentAsList(f_dataList.iterator().next().toSegment()).size());
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#remModelData(java.util.List)}.
	 */
	@Test
	public void testRemModelData() {
		// given
		// when
		f_byProvider.addModelData(f_dataList);
		f_byProvider.remModelData(Arrays.asList(new MockDataSeg[] {new MockDataSeg(2,4)}));
		// then
		assertEquals(2, f_byProvider.getContentAsList().size());
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#getContentAsList(java.lang.Object)}.
	 */
	@Test
	public void testGetContentAsListV() {
		// given
		f_byProvider.addModelData(f_dataList);
		f_byProvider.addModelData(f_dataList2);
		// when
		// then
		assertEquals(6, f_byProvider.getContentAsList(f_dataList.iterator().next().toSegment()).size());
		assertTrue(f_byProvider.getContentAsList(new Object()).isEmpty());
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#getContentAsList()}.
	 */
	@Test
	public void testGetContentAsList() {
		// given
		f_byProvider.addModelData(f_dataList);
		f_byProvider.addModelData(f_dataList2);
		// when
		// then
		assertEquals(6, f_byProvider.getContentAsList().size());
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#loadSegment(java.lang.Object, java.util.List)}.
	 */
	@Test
	public void testLoadSegment() {
		// given
		Object l_object1 = new Object();
		Object l_object2 = new Object();
		Object l_object3 = new Object();
		f_byProvider.loadSegment(l_object1, f_dataList);
		f_byProvider.loadSegment(l_object2, f_dataList2);
		f_byProvider.loadSegment(l_object3, null);
		// when
		// then
		assertEquals(3, f_byProvider.getContentAsList(l_object1).size());
		assertEquals(3, f_byProvider.getContentAsList(l_object2).size());
		assertTrue(f_byProvider.getContentAsList(l_object3).isEmpty());
		assertTrue(f_byProvider.getContentAsList(new Object()).isEmpty());
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#checkSegment(java.lang.Object)}.
	 */
	@Test
	public void testCheckSegment() {
		// given
		Object l_object = new Object();
		// when
		f_byClass.checkSegment(l_object);
		// then
		assertEquals ( f_dataList, f_byClass.getContentAsSet(l_object));
	}

	/**
	 * Test method for {@link com.airsltd.core.model.SegmentedListModel#getData()}.
	 */
	@Test
	public void testGetData() {
		// given
		Object l_object1 = new Object();
		Object l_object2 = new Object();
		Object l_object3 = new Object();
		f_byProvider.loadSegment(l_object1, f_dataList);
		f_byProvider.loadSegment(l_object2, f_dataList2);
		f_byProvider.loadSegment(l_object3, null);
		// when
		Map<Object, Set<MockDataSeg>> l_data = f_byProvider.getData();
		// then
		assertEquals(3, l_data.size());
		assertEquals(f_dataList, l_data.get(l_object1));
		assertEquals(f_dataList2, l_data.get(l_object2));
		assertEquals(null, l_data.get(l_object3));
	}

	@Test
	public void testLoadContent() {
		// given
		MockDataSeg l_data = new MockDataSeg(4,5);
		MockDataSeg l_data2 = new MockDataSeg(2,4);
		f_dataList.iterator().next().toSegment();
		// when
		// then
		assertEquals(l_data, f_byProvider.loadContent(l_data, true));
		// when
		f_byProvider.addData(f_dataList);
		// then
		MockDataSeg l_retData = f_byProvider.loadContent(l_data2, false);
		assertTrue(l_retData != l_data2);
		assertEquals(l_data2, l_retData);
	}

	@Test
	public void testClearData() {
		// given
		Object l_object1 = new Object();
		Object l_object2 = new Object();
		Object l_object3 = new Object();
		f_byProvider.loadSegment(l_object1, f_dataList);
		f_byProvider.loadSegment(l_object2, f_dataList2);
		f_byProvider.loadSegment(l_object3, null);
		// when
		f_byProvider.clearData(l_object2);
		// then
		assertEquals(0, f_byProvider.getData().get(l_object2).size());
	}

	@Test
	public void testAllSegments() {
		// given
		Object l_object1 = new Object();
		Object l_object2 = new Object();
		Object l_object3 = new Object();
		f_byProvider.loadSegment(l_object1, f_dataList);
		f_byProvider.loadSegment(l_object2, f_dataList2);
		f_byProvider.loadSegment(l_object3, null);
		Set<MockDataSeg> l_list = new HashSet<MockDataSeg>();
		for (MockDataSeg l_data : f_dataList) {
			l_data.f_object = l_object1;
			l_list.add(l_data);
		}
		for (MockDataSeg l_data : f_dataList2) {
			l_data.f_object = l_object2;
			l_list.add(l_data);
		}
		// when
		Set<Object> l_segments = f_byProvider.allSegments(l_list);
		// then
		assertEquals(2, l_segments.size());
		assertTrue(l_segments.contains(l_object1));
		assertTrue(l_segments.contains(l_object2));
	}

}
