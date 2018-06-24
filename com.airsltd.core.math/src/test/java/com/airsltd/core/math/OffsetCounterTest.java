package com.airsltd.core.math;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OffsetCounterTest {

	private static final int MINTEST = 0;
	private static final int MAXTEST = 100;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNext() {
		for (int cnt = MINTEST; cnt != MAXTEST; cnt++) {
			for (int gaps = 3; gaps != 50; gaps++) {
				for (int leaves = 0; leaves != gaps-1; leaves++) {
					OffsetCounter l_testCounter = new OffsetCounter(50, cnt, gaps);
					for (int gCnt = 0; gCnt != gaps; gCnt++) {
						l_testCounter.next();
					}
					for (int iCnt = 0; iCnt != leaves; iCnt++) {
						l_testCounter.inc();
					}
					assertEquals("Span: "+cnt+" Gaps: "+gaps+" Leaves: "+leaves, 50+cnt+gaps+leaves, l_testCounter.getCurrentValue());
				}
			}
		}
	}
	
	@Test
	public void testFoundBugs() {
		// given
		// when
		OffsetCounter l_counter = new OffsetCounter(20, 1, 3);
		// then
		assertEquals(22, l_counter.next());
		assertEquals(23, l_counter.inc());
		assertEquals(24, l_counter.next());
		assertEquals(25, l_counter.next());
	}

}
