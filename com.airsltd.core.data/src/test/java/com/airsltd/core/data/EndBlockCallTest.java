package com.airsltd.core.data;

import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.NotificationStatus;

public class EndBlockCallTest extends ConnectionSetup {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private EndBlockCall<MockData> f_endBlockCall;
	private BlockProvider<MockData> f_blockProvider;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		super.setUp();
		CoreInterface m_coreInterface = mock(CoreInterface.class);
		CoreInterface.setSystem(m_coreInterface);
		given(m_coreInterface.getConnection()).willReturn(m_connection);
		given(m_connection.getAutoCommit()).willReturn(true);
		f_blockProvider = mock(BlockProvider.class);
		Savepoint m_savePoint = mock(Savepoint.class);
		f_endBlockCall = new EndBlockCall<MockData>(true, f_blockProvider);
		given(f_blockProvider.initializeConnection(any(Connection.class))).willReturn(m_savePoint);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testCall() throws Exception {
		// given
		// when
		f_endBlockCall.call();
		// then
		verify(m_connection).setAutoCommit(false);
		verify(m_connection).commit();
		verify(m_connection).setAutoCommit(true);
		verify(f_blockProvider).endBlock(m_connection, true);
	}

	@Test
	public void testCallSQLExceptionSavePoint() throws Exception {
		//given
		doThrow(new SQLException()).when(m_connection).commit();
		// when
		f_endBlockCall.call();
		// then
		verify(f_blockProvider).trace("Attempt Rollback");
		verify(m_connection).rollback(any(Savepoint.class));
		verify(CoreInterface.getSystem()).handleException(eq("Unable to do block updates"), any(SQLException.class), eq(NotificationStatus.BLOCK));
	}
	
	@Test
	public void testCallSQLExceptionNullSavePoint() throws Exception {
		//given
		doThrow(new SQLException()).when(m_connection).commit();
		given(f_blockProvider.initializeConnection(m_connection)).willReturn(null);
		// when
		f_endBlockCall.call();
		// then
		verify(CoreInterface.getSystem()).handleException(eq("Unable to do block updates"), any(SQLException.class), eq(NotificationStatus.BLOCK));
	}
	
	@Test(expected=RuntimeException.class)
	public void testCallRTExceptionNoSavePoint() throws Exception {
		// given
		given(f_blockProvider.initializeConnection(m_connection)).willReturn(null);
		doThrow(new RuntimeException()).when(m_connection).setAutoCommit(false);
		// when
		f_endBlockCall.call();
	}
}
