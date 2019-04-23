/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.collections.AirsCollections;
import com.airsltd.core.data.BlockHistory;
import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;

/**
 * @author Jon
 *
 */
@SuppressWarnings("unchecked")
public class BlockHistoryTest {

	private BlockProvider<IBlockData> f_blockProvider = mock(BlockProvider.class);
	private List<IBlockData> f_mockAddData = new ArrayList<IBlockData>(3);
	private List<IBlockData> f_mockRemoveData = new ArrayList<IBlockData>(2);
	private List<BlockMod<IBlockData>> f_mockUpdateData = new ArrayList<BlockMod<IBlockData>>(1);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// given
		f_mockAddData.add(new MockData(10, 100));
		f_mockAddData.add(new MockData(20, 200));
		f_mockAddData.add(new MockData(30, 300));
		f_mockRemoveData.add(new MockData(15, 150));
		f_mockRemoveData.add(new MockData(25, 250));
		f_mockUpdateData.add(new BlockMod<IBlockData>(new MockData(12, 120), new MockData(13,130)));
		given(f_blockProvider.getBlockData()).willReturn(new HashSet<IBlockData>(f_mockAddData));
		given(f_blockProvider.getBlockRemoveData()).willReturn(new HashSet<IBlockData>(f_mockRemoveData));
		given(f_blockProvider.getBlockUpdateData()).willReturn(new HashSet<BlockMod<IBlockData>>(f_mockUpdateData));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockHistory#BlockHistory(com.airsltd.core.data.BlockProvider)}.
	 */
	@Test
	public final void testBlockHistory() {
		// given - see setup()
		// when
		BlockHistory<IBlockData> l_history = new BlockHistory<IBlockData>(f_blockProvider);
		f_mockAddData.add(new MockData(40, 400));
		// then
		assertEquals(3,l_history.getAdds().size());
		assertEquals(2,l_history.getRemoves().size());
		assertEquals(1,l_history.getUpdates().size());
		containsData(f_mockAddData.subList(0, 3), l_history.getAdds());
		containsData(f_mockRemoveData, l_history.getRemoves());
		containsData(f_mockUpdateData, l_history.getUpdates());
	}

	private void containsData(List<?> p_array, List<?> p_array2) {
		for (Object l_current : p_array) {
			assertNotNull(AirsCollections.lookFor(p -> l_current.equals(p), p_array2));
		}
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockHistory#getUpdatesOriginals()}.
	 */
	@Test
	public final void testGetUpdatesOriginals() {
		// given - see setup()
		// when
		BlockHistory<IBlockData> l_history = new BlockHistory<IBlockData>(f_blockProvider);
		// then
		assertEquals(1,l_history.getUpdatesOriginals().size());
		assertEquals(new MockData(12, 120), l_history.getUpdatesOriginals().get(0));
	}

}
