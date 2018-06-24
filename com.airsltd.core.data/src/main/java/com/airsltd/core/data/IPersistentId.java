/**
 *
 */
package com.airsltd.core.data;

/**
 * <p>
 * A data object that implements this interface will return a unique Id for that
 * object. An object is permitted to return zero if the model does not know if
 * the data is already stored in the persistent database.
 * </p>
 *
 * <p>
 * Two objects of the same type must be equal if getPersistantId() returns the
 * same non-zero number. An object that is currently returning zero may be equal
 * to another object.
 * </p>
 *
 * @author Jon Boley
 *
 */
public interface IPersistentId extends IKeyedData {

	/**
	 * this method is called when a parent object needs to store the ID for this
	 * object. If -1 is returned then this object was not able to store itself
	 * correctly (if at all.)
	 *
	 * @return
	 */
	long getPersistentID();
}
