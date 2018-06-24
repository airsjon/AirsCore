/**
 * 
 */
package com.airsltd.core.data;

/**
 * Data that is associated with an {@link IPersistentId}.
 * 
 * @author Jon Boley
 *
 */
public interface ILinkedData<T extends IPersistentId> {

	/**
	 * Return the linked data.
	 * 
	 * @return
	 */
	public T toLinkedData();
}
