package com.airsltd.core.data;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ComplexDataHooksTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		// given
		// when
		IComplexDataHooks l_hooks = ComplexDataHooks.getInstance(MockExtData.class);
		// then
		assertNotNull(l_hooks);
		// when
		ComplexDataHooks.setInstance(MockExtData.class, null);
		// then
		try {
			ComplexDataHooks.getInstance(MockExtData.class);
			assertTrue(false);
		} catch (AirsExtBlockInializationException l_e) {
		}
	}

}
