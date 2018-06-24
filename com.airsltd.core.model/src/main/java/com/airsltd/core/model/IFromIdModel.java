/**
 *
 */
package com.airsltd.core.model;

import com.airsltd.core.data.IBlockData;

/**
 * A model that can provide an element simply from an id.
 * <p>
 * All models that implement this interface are expected to provide a one-to-one, isomorphic mapping of long to T.
 * 
 * @author Jon Boley
 *
 */
public interface IFromIdModel<T extends IBlockData> {

	/**
	 * Return the object associated with p_id.
	 * 
	 * @param p_id  long, the id of the data to be returned.
	 * @return null (no object exists) or the object associate with p_id.
	 */
	T getElement(long p_id);

}
