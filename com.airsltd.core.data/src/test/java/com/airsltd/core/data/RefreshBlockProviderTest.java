package com.airsltd.core.data;


import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.RefreshBlockProvider.DataCheck;

public class RefreshBlockProviderTest extends ConnectionSetup {
	
	class MockRefreshBlockProvider extends RefreshBlockProvider<MockData> {

		public MockRefreshBlockProvider() {
			super(MockData.class);
		}

		/**
		 * All data that has someHiddenData > 100 is redundant and does not need to be modified.
		 */
		@Override
		protected DataState refreshData(MockData p_data, MockData p_oldData,
				com.airsltd.core.data.RefreshBlockProvider.DataCheck p_state) {
			// if someHiddenData < 100 then the data was refreshed but the object in question still needs to be used
			// for modification
			boolean rVal = (p_data.someHiddenData<100) ;
			if (!rVal) {
				p_data.someData = p_data.someHiddenData;
			}
			return rVal?DataState.VALID:DataState.REDUNDENT;
		}

		@Override
		protected
		long getStorePoint(MockData p_data) {
			return p_data.someHiddenData;
		}

		@Override
		protected
		long getCurrentStorePoint(MockData p_data) {
			return p_data.someData;
		}

		@Override
		protected void updateCurrentStorePoint(MockData p_data) {
			p_data.someHiddenData = p_data.someData;
		}
	
		@Override
		protected void createConflict(MockData p_data, MockData p_newData,
				com.airsltd.core.data.RefreshBlockProvider.DataCheck p_dataCheck) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void refreshData(List<MockData> p_dataToUpdate) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected MockData findData(MockData p_data) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	MockData m_data1 = new MockData(1, 15);
	MockData m_data2 = new MockData(2, 20);
	/**
	 * Data using m_data3 will be ignored because after refresh it will be found to be redundant.
	 */
	MockData m_data3 = new MockData(3, 250);
	MockData m_data4 = new MockData(1, 72);
	MockData m_data5 = new MockData(2, 120);
	MockData m_data6 = new MockData(3, 140);
	MockData m_data7 = new MockData(7, 15);
	MockData m_data8 = new MockData(8, 20);
	MockData m_data9 = new MockData(9, 300);
	
	MockRefreshBlockProvider m_provider;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		given(CoreInterface.getSystem().getConnection()).willReturn(m_connection);
		m_provider = new MockRefreshBlockProvider();
		// model is up to date so do the modification
		m_data1.someHiddenData = 15;
		m_data1.f_auto = true;
		// model is not up to date but we still need to do modification
		m_data2.someHiddenData = 15;
		// model is not up to date and the modification has already been done or marked as an issue
		m_data2.f_auto = true;
		m_data3.someHiddenData = 225;
		m_data3.f_auto = true;
		m_data4.f_auto = true;
		m_data5.f_auto = true;
		m_data6.f_auto = true;
		m_data7.f_auto = true;
		m_data8.f_auto = true;
		m_data9.f_auto = true;
		// model is up to date so do the modification
		m_data7.someHiddenData = 1;
		// model is not up to date but we still need to do modification
		m_data8.someHiddenData = 15;
		// model is not up to date and the modification has already been done or marked as an issue
		m_data9.someHiddenData = 225;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public final void testEnumDataCheck() {
		// given
		// when
		// then
		assertEquals(DataCheck.ADD,DataCheck.valueOf("ADD"));
		try {
			DataCheck.valueOf("TEST");
			assertTrue(false);
		} catch (IllegalArgumentException e) {
		} catch (Throwable t) {
			assertTrue(false);
		}
	}

	@Test
	public final void testEndBlock() {
		// block on testing done in testEndBlock
		// given
		ResultSetMetaData m_metaResultSet = mock(ResultSetMetaData.class);
		try {
			given(m_ps.getGeneratedKeys()).willReturn(m_rs);
			given(m_rs.getMetaData()).willReturn(m_metaResultSet);
			given(m_rs.next()).willReturn(true, false);
			given(m_rs.getLong(anyInt())).willReturn(52l);
			given(m_metaResultSet.getColumnCount()).willReturn(2);
		} catch (SQLException e) {
			assertTrue(false);
		}
		m_provider.startBlock();
		// when
		// test add data 1
		assertTrue(m_provider.addContent(m_data1));
		// test add data 20
		assertTrue(m_provider.addContent(m_data2));
		// ignored data (assumes it has been found in the persistent store.)
		assertTrue(m_provider.addContent(m_data3));
		// remove 8
		assertTrue(m_provider.removeContent(m_data7));
		// remove 9
		assertTrue(m_provider.removeContent(m_data8));
		// ignore data (assume it has been removed from persistent store already.)
		assertTrue(m_provider.removeContent(m_data9));
		MockData m_data5 = new MockData(1, 812);
		m_data5.f_auto=true;
		assertTrue(m_provider.updateContent(m_data1, m_data5));
		m_provider.endBlock();
		// then
		assertEquals(m_data1.someHiddenData, m_data1.someData);
		assertEquals(m_data2.someHiddenData, m_data2.someData);
		assertEquals(m_data3.someHiddenData, m_data3.someData);
		try {
			verify(m_connection).prepareStatement("INSERT INTO `mockTable` (data) VALUES (15), (20)", 1);
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE (id=7 AND f_published=0) or (id=8 AND f_published=0)");
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=812 WHERE id=1");
		} catch (SQLException exception) {
		} 
	}

