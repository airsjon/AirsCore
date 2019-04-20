package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.data.BlockMod;
import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.IUserInterfaceListener;
import com.airsltd.core.model.PersistentIdListModelExtTest.MockData2;

public class BlockModelTest extends MockSystemSetup {
	
	static class MockBlockModel extends BlockModel<MockDataSeg, Object> {

		public MockBlockModel(BlockProvider<MockDataSeg> p_provider) {
			super(p_provider);
		}

		@Override
		public Set<MockDataSeg> addModelData(Set<MockDataSeg> p_addData) {
			return p_addData;
		}

		@Override
		public void remModelData(Set<MockDataSeg> p_remData) {
		}

	}

	private BlockProvider<MockDataSeg> f_provider;
	private MockBlockModel f_blockModel;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		// given
		BlockModel.setFromIdModel(MockData2.class, null);
		f_provider = mock(BlockProvider.class);
		f_blockModel = new MockBlockModel(f_provider);
	}

	@After
	public void tearDown() throws Exception {
		super.setUp();
	}

	@Test
	public void testGetBlockProvider() {
		// given
		// when
		// then
		assertEquals(f_provider, f_blockModel.getBlockProvider());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSetBlockProvider() {
		// given
		// when
		f_blockModel.setBlockProvider(mock(BlockProvider.class));
		// then
		assertTrue(f_provider != f_blockModel.getBlockProvider());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddUserInterface() {
		// given
		@SuppressWarnings("rawtypes")
		IUserInterfaceListener l_listener = mock(IUserInterfaceListener.class);
		// when
		f_blockModel.addUserInterface(l_listener);
		f_blockModel.addUserInterface(l_listener);
		// then
		assertEquals(1,f_blockModel.f_uiListeners.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveUserInterface() {
		// given
		@SuppressWarnings("rawtypes")
		IUserInterfaceListener l_listener = mock(IUserInterfaceListener.class);
		// when
		f_blockModel.addUserInterface(l_listener);
		assertEquals(1,f_blockModel.f_uiListeners.size());
		f_blockModel.removeUserInterface(l_listener);
		// then
		assertEquals(0,f_blockModel.f_uiListeners.size());
		
	}

	@Test
	public void testBlockStart() {
		// for coverage
		// given
		// when
		f_blockModel.blockUIStart();
		// then
	}

	@Test
	public void testBlockStop() {
		// for coverage
		// given
		// when
		f_blockModel.blockUIStop();
		// then
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddDataListOfT() {
		// given
		@SuppressWarnings("rawtypes")
		IUserInterfaceListener l_listener = mock(IUserInterfaceListener.class);
		f_blockModel.addUserInterface(l_listener);
		// when
		f_blockModel.addData(new MockDataSeg(3,4));
		// then
		verify(l_listener).addData(anyListOf(MockDataSeg.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateDataListOfBlockModOfT() {
		// given
		IUserInterfaceListener<MockDataSeg> l_listener = mock(IUserInterfaceListener.class);
		f_blockModel.addUserInterface(l_listener);
		List<IUserInterfaceListener<MockDataSeg>> l_listenerList = new ArrayList<>();
		MockDataSeg l_object = new MockDataSeg(5,4);
		// when
		Set<String> l_properties = f_blockModel.prepareUpdateData(
				new HashSet<BlockMod<MockDataSeg>>(Arrays.asList(new BlockMod<MockDataSeg>(l_object, new MockDataSeg(3, 2)))), l_listenerList);
		Set<MockDataSeg> l_set = new HashSet<MockDataSeg>(Arrays.asList(l_object));
		f_blockModel.notifyListeners(l_listenerList, l_properties, l_set);
		// then
		assertEquals(1, l_listenerList.size());
		assertEquals(l_listener, l_listenerList.get(0));
		verify(l_listener).modifyData(anyListOf(MockDataSeg.class),eq(l_properties));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveDataListOfT() {
		// given
		@SuppressWarnings("rawtypes")
		IUserInterfaceListener l_listener = mock(IUserInterfaceListener.class);
		f_blockModel.addUserInterface(l_listener);
		// when
		f_blockModel.removeData(new MockDataSeg(3,4));
		// then
		verify(l_listener).removeData(anyListOf(MockDataSeg.class));
	}

	@Test
	public void testCopyData() {
		// given
		MockDataSeg l_data1 = mock(MockDataSeg.class);
		MockDataSeg l_data2 = mock(MockDataSeg.class);
		// when
		f_blockModel.copyData(l_data1, l_data2);
		// then
		verify(l_data1).copy(l_data2);
	}

	@Test
	public void testLoadContent() {
		// given
		MockDataSeg l_data = new MockDataSeg(3,4);
		// when
		assertEquals(l_data, f_blockModel.loadContent(l_data, true));
		// then
	}

	@Test
	public void testGetContentAsList() {
		// given
		// when
		// then
		assertEquals(new ArrayList<MockDataSeg>(), f_blockModel.getContentAsList(new Object()));
	}

	@Test
	public void testGetContentAsSet() {
		// given
		// when
		// then
		assertEquals(new HashSet<MockDataSeg>(), f_blockModel.getContentAsSet(new Object()));
	}

	@Test
	public void testStartBlock() {
		// given
		// when
		f_blockModel.startBlock();
		// for code coverage
		// then
		verify(f_provider).startBlock();
	}

	@Test
	public void testEndBlock() {
		// given
		given(f_provider.endBlock()).willReturn(true);
		// when
		assertTrue(f_blockModel.endBlock());
		// then
		verify(f_provider).endBlock();
	}

	@Test
	public void testEndBlockBoolean() {
		// given
		given(f_provider.endBlock(true)).willReturn(true);
		// when
		assertTrue(f_blockModel.endBlock(true));
		// then
		verify(f_provider).endBlock(anyBoolean());
	}

	@Test
	public void testCancelBlock() {
		// given
		// when
		f_blockModel.cancelBlock();
		// then
		verify(f_provider).cancelBlock();
	}

	@Test
	public void testAddContent() {
		// given
		given(f_provider.addContent(any(MockDataSeg.class))).willReturn(true);
		// when
		assertTrue(f_blockModel.addContent(new MockDataSeg(1,1)));
		// then
		verify(f_provider).addContent(any(MockDataSeg.class));
	}

	@Test
	public void testRemoveContent() {
		// given
		given(f_provider.removeContent(any(MockDataSeg.class))).willReturn(true);
		// when
		assertTrue(f_blockModel.removeContent(new MockDataSeg(1,1)));
		// then
		verify(f_provider).removeContent(any(MockDataSeg.class));
	}

	@Test
	public void testUpdateContent() {
		// given
		given(f_provider.updateContent(any(MockDataSeg.class), any(MockDataSeg.class))).willReturn(true);
		// when
		assertTrue(f_blockModel.updateContent(new MockDataSeg(1,1), new MockDataSeg(1,2)));
		// then
		verify(f_provider).updateContent(any(MockDataSeg.class), any(MockDataSeg.class));
	}

	@Test
	public void testIsBlockOn() {
		// given
		given(f_provider.isBlockOn()).willReturn(false, true);
		// when
		assertFalse(f_blockModel.isBlockOn());
		assertTrue(f_blockModel.isBlockOn());
		// then
	}

	@Test
	public void testTraceOff() {
		try {
			// given
			given(CoreInterface.getSystem().traceActive(any(Class.class), anyString())).willReturn(false);
			// when
			BlockModel.trace("a trace");
			// then
			verify(CoreInterface.getSystem()).traceActive(eq(BlockModel.class), 
					eq("/data"));
		} finally {
			BlockModel.clearTracing();
		}
	}
	
	@Test
	public void testTraceOn() {
		try {
			// given
			given(CoreInterface.getSystem().traceActive(any(Class.class), anyString())).willReturn(true);
			// when
			BlockModel.trace("a trace");
			// then
			verify(CoreInterface.getSystem()).traceActive(eq(BlockModel.class), 
					eq("/data"));
			verify(CoreInterface.getSystem()).trace(eq(BlockModel.class), 
					eq("/data"), eq("a trace"));
		} finally {
			BlockModel.clearTracing();
		}
	}
	
	@Test
	public void testSetInitialLoad() {
		// given
		// when
		f_blockModel.setInitialLoad(true);
		// then
		assertTrue(f_blockModel.isInitialLoad());
	}
	
	@Test
	public final void testInvalidModel() {
		// given
		new CoreInterface();
		CoreInterface.getSystem().setLog(mock(Log.class));
		// when
		BlockModel.getFromIdModel(MockData2.class);
		// then
		verify(CoreInterface.getSystem().getLog()).error(eq("Unable to load model for com.airsltd.core.model.PersistentIdListModelExtTest$MockData2"),
				(Throwable) eq(null));
	}

	@Test
	public void testSimpleClassConstructor() {
		// given
		// when
		new PersistentIdListModelExt<MockData2, Object>(MockData2.class) {

		};
		// then
		assertTrue(BlockModel.getFromIdModel(MockData2.class)!=null);
	}

}
