/**
 * 
 */
package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.NotificationStatus;

/**
 * @author Jon
 *
 */
public class BlockProviderTest extends ConnectionSetup {

	private BlockProvider<MockData> bp;
	private BlockProvider<MockExtData> bpExt;
	private MockData mockData1;
	private MockData mockData2;
	private MockData mockData3;
	private MockData mockData4;
	private MockData mockData5;
	private MockExtData mockExtData1;
	private MockExtData mockExtData2;
	private MockExtData mockExtData3;
	private MockExtData mockExtData4;
	private ResultSet f_generatedKeyRS;
	private ICoreInterface f_oldSystem;
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
		super.setUp();
		// given
		f_oldSystem = CoreInterface.getSystem();
		CoreInterface.setSystem(mock(ICoreInterface.class));
		given(CoreInterface.getSystem().handleException(anyString(),
				any(Throwable.class), any(NotificationStatus.class))).willReturn(true);
		given(CoreInterface.getSystem().getConnection()).willReturn(m_connection);
		bp = new BlockProvider<MockData>(MockData.class);
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
		bpExt = new BlockProvider<MockExtData>(MockExtData.class);
		mockData1 = new MockData(1,10);
		mockData2 = new MockData(2,20);
		mockData3 = new MockData(3,30);
		mockData4 = new MockData(4,40);
		mockData5 = new MockData(5,50);
		mockExtData1 = new MockExtData(12, 150);
		mockExtData2 = new MockExtData(13, 160);
		mockExtData3 = new MockExtData(14, 170);
		mockExtData4 = new MockExtData(15, 180);
		new MockExtData(16, 190);
		bp.getBlockData().add(mockData1);
		bp.getBlockRemoveData().add(mockData2);
		bp.getBlockUpdateData().add(new BlockMod<MockData>(mockData3, mockData4));
		bpExt.getBlockData().add(mockExtData1);
		bpExt.getBlockRemoveData().add(mockExtData2);
		bpExt.getBlockUpdateData().add(new BlockMod<MockExtData>(mockExtData3, mockExtData4));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		CoreInterface.setSystem(f_oldSystem);
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider.BlockMod#getOldValue()}.
	 */
	@Test
	public final void testBlockModGetOldValue() {
		// given
		Object l_newValue = new Object();
		Object l_oldValue = new Object();
		BlockMod<Object> l_tester = new BlockMod<Object>(l_oldValue, l_newValue);
		// when
		// then
		assertEquals(l_oldValue, l_tester.getOldValue());
	}
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider.BlockMod#getOldValue()}.
	 */
	@Test
	public final void testBlockModEquals() {
		// given
		Object l_newValue = new Object();
		Object l_oldValue = new Object();
		Object l_newValue2 = new Object();
		Object l_oldValue2 = new Object();
		BlockMod<Object> l_tester = new BlockMod<Object>(l_oldValue, l_newValue);
		BlockMod<Object> l_tester2 = new BlockMod<Object>(l_oldValue, l_newValue);
		BlockMod<Object> l_tester3 = new BlockMod<Object>(l_oldValue2, l_newValue);
		BlockMod<Object> l_tester4 = new BlockMod<Object>(l_oldValue, l_newValue2);
		BlockMod<Object> l_tester5 = new BlockMod<Object>(null, l_newValue);
		BlockMod<Object> l_tester6 = new BlockMod<Object>(l_oldValue, null);
		
		BlockMod<Object> l_tester7 = new BlockMod<Object>(null, null);
		BlockMod<Object> l_tester8 = new BlockMod<Object>(null, null);
		
		// when
		// then
		assertTrue(l_tester.equals(l_tester2));
		assertFalse(l_tester.equals(l_tester3));
		assertFalse(l_tester.equals(l_tester4));
		assertFalse(l_tester.equals(l_tester5));
		assertFalse(l_tester.equals(l_tester6));

		assertFalse(l_tester5.equals(l_tester3));
		assertFalse(l_tester5.equals(l_tester4));
		assertFalse(l_tester5.equals(l_tester));
		assertFalse(l_tester5.equals(l_tester6));

		assertFalse(l_tester6.equals(l_tester3));
		assertFalse(l_tester6.equals(l_tester4));
		assertFalse(l_tester6.equals(l_tester5));
		assertFalse(l_tester6.equals(l_tester));
		
		assertFalse(l_tester.equals(3));
		assertTrue(l_tester7.equals(l_tester8));
	}
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider.BlockMod#getOldValue()}.
	 */
	@Test
	public final void testBlockModHashCode() {
		// given
		Object l_newValue = new Object();
		Object l_oldValue = new Object();
		Object l_newValue2 = new Object();
		Object l_oldValue2 = new Object();
		BlockMod<Object> l_tester = new BlockMod<Object>(l_oldValue, l_newValue);
		BlockMod<Object> l_tester2 = new BlockMod<Object>(l_oldValue, l_newValue);
		BlockMod<Object> l_tester3 = new BlockMod<Object>(l_oldValue2, l_newValue);
		BlockMod<Object> l_tester4 = new BlockMod<Object>(l_oldValue, l_newValue2);
		BlockMod<Object> l_tester5 = new BlockMod<Object>(null, l_newValue);
		BlockMod<Object> l_tester6 = new BlockMod<Object>(l_oldValue, null);
		
		// when
		// then
		assertEquals(l_oldValue.hashCode(),l_tester.hashCode());
		assertEquals(l_oldValue.hashCode(),l_tester2.hashCode());
		assertEquals(l_oldValue2.hashCode(),l_tester3.hashCode());
		assertEquals(l_oldValue.hashCode(),l_tester4.hashCode());
		assertEquals(0,l_tester5.hashCode());
		assertEquals(l_oldValue.hashCode(),l_tester6.hashCode());

	}
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#startBlock()}.
	 */
	@Test
	public final void testStartBlockLoggingOff() {
		testStartBlockBDD(false);
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#startBlock()}.
	 */
	@Test
	public final void testStartBlockLoggingOn() {
		testStartBlockBDD(true);
	}
	
	private final void testStartBlockBDD(boolean p_loggingOn) {
		// given
		bp.setTracing(p_loggingOn);
		bp.setDebugging(p_loggingOn);
		
		// when
		bp.setBlockOn(true);
		bp.startBlock();
		// then
		assertEquals(1, bp.getBlockData().size());
		assertEquals(1, bp.getBlockRemoveData().size());
		assertEquals(1, bp.getBlockUpdateData().size());
		assertTrue(bp.isBlockOn());
		
		// given
		
		// when
		bp.setBlockOn(false);
		bp.startBlock();
		// then
		assertEquals(0, bp.getBlockData().size());
		assertEquals(0, bp.getBlockRemoveData().size());
		assertEquals(0, bp.getBlockUpdateData().size());
		assertTrue(bp.isBlockOn());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#addContent(java.sql.Connection, com.airsltd.core.data.BlockData)}.
	 */
	@Test
	public final void testAddContentLoggingOn() {
		testAddContentBdd(true);
		// given
		f_generatedKeyRS = mock(ResultSet.class);
		try {
			given(m_ps.getGeneratedKeys()).willThrow(mock (SQLException.class));
		} catch (SQLException e1) {
		}

		// when
		bp.setBlockOn(false);
		bp.setNoDuplicateCheck(false);
		// then
		assertFalse(bp.addContent(mockData5));
	}
	
	@Test
	public final void testAddContentLoggingOff() {
		testAddContentBdd(false);
	}
	
	private final void testAddContentBdd(boolean p_loggingOn) {
		// given
		bp.setTracing(p_loggingOn);
		bp.setDebugging(p_loggingOn);
		
		// when 
		bp.setBlockOn(true);
		bp.setNoDuplicateCheck(false);
		
		// then
		assertTrue(bp.addContent(mockData1));
		assertEquals(1, bp.getBlockData().size());
	
		// when
		bp.setBlockOn(true);
		bp.setNoDuplicateCheck(true);

		// then
		assertTrue(bp.addContent(mockData5));
		assertEquals(2, bp.getBlockData().size());
		assertTrue(bp.getBlockData().contains(mockData5));
		if (p_loggingOn) {
			verify(CoreInterface.getSystem()).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		} else {
			verify(CoreInterface.getSystem(),never()).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		}

		// given

		// when
		bp.setBlockOn(false);
		bp.setNoDuplicateCheck(false);
		// then
		assertTrue(bp.addContent(mockData5));
		if (p_loggingOn) {
			verify(CoreInterface.getSystem(),times(2)).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		} else {
			verify(CoreInterface.getSystem(),never()).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		}
		try {
			verify(m_connection).prepareStatement(eq("INSERT INTO `mockTable` (id,data) VALUES (5,50)"), anyInt());
		} catch (SQLException e) {
			assertTrue(false);
		}

		// given
		f_generatedKeyRS = mock(ResultSet.class);
		try {
			given(m_ps.getGeneratedKeys()).willReturn(f_generatedKeyRS);
			given(f_generatedKeyRS.next()).willReturn(true, false);
			java.sql.ResultSetMetaData l_metaMock = mock (java.sql.ResultSetMetaData.class);
			given(f_generatedKeyRS.getMetaData()).willReturn(l_metaMock );
			given(f_generatedKeyRS.getLong(anyInt())).willReturn(52l);
			given(l_metaMock.getColumnCount()).willReturn(1);
		} catch (SQLException e1) {
		}

		// when
		bp.setBlockOn(false);
		bp.setNoDuplicateCheck(false);
		// then
		assertTrue(bp.addContent(mockData5));
		assertTrue(bp.addContent(mockData5));
		if (p_loggingOn) {
			verify(CoreInterface.getSystem(),times(4)).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		} else {
			verify(CoreInterface.getSystem(),never()).trace(eq(MockData.class), eq("/trace/block"), eq("Adding content: "+bp+" adds "+mockData5));
		}
		try {
			verify(m_connection,times(3)).prepareStatement(eq("INSERT INTO `mockTable` (id,data) VALUES (5,50)"), anyInt());
		} catch (SQLException e) {
			assertTrue(false);
		}

	}


	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#removeContent(java.sql.Connection, com.airsltd.core.data.BlockData)}.
	 */
	@Test
	public final void testRemoveContentLoggingOn() {
		testRemoveContentBdd(true);
	}
	
	@Test
	public final void testRemoveContentLoggingOff() {
		testRemoveContentBdd(false);
		f_generatedKeyRS = mock(ResultSet.class);
		try {
			given(m_connection.createStatement()).willThrow(mock(SQLException.class));
		} catch (SQLException e1) {
		}

		// when
		bp.setBlockOn(false);
		bp.setNoDuplicateCheck(false);
		// then
		assertFalse(bp.removeContent(mockData5));
	}
	
	@SuppressWarnings("unchecked")
	private final void testRemoveContentBdd(boolean p_loggingOn) {
		// given - see setup()
		bp.setTracing(p_loggingOn);
		bp.setDebugging(p_loggingOn);
		
		IModelListener<MockData> l_modelListener = mock(IModelListener.class);

		// when
		bp.addModelListener(l_modelListener);
		bp.setBlockOn(true);
		assertTrue(bp.removeContent(mockData1));
		assertTrue(bp.removeContent(mockData1));
		
		// then
		assertEquals(2, bp.getBlockRemoveData().size());
		assertTrue(bp.getBlockRemoveData().contains(mockData1));
		if (p_loggingOn) {
			verify(CoreInterface.getSystem(),times(2)).trace(eq(MockData.class), eq("/trace/block"), eq("Removing content: "+bp+" removes "+mockData1));
		} else {
			verify(CoreInterface.getSystem(),never()).trace(eq(MockData.class), eq("/trace/block"), eq("Removing content: "+bp+" removes "+mockData1));
		}

		// when
		bp.setBlockOn(false);
		assertTrue(bp.removeContent(mockData5));
		// then
		verify(l_modelListener).removeData(new HashSet<MockData>(Arrays.asList(mockData5)));
		try {
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE id=5 AND f_published=0");
		} catch (SQLException e) {
			assertTrue(false);
		}
		
	}

	@Test
	public final void testRemoveContentThrow() {
		try {
			// given
			doThrow(new SQLException()).when(m_connection).close();
			// when
			// then
			bp.removeContent(mockData1);
			// when
		} catch (SQLException p_exception) {
		}
		// verify
		verify(CoreInterface.getSystem(),times(1)).handleException(anyString(), 
				any(Exception.class), any(NotificationStatus.class));
	}
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#updateContent(java.sql.Connection, com.airsltd.core.data.BlockData, com.airsltd.core.data.BlockData)}.
	 */
	@Test
	public final void testUpdateContentLoggingOn() {
		testUpdateContentBdd(true);
	}
	
	@Test
	public final void testUpdateContentLoggingOff() {
		testUpdateContentBdd(false);
		// given
		MockData l_mockData = new MockData(mockData1.someId, mockData1.someData);
		// when
		assertTrue(bp.updateContent(mockData1, l_mockData));
		mockData1.someHiddenData = 5;
		assertTrue(bp.updateContent(mockData1, l_mockData));
	}
	
	private final void testUpdateContentBdd(boolean p_loggingOn) {
		// given - see setup()
		bp.setTracing(p_loggingOn);
		bp.setDebugging(p_loggingOn);

		// when
		bp.setBlockOn(true);
		MockData tData = new MockData(5,20);
		assertTrue(bp.updateContent(mockData5, tData ));
		assertTrue(bp.updateContent(mockData5, tData));

		// then
		assertEquals(2, bp.getBlockUpdateData().size());
		assertTrue(bp.getBlockUpdateData().contains(new BlockMod<MockData>(mockData5, tData)));
		if (p_loggingOn) {
			verify(CoreInterface.getSystem(),times(2)).trace(eq(MockData.class), eq("/trace/block"), eq("Updating content: "+bp+" updates "+mockData5+" with "+tData));
		} else {
			verify(CoreInterface.getSystem(),never()).trace(eq(MockData.class), eq("/trace/block"), eq("Updating content: "+bp+" updates "+mockData5+" with "+tData));
		}

		// when
		bp.setBlockOn(false);
		assertTrue(bp.updateContent(mockData5, tData));
		// then
		try {
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=20 WHERE id=5");
		} catch (SQLException e) {
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#endBlock(java.sql.Connection)}.
	 */
	@Test
	public final void testEndBlockLoggingOn() {
		testEndBlockBDD(true);
	}
	
	
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#endBlock(java.sql.Connection)}.
	 */
	@Test
	public final void testEndBlockLoggingOff() {
		testEndBlockBDD(false);
	}
	
//	@Test
//	public final void testEndBlockBooleanThrows() {
//		try {
//			// given
//			doThrow(new SQLException()).doNothing(). when(m_connection).setAutoCommit(false);
//			doNothing().doThrow(new SQLException()).when(m_connection).close();
//			// when
//			bp.startBlock();
//			// then
//			assertFalse(bp.endBlock(false));
//			bp.endBlock(false);
//			// when
//		} catch (SQLException p_exception) {
//		}
//		// verify
//		verify(CoreInterface.getSystem(),times(2)).handleException(anyString(), 
//				any(Exception.class), any(NotificationStatus.class));
//	}
//
	@Test
	public final void testEndBlockAirsConnectionBooleanThrows() {
		Connection l_conn = null;
		Connection l_sqlConn = null;
		try {
			// given
			l_conn = mock(Connection.class);
			l_sqlConn = mock(Connection.class);
			doThrow(new SQLException()). when(l_sqlConn).setAutoCommit(false);
			// when
			bp.cancelBlock();
			// then
			assertFalse(bp.endBlock(l_conn, false));
			// when
			bp.startBlock();
			bp.endBlock(l_sqlConn,false);
//			assertTrue(false);
		} catch (SQLException p_exception) {
			
		}
			// verify
		try {
			assertNotNull(l_conn);
//			verify(l_sqlConn).rollback();
//		} catch (SQLException p_exception) {
		} finally {
		}
	}
	
	@SuppressWarnings("unchecked")
	private final void testEndBlockBDD(boolean p_loggingOn) {
		// given - see setup()
		ResultSetMetaData m_metaResultSet = mock(ResultSetMetaData.class);
		List<MockData> l_list = new ArrayList<MockData>();
		l_list.add(mockData1);
		try {
			given(m_ps.getGeneratedKeys()).willReturn(m_rs);
			given(m_rs.getMetaData()).willReturn(m_metaResultSet);
			given(m_metaResultSet.getColumnCount()).willReturn(2);
		} catch (SQLException e) {
			assertTrue(false);
		}
		bp.setTracing(p_loggingOn);
		bp.setDebugging(p_loggingOn);
		
		IModelListener<MockData> m_modelListener = mock(IModelListener.class);
		// when
		bp.setBlockOn(true);
		bp.setBlockSize(2);
		bp.addModelListener(m_modelListener);
		MockData tData = new MockData(5,20);
		MockData t2Data = new MockData(4,90);
		assertTrue(bp.updateContent(mockData5, tData ));
		assertTrue(bp.updateContent(mockData4, t2Data));
		assertTrue(bp.endBlock());
		assertFalse(bp.isBlockOn());
		// then
		try {
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE (id=2 AND f_published=0)");
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE (id=3)");
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=20 WHERE id=5");
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=90 WHERE id=4");
			verify(m_connection).prepareStatement(eq("INSERT INTO `mockTable` (id,data) VALUES (1,10), (4,40)"), anyInt());
			if (p_loggingOn) {
				verify(CoreInterface.getSystem()).debug(eq(MockData.class), eq("/debug/block"), eq("SQL - Adding data"));
			} else {
				verify(CoreInterface.getSystem(),never()).debug(eq(MockData.class), eq("/debug/block"), eq("SQL - Adding data"));
			}
			verify(m_modelListener).blockUIStart();
			verify(m_modelListener).addData(anySetOf(MockData.class));
			verify(m_modelListener).removeData(anySetOf(MockData.class));
			verify(m_modelListener).blockUIStop();
		} catch (SQLException e) {
			assertTrue(false);
		}
		
	}
	
	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#getBlockSize()}.
	 */
	@Test
	public final void testGetBlockSize() {
		assertEquals(40, bp.getBlockSize());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#setBlockSize(int)}.
	 */
	@Test
	public final void testSetBlockSize() {
		bp.setBlockSize(30);
		assertEquals(30, bp.getBlockSize());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#getBlockSize()}.
	 */
	@Test
	public final void testIsNoDuplicateCheck() {
		assertFalse(bp.isNoDuplicateCheck());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#setBlockSize(int)}.
	 */
	@Test
	public final void testSetNoDuplicateCheck() {
		bp.setNoDuplicateCheck(true);
		assertTrue(bp.isNoDuplicateCheck());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.BlockProvider#getBlockData()}.
	 */
	@Test
	public final void testGetBlockData() {
		assertTrue(bp.getBlockData().contains(mockData1));
		assertTrue(bp.getBlockRemoveData().contains(mockData2));
		assertEquals(mockData3,bp.getBlockUpdateData().iterator().next().getOldValue());
		assertEquals(mockData4,bp.getBlockUpdateData().iterator().next().getNewValue());
	}

	/**
	 * Test method form {@link com.aisltd.core.data.BlockProvider#cancelBlock()}.
	 */
	@Test
	public final void testCancelBlock() {
		// given
		bp.setBlockOn(true);
		// when
		bp.cancelBlock();
		// then
		assertFalse(bp.isBlockOn());
		
	}
	
	@Test
	public final void testCanEdit() {
		// given
		// when
		// then
		assertTrue(bp.canEdit(mockData1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testLoadContent() {
		// given
		IModelListener<MockData> l_modelListener3 = mock(IModelListener.class);
		IModelListener<MockData> l_modelListener = mock(IModelListener.class);
		IModelListener<MockData> l_modelListener2 = mock(IModelListener.class);
		given(l_modelListener.loadContent(eq(mockData1), anyBoolean())).willReturn(mockData1);
		given(l_modelListener3.loadContent(eq(mockData1), anyBoolean())).willReturn(null,mockData2, mockData1);
		given(l_modelListener2.loadContent(eq(mockData1), anyBoolean())).willReturn(mockData1);
		// when
		bp.addModelListener(l_modelListener);
		bp.addModelListener(l_modelListener3);
		bp.addModelListener(l_modelListener2);
		// then
		assertTrue(bp.loadContent(mockData1, true)==null);
		assertEquals(mockData2, bp.loadContent(mockData1, false));
		assertEquals(mockData1,bp.loadContent(mockData1,false));
		verify(l_modelListener).loadContent(mockData1,true);
		verify(l_modelListener,times(2)).loadContent(mockData1,false);
		verify(l_modelListener3,times(3)).loadContent(eq(mockData1), anyBoolean());
		verify(l_modelListener2).loadContent(eq(mockData1), anyBoolean());
	}
	
	@Test
	public final void testGetBlockUpdateOriginals() {
		// given
		// when
		Set<MockData> l_list = bp.getBlockUpdateOriginals();
		// then
		assertEquals(1, l_list.size());
		assertTrue(l_list.contains(mockData3));
		
	}
	
	@Test
	public final void testDoAddSql() {
		// this method needs to be tested for the auto increment aspects
		// given - see setup()
		mockData1.f_auto = true;
		ResultSetMetaData m_metaResultSet = mock(ResultSetMetaData.class);
		List<MockData> l_list = new ArrayList<MockData>();
		l_list.add(mockData1);
		try {
			given(m_ps.getGeneratedKeys()).willReturn(m_rs);
			given(m_rs.getMetaData()).willReturn(m_metaResultSet);
			given(m_rs.next()).willReturn(true, false);
			given(m_rs.getLong(anyInt())).willReturn(52l);
			given(m_metaResultSet.getColumnCount()).willReturn(2);
			
			// when
			bp.doAddSQL("mock sql add", l_list, m_connection);
			// then
			assertEquals(52, mockData1.someId);
		} catch (SQLException e) {
			assertTrue(false);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testSetters() {
		// given
		Set<MockData> m_blockData = mock(Set.class);
		Set<MockData> m_blockRemoveData = mock(Set.class);
		Set<BlockMod<MockData>> m_blockUpdateData = mock(Set.class);
		// when
		bp.setBlockData(m_blockData);
		bp.setBlockRemoveData(m_blockRemoveData);
		bp.setBlockUpdateData(m_blockUpdateData);
		// then
		assertEquals(m_blockData, bp.getBlockData());
		assertEquals(m_blockRemoveData, bp.getBlockRemoveData());
		assertEquals(m_blockUpdateData, bp.getBlockUpdateData());
	}
	
	@Test
	public final void testGetConnection() {
		
	}
	
	@Test
	public final void testProcessAutoIncrements() {
		testInternalAutoIncrement(false);
	}

	@Test
	public final void testProcessAutoIncrementsLogging() {
		testInternalAutoIncrement(true);
	}
	
	private final void testInternalAutoIncrement(boolean p_loggingOn) {
		try {
			// given
			bp.setTracing(p_loggingOn);
			bp.setDebugging(p_loggingOn);
			ResultSet l_rs = mock(ResultSet.class);
			PreparedStatement l_ps = mock(PreparedStatement.class);
			ResultSetMetaData l_metaData = mock(ResultSetMetaData.class);
			given (m_ps.executeQuery()).willReturn(null, l_rs);
			given(l_rs.next()).willReturn(true,false);
			given(l_ps.getGeneratedKeys()).willReturn(l_rs);
			given(l_rs.getMetaData()).willReturn(l_metaData);
			given(l_metaData.getColumnCount()).willReturn(3);
			IBlockData l_data = mock(IBlockData.class);
			// when
			bp.processAutoIncrements(l_ps, l_data);
			bp.processAutoIncrements(l_ps, l_data);
			// then
			if (p_loggingOn) 
				verify(CoreInterface.getSystem()).trace(eq(MockData.class), eq("/trace/block"), eq("Autoincrement Field set: 0 set to 0"));
			verify(l_data,times(3)).autoIncrementField(anyInt(), anyLong());
		} catch (SQLException p_exception) {
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	public final void testRemoveModelListener() {
		// given
		IModelListener<MockData> l_modelListener = mock(IModelListener.class);
		IModelListener<MockData> l_modelListener2 = mock(IModelListener.class);
		// when
		bp.addModelListener(l_modelListener);
		bp.addModelListener(l_modelListener2);
		bp.removeModelListener(l_modelListener2);
		// then
		assertFalse(bp.getModelListeners().contains(l_modelListener2));
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	public final void testAddModelListener() {
		// given
		IModelListener<MockData> l_modelListener = mock(IModelListener.class);
		IModelListener<MockData> l_modelListener2 = mock(IModelListener.class);
		// when
		bp.addModelListener(l_modelListener);
		bp.addModelListener(l_modelListener2);
		bp.addModelListener(l_modelListener2);
		// then
		assertTrue(bp.getModelListeners().contains(l_modelListener2));
		assertTrue(bp.getModelListeners().contains(l_modelListener));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testAddContentImmediate() {
		try {
			// given
			MockData l_data = mock(MockData.class);
			doThrow(new SQLException()).doNothing().when(m_connection).close();
			IModelListener<MockData> l_modelListener = mock(IModelListener.class);
			bp.addModelListener(l_modelListener);
			given(l_modelListener.loadContent(eq(l_data), anyBoolean())).willReturn(l_data);
			// when
			bp.addContentImmediate(l_data);
			assertTrue(bp.addContentImmediate(l_data));
			// then
			verify(l_modelListener,times(2)).addData(eq(new HashSet<MockData>(Arrays.asList(l_data))));
		} catch (SQLException p_exception) {
			p_exception.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testUpdateContentImmediate() {
		try {
			// given
			MockData l_data = mock(MockData.class);
			MockData n_data = mock(MockData.class);
			doThrow(new SQLException()).doNothing().when(m_connection).close();
			IModelListener<MockData> l_modelListener = mock(IModelListener.class);
			bp.addModelListener(l_modelListener);
			given(l_modelListener.loadContent(eq(l_data), anyBoolean())).willReturn(l_data);
			final IUserInterfaceListener<MockData> l_uiListener = mock(IUserInterfaceListener.class);
			given(l_modelListener.prepareUpdateData(anySet(), anyList())).willAnswer(new Answer<Set<String>>() {

				@Override
				public Set<String> answer(InvocationOnMock p_invocation)
						throws Throwable {
					List<IUserInterfaceListener<MockData>> l_interface = (List<IUserInterfaceListener<MockData>>) p_invocation.getArguments()[1];
					l_interface.add(l_uiListener);
					Set<String> l_retVal = new HashSet<>();
					l_retVal.add("mockField");
					return l_retVal;
				}
			});
			// when
			assertTrue(bp.updateContentImmediate(l_data,n_data));
			List<MockData> l_listOfModified = new ArrayList<>();
			l_listOfModified.add(l_data);
			Set<String> l_expectedPropertySet = new HashSet<>();
			l_expectedPropertySet.add("mockField");
			// then
			verify(l_uiListener).modifyData(l_listOfModified, l_expectedPropertySet );
		} catch (SQLException p_exception) {
			p_exception.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	@Test
	public final void testProcessGeneratedKeysNull() {
		try {
			// this provides code coverage for a null list
			// given
			// when
			bp.processGeneratedKeys(m_ps, null);
			// then
		} catch (SQLException p_exception) {
		}
		
	}
	
	@Test
	public final void testDebugTraceSetters() {
		// given
		// when
		bp.setTracing(true);
		bp.setDebugging(true);
		// then
		assertTrue(bp.isTracing());
		assertTrue(bp.isDebugging());
	}
	
	@Test
	public final void testLoadDataBase() {
		// given
		try {
			given(m_rs.next()).willReturn(true, false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			assertTrue(false);
		}
		@SuppressWarnings("unchecked")
		IModelListener<MockData> l_listener = mock(IModelListener.class);
		bp.addModelListener(l_listener);
		// when
		bp.loadDataBase("sqlTest String 1", null);
		// then
		verify(l_listener).addData(anySetOf(MockData.class));
	}
	
	@Test
	public final void testLoadDataBaseList() {
		// given
		ISqlSelectorCallBack l_callBack = mock(ISqlSelectorCallBack.class);
		try {
			given(m_rs.next()).willThrow(new SQLException()).willReturn(true, true, false);
			doThrow(new ParseException("Mock String", 0)).doNothing().when(new MockData().toField(0)).fromSqlValue(anyString());
		} catch (SQLException | ParseException e1) {
			e1.printStackTrace();
			assertTrue(false);
		}
		// when
		try {
			bp.loadDataBaseList("sqlTest String E1", null);
		} catch (RuntimeException l_re) {
			assertTrue(l_re.getCause() instanceof SQLException);
		}
//		try {
//			bp.loadDataBaseList("sqlTest String E2", null);
//		} catch (RuntimeException l_re) {
//			assertTrue(l_re.getCause() instanceof InstantiationException);
//		}
//		try {
//			bp.loadDataBaseList("sqlTest String E3", null);
//		} catch (RuntimeException l_re) {
//			assertTrue(l_re.getCause() instanceof IllegalAccessException);
//		}
		try {
			bp.loadDataBaseList("sqlTest String E4", null);
		} catch (RuntimeException l_re) {
			assertTrue(l_re.getCause() instanceof ParseException);
		}
		// no throws 
		bp.loadDataBaseList("sqlTest String 1", null);
		bp.setTracing(true);
		bp.loadDataBaseList("sqlTest String 2", l_callBack);
		// then
		try {
			verify(l_callBack).loadSelectionString(m_ps);
			verify(CoreInterface.getSystem()).trace(eq(MockData.class), eq("/trace/block"), anyString());
		} catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testExtDataMods() {
		// given
		// when
		bpExt.addContent(new MockExtData(42, 540));
		bpExt.updateContent(mockExtData2, new MockExtData(13, 450));
		bpExt.removeContent(mockExtData1);
		// then
	}
	
	@Test
	public void testGetDataClass() {
		// given
		// when
		assertEquals(MockData.class, bp.getDataClass());
		// then
	}
	
	@Test
	public void testContinueProcessing() {
		// given
		// when
		bp.setContinueProcessing(true);
		// then
		assertTrue(bp.isContinueProcessing());
	}
	
	@Test
	public void testRemoveRecordHook() {
		// given
		// when
		// cc
		bp.removeRecordHook(mockData1);
		// normal
		bpExt.removeRecordHook(mockExtData1);
		// then
		assertEquals(1, mockExtData1.getRemoveCount());
	}
}
