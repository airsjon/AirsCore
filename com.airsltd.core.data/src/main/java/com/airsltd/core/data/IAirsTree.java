/**
 *
 */
package com.airsltd.core.data;

/**
 * @author Jon Boley
 *
 */
public interface IAirsTree<T> extends Comparable<T> {

	/**
	 * Get the parent of this element.
	 *
	 * @return
	 */
	T getParent();

}
