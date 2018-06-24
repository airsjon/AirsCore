/**
 *
 */
package com.airsltd.core;

/**
 * An object that implements this interface knows how to delete it's self.
 *
 * @author Jon
 *
 */

public interface IAirsDeletable {
	/**
	 * method to delete the object. This method should handle all the issues
	 * associated with deleting this object.
	 */
	void delete();

	/**
	 * Return true if this object can be deleted. This method can check whether
	 * it makes sense to allow a deletion of this object.
	 * 
	 * @return <code>true</code> if the object can be deleted
	 */
	boolean canDelete();
}
