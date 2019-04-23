package com.airsltd.core.collections;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AirsCollectionsTest {

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
	public void testEmptyList() {
		// given
		// when
		BooleanFunction<List<String>> l_test = AirsCollections.emptyList(String.class);
		// then
		assertFalse(l_test.apply(Arrays.asList("test")));
		assertTrue(l_test.apply(new ArrayList<String>()));
		assertTrue(l_test.apply(null));
	}

	@Test
	public void testSortProperty() {
		// given
		List<ObjectWithTestEnum> l_orig = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.SECOND, TestEnum.THIRD),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.THIRD));
		List<ObjectWithTestEnum> l_positive = new ArrayList<ObjectWithTestEnum>();
		List<ObjectWithTestEnum> l_negative = new ArrayList<ObjectWithTestEnum>();
		// when
		AirsCollections.sort(l_orig, l_positive, l_negative, TestEnum.FIRST);
		// then
		assertTrue(l_positive.contains(l_orig.get(0)));
		assertTrue(l_positive.contains(l_orig.get(2)));
		assertTrue(l_negative.contains(l_orig.get(1)));
		assertEquals(2,l_positive.size());
		assertEquals(1,l_negative.size());
	}

	@Test
	public void testSortEnumSetProperty() {
		// given
		List<ObjectWithTestEnum> l_orig = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.SECOND),
				new ObjectWithTestEnum(TestEnum.THIRD),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND),
				new ObjectWithTestEnum(TestEnum.SECOND, TestEnum.THIRD),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.THIRD),
				new ObjectWithTestEnum(false),
				new ObjectWithTestEnum(true));
		List<ObjectWithTestEnum> l_positive = new ArrayList<ObjectWithTestEnum>();
		List<ObjectWithTestEnum> l_negative = new ArrayList<ObjectWithTestEnum>();
		// when
		AirsCollections.sort(l_orig, l_positive, l_negative, EnumSet.of(TestEnum.FIRST, TestEnum.SECOND));
		// then
		assertEquals(6,l_positive.size());
		assertEquals(2,l_negative.size());
		assertTrue(l_negative.contains(l_orig.get(2)));
		assertTrue(l_negative.contains(l_orig.get(6)));
		assertTrue(l_positive.contains(l_orig.get(0)));
		assertTrue(l_positive.contains(l_orig.get(1)));
		assertTrue(l_positive.contains(l_orig.get(3)));
		assertTrue(l_positive.contains(l_orig.get(4)));
		assertTrue(l_positive.contains(l_orig.get(5)));
		assertTrue(l_positive.contains(l_orig.get(7)));
	}

	@Test
	public void testTestFor() {
		// given
		BooleanFunction<ObjectWithTestEnum> l_test = new BooleanFunction<ObjectWithTestEnum>() {
			
			@Override
			public Boolean apply(ObjectWithTestEnum p_object) {
				return p_object.isProperty(TestEnum.THIRD);
			}
		};
		Iterable<ObjectWithTestEnum> l_list = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.THIRD),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		Iterable<ObjectWithTestEnum> l_list2 = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		// when
		assertTrue(AirsCollections.testFor(l_test, l_list, true));
		assertFalse(AirsCollections.testFor(l_test, l_list2, true));
		assertFalse(AirsCollections.testFor(l_test, l_list, false));
		assertTrue(AirsCollections.testFor(l_test, l_list2, false));
		// then
	}

	@Test
	public void testLookFor() {
		// given
		BooleanFunction<ObjectWithTestEnum> l_test = new BooleanFunction<ObjectWithTestEnum>() {
			
			@Override
			public Boolean apply(ObjectWithTestEnum p_object) {
				return p_object.isProperty(TestEnum.THIRD);
			}
		};
		ObjectWithTestEnum l_third = new ObjectWithTestEnum(TestEnum.THIRD);
		Iterable<ObjectWithTestEnum> l_list = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				l_third,
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		Iterable<ObjectWithTestEnum> l_list2 = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		// when
		assertEquals(l_third, AirsCollections.lookFor(l_test, l_list));
		assertNull(AirsCollections.lookFor(l_test, l_list2));
		// then
	}

	@Test
	public void testApply() {
		// given
		List<ObjectWithTestEnum> l_array = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.THIRD),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		List<ObjectWithTestEnum> l_array2 = Arrays.asList(
				new ObjectWithTestEnum(TestEnum.FIRST),
				new ObjectWithTestEnum(TestEnum.FIRST, TestEnum.SECOND));
		Iterable<ObjectWithTestEnum> l_list = l_array;
		Iterable<ObjectWithTestEnum> l_list2 = l_array2;
		IApplyMethod<ObjectWithTestEnum> l_apply = new IApplyMethod<ObjectWithTestEnum>() {
			
			@Override
			public boolean apply(ObjectWithTestEnum p_element) {
				if (p_element.isProperty(TestEnum.THIRD)) {
					return true;
				} else {
					p_element.setProperty(TestEnum.THIRD, true);
					return false;
				}
			}
		};
		// when
		AirsCollections.apply(l_apply, l_list);
		AirsCollections.apply(l_apply, l_list2);
		// then
		assertTrue(l_array.get(0).isProperty(TestEnum.THIRD));
		assertFalse(l_array.get(2).isProperty(TestEnum.THIRD));
		assertTrue(l_array2.get(0).isProperty(TestEnum.THIRD));
		assertTrue(l_array2.get(1).isProperty(TestEnum.THIRD));
	}

	@Test
	public void testFindALL() {
		// given
		List<Integer> l_list = Arrays.asList(new Integer(3), new Integer(4), new Integer(54), new Integer(72), new Integer(5));
		// when
		List<Integer> l_found = AirsCollections.findAll(p -> { return (p & 1) == 0; }, l_list);
		// then
		assertTrue(l_found.contains(54));
		assertTrue(l_found.contains(4));
		assertTrue(l_found.contains(72));
		assertEquals(3, l_found.size());
	}
	
	@Test
	public void testRemoveIf() {
		// given
		List<Integer> l_list = new ArrayList<Integer>(Arrays.asList(new Integer(3), new Integer(4), new Integer(54), new Integer(72), new Integer(5)));
		// when
		AirsCollections.removeIf(p -> { return (p & 1) == 0; }, l_list);
		// then
		assertTrue(l_list.contains(3));
		assertTrue(l_list.contains(5));
		assertEquals(2, l_list.size());
	}
}
