package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.ComplexDataHooks;
import com.airsltd.core.data.IComplexDataHooks;
import com.airsltd.core.data.ISqlSelectorCallBack;

public class PersistentIdListModelTest extends MockSystemSetup {

	private PersistentIdListModel<MockDataSeg> f_model;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		ComplexDataHooks.setInstance(MockExtData.class, new IComplexDataHooks() {
			
			@Override
			public void startEndBlock(Connection p_connection) throws SQLException {
			}
			
			@Override
			public void startBlock() {
			}
			
			@Override
			public void finishEndBlock(Connection p_connection) throws SQLException {
			}

			@Override
			public void cancelBlock() {
			}
		});
		f_model = new PersistentIdListModel<MockDataSeg>(MockDataSeg.class, mock(BlockProvider.class)) {

		};
		new PersistentIdListModel<MockExtData>(MockExtData.class) {

		};
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetElement() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) }));
		// when
		// then
		assertEquals(new MockDataSeg(3,9), f_model.getElement(3));
		assertNull(f_model.getElement(5));
	}

	@Test
	public void testLoadContent() {
		// given
		MockDataSeg[] l_initData = new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) };
		f_model.addModelData(Arrays.asList(l_initData));
		MockDataSeg l_mockData = new MockDataSeg(5,10);
		MockDataSeg l_mockData2 = new MockDataSeg(3, 9);
		MockDataSeg l_mockData3 = new MockDataSeg(3, 9);
		// when
		// then
		assertEquals(l_mockData, f_model.loadContent(l_mockData, true));
		assertNull(f_model.loadContent(l_mockData2, true));
		assertEquals(l_initData[2], f_model.loadContent(l_mockData3, false));
	}
	
	@Test
	public void testGetElementData() {
		// given
		// when
		Map<Long, MockDataSeg> l_data = f_model.getElementData();
		// then
		assertTrue(l_data.isEmpty());
	}
	
	@Test
	public void testGetContentAsList() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) }));
		// when
		// then
		List<MockDataSeg> l_data = f_model.getContentAsList("");
		assertEquals(4, l_data.size());
	}
	
	@Test
	public void testLoadModelSystem() {
		// given
		@SuppressWarnings({ "unchecked" })
		PersistentIdListModel<MockDataSeg> l_model2 = new PersistentIdListModel<MockDataSeg>(MockDataSeg.class, mock(BlockProvider.class)) {
			@Override
			protected String getSelectionQuery(Object p_qualifier) {
				return "blarg";
			}
		};
		MockDataSeg[] l_initData = new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) };
		given(l_model2.getBlockProvider().loadDataBaseListExt(anyString(), isNull(ISqlSelectorCallBack.class), eq(-1l))).willReturn(new HashSet<MockDataSeg>(Arrays.asList(l_initData)));
		// when
		f_model.loadModel("A test");
		l_model2.loadModel("all in");
		// then
		assertTrue(f_model.getContentAsList("A test").isEmpty());
		assertEquals(4, l_model2.getContentAsList("A test").size());
	}
	
	@Test
	public final void testMissing() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) }));
		// when
		List<Long> l_retVal = f_model.missing(Arrays.asList(3l, 5l, 7l, 1l, 2l, 4l, 8l, 5l));
		// then
		assertEquals(3, l_retVal.size());
		assertTrue(5L==l_retVal.get(0));
		assertTrue(7==l_retVal.get(1));
		assertTrue(8==l_retVal.get(2));
		
	}

	@Test
	public final void testRemModelData() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) }));
		// when
		f_model.remModelData(Arrays.asList(new MockDataSeg(3,10), new MockDataSeg(2,4)));
		// then
		assertEquals(2, f_model.getContentAsList("").size());
		assertNotNull(f_model.getElement(4));
		assertNotNull(f_model.getElement(1));
	}
	
	@Test
	public final void testCopyData() {
		// given
		f_model.addModelData(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9), new MockDataSeg(4, 16) }));
		// when
		f_model.copyData(f_model.getElement(2), new MockDataSeg(5,3));
		f_model.copyData(f_model.getElement(3), new MockDataSeg(3, 10));
		// then
		assertTrue(f_model.getElement(2) == null);
		assertEquals(3, f_model.getElement(5).someData);
		assertEquals(10, f_model.getElement(3).someData);
	}

}