	@Test
	public final void testRemoveRefresh() {
		// given
		
		// when
		m_provider.removeRefresh(new MockData(15, 14), DataCheck.UPDATE);
		// then
		assertEquals(0,m_provider.getBlockData().size());
	}
	
	@Test
	public final void testRemoveContentT() {
		// block on testing done in testEndBlock
		// given
		// when
		assertTrue(m_provider.removeContent(m_data1));
		assertTrue(m_provider.removeContent(m_data2));
		assertTrue(m_provider.removeContent(m_data3));
		// then
		assertEquals(m_data1.someHiddenData, m_data1.someData);
		assertEquals(m_data2.someHiddenData, m_data2.someData);
		assertEquals(m_data3.someHiddenData, m_data3.someData);
		try {
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE (id=1 AND f_published=0)");
			verify(m_state).executeUpdate("DELETE FROM `mockTable` WHERE (id=2 AND f_published=0)");
			verify(m_state,never()).executeUpdate("DELETE FROM `mockTable` WHERE (id=3 AND f_published=0)");
		} catch (SQLException exception) {
		}
	}

	@Test
	public final void testAddContentT() {
		// block on testing done in testEndBlock
		// given
		// when
		assertTrue(m_provider.addContent(m_data1));
		assertTrue(m_provider.addContent(m_data2));
		assertTrue(m_provider.addContent(m_data3));
		// then
		assertEquals(m_data1.someHiddenData, m_data1.someData);
		assertEquals(m_data2.someHiddenData, m_data2.someData);
		assertEquals(m_data3.someHiddenData, m_data3.someData);
		try {
			verify(m_connection).prepareStatement("INSERT INTO `mockTable` (data) VALUES (15)", 1);
			verify(m_connection).prepareStatement("INSERT INTO `mockTable` (data) VALUES (20)", 1);
			verify(m_connection,never()).prepareStatement("INSERT INTO `mockTable` (data) VALUES (250)", 1);
		} catch (SQLException exception) {
		}
	}

	@Test
	public final void testUpdateContentTT() {
		// block on testing done in testEndBlock
		// given
		// when
		assertTrue(m_provider.updateContent(m_data1, m_data4));
		assertTrue(m_provider.updateContent(m_data2, m_data5));
		assertTrue(m_provider.updateContent(m_data3, m_data6));
		// then
		assertEquals(m_data1.someHiddenData, m_data1.someData);
		assertEquals(m_data2.someHiddenData, m_data2.someData);
		assertEquals(m_data3.someHiddenData, m_data3.someData);
		try {
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=72 WHERE id=1");
			verify(m_connection).prepareStatement("UPDATE `mockTable` SET data=120 WHERE id=2");
			verify(m_connection, never()).prepareStatement("UPDATE `mockTable` SET data=140 WHERE id=3");
		} catch (SQLException exception) {
		}
	}

	@Test
	public final void testRefreshDataBlock() {
		// currently nothing to check as these are hooks which will get called by other tests
	}

	@Test
	public final void testRefreshDataBlockEnd() {
		// currently nothing to check as these are hooks which will get called by other tests
	}

	@Test
	public final void testIsUptoDateListOfT() {
		// given
		List<MockData> l_list = new ArrayList<MockData>();
		l_list.add(m_data1);
		l_list.add(m_data2);
		l_list.add(m_data3);
		// when
		boolean [] rVal = m_provider.isUptoDate(l_list);
		// then
		assertEquals(3, rVal.length);
		assertTrue(rVal[0]);
		assertFalse(rVal[1]);
		assertTrue(rVal[0]);
	}

	@Test
	public final void testMakeUptoDateListOfT() {
		// given
		List<MockData> l_list = new ArrayList<MockData>();
		l_list.add(m_data1);
		l_list.add(m_data2);
		l_list.add(m_data3);
		// when
		m_provider.makeUptoDate(l_list);
		boolean [] rVal = m_provider.isUptoDate(l_list);
		// then
		assertEquals(3, rVal.length);
		assertTrue(rVal[0]);
		assertTrue(rVal[1]);
		assertTrue(rVal[0]);
	}

}
