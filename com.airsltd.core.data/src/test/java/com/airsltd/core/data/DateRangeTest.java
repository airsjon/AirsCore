package com.airsltd.core.data;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateRangeTest {

	private DateRange f_range1;
	private DateRange f_range2;

	@Before
	public void setUp() throws Exception {
		f_range1 = new DateRange(new Date(new GregorianCalendar(2002,4,20).getTimeInMillis()));
		f_range2 = new DateRange(new Date(new GregorianCalendar(2002,4,20).getTimeInMillis()),
				new Date(new GregorianCalendar(1973, 5, 13).getTimeInMillis()));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStart() {
		// given
		// when
		// then
		assertEquals(new Date(new GregorianCalendar(2002, 4, 20).getTimeInMillis()), f_range1.getStart());
		assertEquals(new Date(new GregorianCalendar(2002, 4, 20).getTimeInMillis()), f_range2.getStart());
	}

	@Test
	public void testSetStart() {
		// given
		// when
		f_range1.setStart(new Date(new GregorianCalendar(2003,4,20).getTimeInMillis()));
		f_range2.setStart(new Date(new GregorianCalendar(2003, 7, 23).getTimeInMillis()));
		// then
		assertEquals(new Date(new GregorianCalendar(2003, 4, 20).getTimeInMillis()), f_range1.getStart());
		assertEquals(new Date(new GregorianCalendar(2003, 7, 23).getTimeInMillis()), f_range2.getStart());
	}

	@Test
	public void testGetEnd() {
		// given
		// when
		// then
		assertEquals(new Date(new GregorianCalendar(2002, 4, 20).getTimeInMillis()), f_range1.getEnd());
		assertEquals(new Date(new GregorianCalendar(1973, 5, 13).getTimeInMillis()), f_range2.getEnd());
	}

	@Test
	public void testSetEnd() {
		// given
		// when
		f_range1.setEnd(new Date(new GregorianCalendar(2003,4,20).getTimeInMillis()));
		f_range2.setEnd(new Date(new GregorianCalendar(2002, 4, 20).getTimeInMillis()));
		// then
		assertEquals(new Date(new GregorianCalendar(2003, 4, 20).getTimeInMillis()), f_range1.getEnd());
		assertEquals(new Date(new GregorianCalendar(2002, 4, 20).getTimeInMillis()), f_range2.getEnd());
	}

}
