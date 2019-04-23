/**
 *
 */
package com.airsltd.core.collections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Jon Boley
 *
 */
public class AirsCollections {

	/**
	 * Private constructor to make it impossible to create this helper class.
	 */
	private AirsCollections() {
	}

	/**
	 * Test for empty list with null representing an empty list.
	 *
	 * @param p_class
	 * @return
	 */
	public static <T> BooleanFunction<List<T>> emptyList(Class<T> p_class) {
		return p_object -> p_object == null || p_object.isEmpty();
	}

	/**
	 * Separate a list into two lists based on a property being set.
	 *
	 * @param p_orginalList
	 *            not null, an {@link Iterable} of T that is to be split based
	 *            on p_propety being true or false
	 * @param p_positiveList
	 *            not null, a list that will be destructively modified with
	 *            elements that will have the property set
	 * @param p_negativeList
	 *            not null, a list that will be destructively modified with
	 *            elements that do not have the property set
	 * @param p_property
	 *            not null, the property to look for in T
	 * @param T
	 *            not null, a type that extends {@link IEnumProperty}
	 * @param U
	 *            not null, an Enum
	 */
	public static <U extends Enum<U>, T extends IEnumProperty<U>> void sort(Iterable<? extends T> p_orginalList,
			List<? super T> p_positiveList, List<? super T> p_negativeList, U p_property) {
		for (final T l_element : p_orginalList) {
			if (l_element.isProperty(p_property)) {
				p_positiveList.add(l_element);
			} else {
				p_negativeList.add(l_element);
			}
		}
	}

	/**
	 * Separate the elements of an Iterable into two lists based on a set of
	 * properties being set.
	 *
	 * @param p_orginalList
	 * @param p_positiveList
	 * @param p_negativeList
	 * @param p_property
	 */
	public static <U extends Enum<U>, T extends IEnumProperty<U>> void sort(Iterable<? extends T> p_orginalList,
			List<? super T> p_positiveList, List<? super T> p_negativeList, EnumSet<U> p_property) {
		final int l_size = p_property.size();
		for (final T l_element : p_orginalList) {
			final Set<U> l_property = p_property.clone();
			l_property.removeAll(l_element.getQuality());
			if (l_property.size() == l_size) {
				p_negativeList.add(l_element);
			} else {
				p_positiveList.add(l_element);
			}
		}
	}

	/**
	 * Test each element of p_theList with p_testMethod.
	 * <p>
	 *
	 * @param p_testMethod
	 *            not null, the {@link BooleanFunction} to test each element
	 *            with
	 * @param p_theList
	 *            not null, any {@link Iterable} of T to test on
	 * @param p_retVal
	 *            the value to return if an element is found to match the
	 *            condition
	 * @param T
	 *            the Class to be testing in the list
	 * @return p_retVal is an element satisfies p_testMethod, othewise !p_retVal
	 */
	public static <T> boolean testFor(BooleanFunction<T> p_testMethod, Iterable<T> p_theList, boolean p_retVal) {
		boolean l_retVal = !p_retVal;
		for (final T l_element : p_theList) {
			if (p_testMethod.apply(l_element)) {
				l_retVal = p_retVal;
				break;
			}
		}
		return l_retVal;
	}

	/**
	 * Look for the first element that satisfies p_testMethod.
	 *
	 * @param p_testMethod
	 *            not null, method to use in looking for a T object in the list
	 * @param p_contentAsList
	 *            not null, an {@link Iterable} of T to be searched
	 * @param T
	 *            the class of objects being searched
	 * @return the first element in List that returns true when p_testMethod is
	 *         applied to the element.
	 */
	public static <T> T lookFor(BooleanFunction<T> p_testMethod, Iterable<T> p_contentAsList) {
		T l_retVal = null;
		for (final T l_element : p_contentAsList) {
			if (p_testMethod.apply(l_element)) {
				l_retVal = l_element;
				break;
			}
		}
		return l_retVal;
	}

	/**
	 * Apply the method p_applyMethod to all objects in p_theList.
	 *
	 * @param p_applyMethod
	 *            not null, the method to be applied
	 * @param p_theList
	 *            not null, the {@link Iterable} of T to apply p_applyMethod on
	 * @see IApplyMethod
	 */
	public static <T> void apply(IApplyMethod<T> p_applyMethod, Iterable<T> p_theList) {
		for (final T l_element : p_theList) {
			if (p_applyMethod.apply(l_element)) {
				break;
			}
		}
	}

	public static <T> List<T> findAll(BooleanFunction<T> p_testor, Iterable<T> p_theList) {
		final List<T> l_retVal = new ArrayList<T>();
		p_theList.forEach(o1 -> {
			if (p_testor.apply(o1)) {
				l_retVal.add(o1);
			}
		});
		return l_retVal;
	}
	
	public static <T> void removeIf(BooleanFunction<T> p_testor, Iterable<T> p_theList) {
		Iterator<T> l_iter = p_theList.iterator();
		while (l_iter.hasNext()) {
			if (p_testor.apply(l_iter.next())) {
				l_iter.remove();
			}
		}
	}

}
