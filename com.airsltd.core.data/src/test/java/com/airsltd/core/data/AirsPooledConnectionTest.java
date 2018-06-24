package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.AirsPooledConnection;
import com.airsltd.core.data.ISqlConnection;

public class AirsPooledConnectionTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// clean Connection pool for testing
		AirsPooledConnection.setInstance(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAirsPooledConnection() throws SQLException {
		// given
		Connection m_connection = mock(Connection.class);
		DataSource m_datasource = mock(DataSource.class);
		AirsPooledConnection.getInstance().setDatasource(null);
		given(m_datasource.getConnection()).willReturn(m_connection );
		// when
		try {
			AirsPooledConnection.getInstance().getConnection();
			assertTrue(false);
		} catch (RuntimeException re) {
		} catch (SQLException e) {
		}
		AirsPooledConnection.getInstance().setDatasource(m_datasource);
		AirsPooledConnection.getInstance().getConnection();
		// then
		verify(m_datasource).getConnection();
	}

	@Test
	public final void testInitialize() {
		// given
		ISqlConnection m_connData = mock(ISqlConnection.class);
		given(m_connData.getUrl()).willReturn("www.nowhere.com");
		given(m_connData.getUser()).willReturn("jonb");
		given(m_connData.getPassword()).willReturn("mockpass");
		given(m_connData.getMaxActive()).willReturn(50);
		given(m_connData.getMaxWait()).willReturn(10);
		
		// when
		try {
			AirsPooledConnection.getInstance().initialize(m_connData);
		} catch (ClassNotFoundException e) {
			fail("Class not found");
		};
		// then
		
	}

}
