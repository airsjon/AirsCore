package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.data.BlockProvider;

public class ListModelTest {

	private ListModel<MockDataSeg, Object> f_model;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		f_model = new ListModel<MockDataSeg, Object>(mock(BlockProvider.class)) {

		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddModelData() {
		// given
		// when
		f_model.addModelData(new HashSet<MockDataSeg>(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9) })));
		// then
		assertEquals(3, f_model.getData().size());
	}

	@Test
	public void testRemModelData() {
		// given
		f_model.addModelData(new HashSet<MockDataSeg>(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9) })));
		// when
		f_model.remModelData(new HashSet<MockDataSeg>(Arrays.asList(new MockDataSeg[] { new MockDataSeg(2, 4), new MockDataSeg(3,9) })));
		// then
		assertEquals(1, f_model.getData().size());
	}

	@Test
	public void testGetContentAsList() {
		// given
		// when
		assertEquals(0, f_model.getContentAsList(new Object()).size());
		f_model.addModelData(new HashSet<MockDataSeg>(Arrays.asList(new MockDataSeg[] { new MockDataSeg(1,1),
				new MockDataSeg(2,4), new MockDataSeg(3, 9) })));
		// then
		assertEquals(3, f_model.getContentAsList(new Object()).size());
		assertFalse(f_model.getContentAsList(new Object()) == f_model.getContentAsList(new Object()));
	}

}
