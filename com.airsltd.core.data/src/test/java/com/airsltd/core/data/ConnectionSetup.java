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

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.data.CoreInterface;

/**
 * Set up a mock database connection.  Also sets up a mock {@link ICoreInterface} by calling the
 * setup and teardown methods of {@link MockSystemSetup}
 * 
 * @author Jon Boley
 *
 */
public class ConnectionSetup extends MockSystemSetup {

	protected Connection m_connection;
	protected PreparedStatement m_ps;
	protected ResultSet m_rs;
	protected Statement m_state;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		// given
		m_connection = mock(Connection.class);
		m_ps = mock(PreparedStatement.class);
		m_rs = mock(ResultSet.class);
		m_state = mock(Statement.class);
		given(m_connection.prepareStatement(anyString())).willReturn(m_ps);
		given(m_connection.prepareStatement(anyString(),anyInt())).willReturn(m_ps);
		given(m_connection.createStatement()).willReturn(m_state);
		given(m_ps.executeQuery()).willReturn(m_rs);
		given(m_state.executeUpdate(anyString())).willReturn(1);
		given(CoreInterface.getSystem().getConnection()).willReturn(m_connection);
	} 
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
