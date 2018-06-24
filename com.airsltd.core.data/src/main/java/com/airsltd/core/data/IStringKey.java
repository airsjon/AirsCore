/**
 *
 */
package com.airsltd.core.data;

/**
 * A class that implements this interface provides a {@link String} key
 *
 * @author Jon Boley
 *
 */
public interface IStringKey {

	/**
	 * Return a key as a {@link String} value. Often used for SQL select
	 * clauses.
	 *
	 * @return {@link String}
	 */
	String toKey();
}
