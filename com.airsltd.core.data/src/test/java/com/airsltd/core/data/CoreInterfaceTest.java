/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.AirsPooledConnection;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.ExceptionCollector;


/**
 * @author Jon
 *
 */
public class CoreInterfaceTest extends ConnectionSetup {

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
		new CoreInterface();
		AirsPooledConnection.setInstance(mock(AirsPooledConnection.class));
		given(AirsPooledConnection.getInstance().getConnection()).willReturn(m_connection);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.airsltd.core.data.CoreInterface#getSystem()}.
	 */
	@Test
	public final void testGetSystem() {
		// given
		ICoreInterface mockInterface = mock(ICoreInterface.class);
		// when
		CoreInterface.setSystem(null);
		// then
		assertTrue(CoreInterface.getSystem() instanceof CoreInterface);
		// given
		// when
		CoreInterface.setSystem(mockInterface);
		// then
		assertEquals(mockInterface, CoreInterface.getSystem());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.CoreInterface#setSystem(com.airsltd.core.ICoreInterface)}.
	 */
	@Test
	public final void testSetSystem() {
		testGetSystem();
	}

	/**
	 * Test method for {@link com.airsltd.core.data.CoreInterface#globalHandleException(java.lang.String, java.lang.Exception, int)}.
	 */
	@Test
	public final void testHandleException() {
		// given
		CoreInterface.getSystem().setLog(mock(Log.class));
		// when
		try (ExceptionCollector<Object> l_ec = new ExceptionCollector<Object>("A test") {
					
					@Override
					public boolean collect(String p_message, Throwable p_e,
							NotificationStatus p_s) {
						return p_e instanceof SQLException;
					}
				}) {
			l_ec.call(new Callable<Object>() {

				@Override
				public Boolean call() throws Exception {
					CoreInterface.getSystem().handleException("Test error", new RuntimeException(), NotificationStatus.BLOCK);
					CoreInterface.getSystem().handleException("Test error", new SQLException(), NotificationStatus.BLOCK);
					fail("Pass out should happen");
					return false;
				}}, false);
		}
		// then
		verify(CoreInterface.getSystem().getLog()).error(eq("Test error"), any(RuntimeException.class));
		verify(CoreInterface.getSystem().getLog()).info(eq("A test"), any(SQLException.class));
	}

	/**
	 * Test method for {@link com.airsltd.core.data.CoreInterface#logTimedEventStart(boolean, java.lang.String)}.
	 */
	@Test
	public final void testLogTimedEventStart() {
		Log mockLog = mock(Log.class);

		// given
		given(mockLog.isWarnEnabled()).willReturn(true, false, true, false, true, false);
		given(mockLog.isTraceEnabled()).willReturn(true, true, false, true, true, false, true, true, false);
		
		// when
		CoreInterface.getSystem().setLog(mockLog);
		Long t1 = CoreInterface.getSystem().logTimedEventStart(true, "1");
		Long t2 = CoreInterface.getSystem().logTimedEventStart(true, "2");
		Long t3 = CoreInterface.getSystem().logTimedEventStart(false, "3");
		Long t4 = CoreInterface.getSystem().logTimedEventStart(false, "4");
		Long t5 = CoreInterface.getSystem().logTimedEventStart(true, null);
		Long t6 = CoreInterface.getSystem().logTimedEventStart(true, null);
		Long t7 = CoreInterface.getSystem().logTimedEventStart(false, null);
		Long t8 = CoreInterface.getSystem().logTimedEventStart(false, null);
		Long t9 = CoreInterface.getSystem().logTimedEventStart(true, "");
		Long t10 = CoreInterface.getSystem().logTimedEventStart(true, "");
		Long t11 = CoreInterface.getSystem().logTimedEventStart(false, "");
		Long t12 = CoreInterface.getSystem().logTimedEventStart(false, "");
		// then
		assertNull(t4);
		assertNull(t8);
		assertNull(t12);
		assertNotNull(t5);
		assertNotNull(t6);
		assertNotNull(t7);
		assertNotNull(t9);
		assertNotNull(t10);
		assertNotNull(t11);
		String t1String = String.format("@%,d: %s", t1/1000000, "1");
		String t2String = String.format("@%,d: %s", t2/1000000, "2");
		String t3String = String.format("@%,d: %s", t3/1000000, "3");
		verify(mockLog,times(1)).warn(t1String);
		verify(mockLog,times(1)).warn(t2String);
		verify(mockLog,times(1)).trace(t3String);
		verify(mockLog,times(2)).warn(anyString());
		verify(mockLog,times(1)).trace(anyString());
	}

	/**
	 * Test method for {@link com.airsltd.core.data.CoreInterface#logTimedEventEnd(boolean, java.lang.String, long)}.
	 */
	@Test
	public final void testLogTimedEventEnd() {
		Log mockLog = mock(Log.class);

		// given
		given(mockLog.isWarnEnabled()).willReturn(true, false, true, false, true, false);
		given(mockLog.isTraceEnabled()).willReturn(true, true, false, true, true, false, true, true, false);
		long l_startTime = System.nanoTime();
		
		// when
		CoreInterface.getSystem().setLog(mockLog);
		Long t1 = CoreInterface.getSystem().logTimedEventEnd(true, "1", l_startTime);
		Long t2 = CoreInterface.getSystem().logTimedEventEnd(true, "2", l_startTime);
		Long t3 = CoreInterface.getSystem().logTimedEventEnd(false, "3", l_startTime);
		Long t4 = CoreInterface.getSystem().logTimedEventEnd(false, "4", l_startTime);
		Long t5 = CoreInterface.getSystem().logTimedEventEnd(true, null, l_startTime);
		Long t6 = CoreInterface.getSystem().logTimedEventEnd(true, null, l_startTime);
		Long t7 = CoreInterface.getSystem().logTimedEventEnd(false, null, l_startTime);
		Long t8 = CoreInterface.getSystem().logTimedEventEnd(false, null, l_startTime);
		Long t9 = CoreInterface.getSystem().logTimedEventEnd(true, "", l_startTime);
		Long t10 = CoreInterface.getSystem().logTimedEventEnd(true, "", l_startTime);
		Long t11 = CoreInterface.getSystem().logTimedEventEnd(false, "", l_startTime);
		Long t12 = CoreInterface.getSystem().logTimedEventEnd(false, "", l_startTime);
		// then
		assertEquals(-1l,t4.longValue());
		assertEquals(-1l,t8.longValue());
		assertEquals(-1l,t12.longValue());
		assertNotNull(t1);
		assertNotNull(t2);
		assertNotNull(t3);
		assertNotNull(t5);
		assertNotNull(t6);
		assertNotNull(t7);
		assertNotNull(t9);
		assertNotNull(t10);
		assertNotNull(t11);
		verify(mockLog,times(2)).warn(anyString());
		verify(mockLog,times(1)).trace(anyString());
	}

	@Test
	public void testUpdatePassword() {
		// given
		String l_student = "blaarg";
		try {
			doReturn(true).doReturn(true).doReturn(false).doThrow(mock(SQLException.class)).when(m_rs).next();
			given(m_rs.getInt(1)).willReturn(1,0);
		} catch (SQLException e) {
			assertTrue(false);
		}
		// when
		assertTrue(CoreInterface.getSystem().updatePassword(l_student, "newPassword", "oldPassword"));
		assertFalse(CoreInterface.getSystem().updatePassword(l_student, "newPassword", "oldPassword"));
		assertFalse(CoreInterface.getSystem().updatePassword(l_student, "newPassword", "oldPassword"));
		try {
			assertFalse(CoreInterface.getSystem().updatePassword(l_student, "newPassword", "oldPassword"));
		} catch (RuntimeException l_re) {
			assertTrue(l_re.getCause() instanceof SQLException);
		}
		// then
		try {
			verify(m_ps,times(4)).setString(eq(1), eq("blaarg"));
			verify(m_ps,times(4)).setString(eq(2), eq("14a88b9d2f52c55b5fbcf9c5d9c11875"));
			verify(m_ps,times(4)).setString(eq(3), eq("cbba6d57536106f93cdeb6e426c2750e"));
		} catch (SQLException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testConvertPassword() {
		// given
		// when
		// then
		
		assertEquals("", CoreInterface.getSystem().convertPassword(null));
		assertEquals("", CoreInterface.getSystem().convertPassword(""));
		assertEquals("0cc175b9c0f1b6a831c399e269772661", CoreInterface.getSystem().convertPassword("a"));
		assertEquals("82c6f255a2e1081a8ca74d30b7191567", CoreInterface.getSystem().convertPassword("1thtjudv0psr0"));
		assertEquals("579eb3a969e45f89684107fd3d1847cd", CoreInterface.getSystem().convertPassword("180ns8hp5qf7u"));
		assertEquals("3dcae9b151cb6822d71a2ac014fdb3a9", CoreInterface.getSystem().convertPassword("airsnas250"));
	}
}
