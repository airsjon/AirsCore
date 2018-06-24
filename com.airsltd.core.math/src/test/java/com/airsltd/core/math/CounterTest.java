package com.airsltd.core.math;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CounterTest {

	public enum FieldStyle {
		LOWERCASE, UPPERCASE, CAPITALIZED, ASIS
	}

	private static final String NL = System.getProperty("line.separator");
	private static final String EXPECTED = "[Counter LOWERCASE:0,"+NL+
			"  UPPERCASE:1,"+NL+
			"  CAPITALIZED:4,"+NL+
			"  ASIS:2]";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Counter<FieldStyle> f_counter;

	@Before
	public void setUp() throws Exception {
		f_counter = Counter.fromClass(FieldStyle.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCount() {
		// given
		// when
		f_counter.count(FieldStyle.CAPITALIZED);
		f_counter.count(FieldStyle.CAPITALIZED);
		f_counter.count(FieldStyle.CAPITALIZED);
		f_counter.count(FieldStyle.CAPITALIZED);
		f_counter.count(FieldStyle.ASIS);
		f_counter.count(FieldStyle.ASIS);
		f_counter.count(FieldStyle.UPPERCASE);
		// then
		assertEquals(EXPECTED, f_counter.toString());
	}

}
