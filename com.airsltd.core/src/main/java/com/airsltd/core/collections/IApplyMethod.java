/**
 *
 */
package com.airsltd.core.collections;

/**
 * A method that will apply some code to an object T.
 * <p>
 * Most common use is with the
 * {@link AirsCollections#apply(IApplyMethod, java.util.List)}
 *
 * @author Jon Boley
 *
 */
public interface IApplyMethod<T> {

	/**
	 * The method to apply to T p_element.
	 * <p>
	 * To break the iteration, return true.
	 *
	 * @param p_element
	 *            can be null, an element to be manipulated.
	 * @return true if the iteration should be ended.
	 */
	boolean apply(T p_element);
}
