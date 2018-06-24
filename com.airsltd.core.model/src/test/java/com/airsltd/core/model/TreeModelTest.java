package com.airsltd.core.model;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeModelTest {

	/**
	 * @author Jon Boley
	 *
	 */
	public class MockTreeChild implements ITreeChild<MockTreeChild> {
		
		ITreeParent<MockTreeChild> f_parent;

		@Override
		public ITreeParent<MockTreeChild> getParent() {
			return f_parent;
		}

		@Override
		public void setParent(ITreeParent<MockTreeChild> p_parent) {
			f_parent = p_parent;
		}

	}

	/**
	 * @author Jon Boley
	 *
	 */
	public class MockTreeModel extends TreeModel<MockTreeChild, ITreeParent<MockTreeChild>> {

		/* (non-Javadoc)
		 * @see com.airsltd.core.model.TreeModel#loadModel(java.lang.Object)
		 */
		private int f_loadModelCount;

		@SuppressWarnings("unchecked")
		@Override
		public boolean loadModel(Object p_selector) {
			f_loadModelCount++;
			getHeads().add(mock(ITreeParent.class));
			return true;
		}

		/**
		 * @return the loadModelCount
		 */
		protected int getLoadModelCount() {
			return f_loadModelCount;
		}

	}

	private MockTreeModel f_treeModel;
	private MockTreeChild f_treeItem;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		f_treeModel = new MockTreeModel();
		f_treeItem = new MockTreeChild();
		f_treeItem.setParent(mock(ITreeParent.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetElements() {
		// given
		// when
		// then
		assertTrue(f_treeModel.getElements(null).isEmpty());
		assertEquals(1, f_treeModel.getElements(new Object()).size());
		assertEquals(1, f_treeModel.getElements(new Object()).size());
	}

	@Test
	public void testGetHeads() {
		// given
		// when
		assertTrue(f_treeModel.getHeads().isEmpty());
		f_treeModel.getElements(new Object());
		assertEquals(1, f_treeModel.getHeads().size());
		// then
	}

	@Test
	public void testSetHeads() {
		// given
		// when
		assertTrue(f_treeModel.getHeads().isEmpty());
		f_treeModel.getElements(new Object());
		assertEquals(1, f_treeModel.getHeads().size());
		f_treeModel.setHeads(new ArrayList<ITreeParent<MockTreeChild>>());
		assertTrue(f_treeModel.getHeads().isEmpty());
		// then
	}

}
