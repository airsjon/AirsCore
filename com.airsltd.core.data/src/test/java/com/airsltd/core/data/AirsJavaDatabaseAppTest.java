package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.data.AirsJavaDatabaseApp;
import com.airsltd.core.data.AirsPooledConnection;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.ExceptionCollector;
import com.airsltd.core.data.ISqlConnection;

public class AirsJavaDatabaseAppTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private AirsJavaDatabaseApp f_databaseConnection;

	@Before
	public void setUp() throws Exception {
		f_databaseConnection = new AirsJavaDatabaseApp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitializeDatabase() {
		// given
		ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
		// when
		f_databaseConnection.initializeDatabase(l_sqlConnection);
		// then
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSystemInterface() {
		// given
		ExceptionCollector<Object> l_collector = mock(ExceptionCollector.class);
		ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
		f_databaseConnection.initializeDatabase(l_sqlConnection);
		// when
		ICoreInterface l_testInterface = CoreInterface.getSystem();
		// then
		assertNull(l_testInterface.getUserData());
		l_testInterface.loadProperty(new Object(), "", new Object());
		assertNull(l_testInterface.getProperty(new Object(), ""));
		assertFalse(l_testInterface.isProperty(""));
		l_testInterface.setPersistentProperty("", "");
		assertNull(l_testInterface.getPersistentProperty("", ""));
		assertFalse(l_testInterface.isPersistentProperty(""));
		assertNull(l_testInterface.productName());
		l_testInterface.playSound("", "");
		l_testInterface.pushExceptionCollector(l_collector);
		assertEquals(l_collector, l_testInterface.popExceptionCollector());
		assertFalse(l_testInterface.traceActive(AirsJavaDatabaseApp.class, ""));
		assertNull(l_testInterface.getApplication());
		assertEquals(DateFormat.getDateInstance(), l_testInterface.getDateFormat());
		
	}
	
	@Test
	public void testGetConnection() {
		AirsPooledConnection l_oldConnection = AirsPooledConnection.getInstance();
		try {
			// given
			AirsPooledConnection l_connection = mock(AirsPooledConnection.class);
			AirsPooledConnection.setInstance(l_connection);
			ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
			f_databaseConnection.initializeDatabase(l_sqlConnection );
			Connection l_conn = mock(Connection.class);
			try {
				given(l_connection.getConnection()).willThrow(new SQLException()).willReturn(l_conn);
			} catch (SQLException e) {
				e.printStackTrace();
				assertTrue(false);
			}
			// when
			CoreInterface.getSystem().getConnection();
			Connection l_returnedConn = CoreInterface.getSystem().getConnection();
			// then
			assertEquals(l_conn, l_returnedConn);
		} finally {
			AirsPooledConnection.setInstance(l_oldConnection);
		}
	}
}
