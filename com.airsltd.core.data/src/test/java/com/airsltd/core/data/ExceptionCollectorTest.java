package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.ExceptionCollector;
import com.airsltd.core.data.PeelExecutionException;

public class ExceptionCollectorTest extends MockSystemSetup {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private ExceptionCollector<Object> f_collection;
	private RuntimeException f_suppressor;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		f_suppressor = new RuntimeException();
		f_collection = new ExceptionCollector<Object>("Don't Mock Me here") {

			@Override
			public boolean collect(String p_message, Throwable p_e, NotificationStatus p_s) {
				return p_e == f_suppressor;
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testClose() {
		// given
		// when
		f_collection.close();
		// then
		verify(CoreInterface.getSystem()).popExceptionCollector();
		// when
		assertFalse(f_collection.collect("Internal Error Mock", new Throwable(), NotificationStatus.BLOCK));
		assertTrue(f_collection.collect("Mock Message", f_suppressor, NotificationStatus.BLOCK));
		// then
	}

	@Test
	public void testGettersAndSetters() {
		// given
		// when
		f_collection.setDescription("Another collection point");
		f_collection.setLogException(false);
		// then
		assertEquals("Another collection point", f_collection.getDescription());
		assertFalse(f_collection.isLogException());
	}
	
	@Test(expected=RuntimeException.class)
	public void testCall() throws Exception {
		// given
		@SuppressWarnings("unchecked")
		Callable<Object> l_callable = mock(Callable.class);
		given(l_callable.call()).willReturn(true, false)
			.willThrow(new PeelExecutionException(f_collection))
			.willThrow(new Exception());
		// when
		assertEquals(Boolean.TRUE, f_collection.call(l_callable, false));
		assertEquals(Boolean.FALSE, f_collection.call(l_callable, false));
		assertEquals(Boolean.FALSE, f_collection.call(l_callable, false));
		f_collection.call(l_callable, false);
		// then
	}
	
	@Test(expected=PeelExecutionException.class)
	public void testCallThrowPeel() throws Exception {
		// given
		@SuppressWarnings("unchecked")
		Callable<Object> l_callable = mock(Callable.class);
		given(l_callable.call()).willThrow(new PeelExecutionException(mock(ExceptionCollector.class)));
		// when
		f_collection.call(l_callable, false);
	}

}
