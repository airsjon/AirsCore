/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;

import com.airsltd.core.IAirsUserData;
import com.airsltd.core.data.CoreInterface;

/**
 * Mock connection setup for standard SQL data store.
 * 
 * @author Jon Boley
 *
 */
public class ConnectionSetupTest extends BlockProviderSetupTest {

	protected MockConnectionCore m_system;
	protected Connection m_connection;
	protected PreparedStatement m_ps;
	protected ResultSet m_rs;
	protected Statement m_state;
	protected IAirsUserData m_userData;

	public void setUp() throws Exception {
		super.setUp();
		// given
		m_system = new MockConnectionCore();
		CoreInterface.setSystem(m_system);
		m_connection = mock(Connection.class);
		m_ps = mock(PreparedStatement.class);
		m_rs = mock(ResultSet.class);
		m_state = mock(Statement.class);
		m_userData = mock(IAirsUserData.class);
		m_system.setConnection(m_connection);
		m_system.setUserData(m_userData);
		m_system.setLog(mock(Log.class));

		given(m_connection.prepareStatement(anyString())).willReturn(m_ps);
		given(m_connection.prepareStatement(anyString(),anyInt())).willReturn(m_ps);
		given(m_connection.createStatement()).willReturn(m_state);
		given(m_state.executeQuery(anyString())).willReturn(m_rs);
		given(m_state.executeUpdate(anyString())).willReturn(4);
		given(m_ps.executeQuery()).willReturn(m_rs);
		
	}


}
