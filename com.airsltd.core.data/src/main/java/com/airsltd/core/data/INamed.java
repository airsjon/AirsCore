/**
 *
 */
package com.airsltd.core.data;

/**
 * Data that is indexed by a string.
 *
 * @author Jon Boley
 *
 */
public interface INamed {
	/**
	 * The Name associated with the data.
	 * <p>
	 * It is expected that there is an isomorphic relationship between names and
	 * objects.
	 *
	 * @return a String representing the object.
	 */
	String toName();
}
