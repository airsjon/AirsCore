/**
 * 
 */
package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;

/**
 * @author jonb
 *
 */
public class SegmentedListModelTest extends MockSystemSetup {

	private SegmentedListModel<MockDataSeg, Object> f_byProvider;
	private ArrayList<MockDataSeg> f_dataList2;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		f_byProvider = new SegmentedListModel<MockDataSeg, Object>(mock(BlockProvider.class));
		f_dataList2 = new ArrayList<MockDataSeg>();
		f_dataList2.add(new MockDataSeg(4,16));
		f_dataList2.add(new MockDataSeg(5,25));
		f_dataList2.add(new MockDataSeg(6,36));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetSegment() throws Exception {
		//given
		// when
		// then
		assertEquals(f_dataList2.get(0).toSegment(), f_byProvider.getSegment(f_dataList2.get(0)));
	}
}
