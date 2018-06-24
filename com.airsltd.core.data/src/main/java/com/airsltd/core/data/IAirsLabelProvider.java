/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.data;

/**
 * Provide for easy segmentation of an object into String values.
 * <p>
 * These labels should be used for output to users.
 *
 * @author Jon Boley
 *
 */
public interface IAirsLabelProvider<T> {

	/**
	 * Return the text associated with this object and column.
	 *
	 * @param p_object
	 *            not null, the object in question
	 * @param p_index
	 *            int, the index of the column to return
	 * @return a User oriented string.
	 */
	String getAirsColumnText(T p_object, int p_index);

	/**
	 * Return true if this column is effected by changes to the specified
	 * property.
	 *
	 * @param p_index
	 *            int, the index of the column being checked
	 * @param p_property
	 *            can be null, the property being looked at
	 * @return true if the comparator may have changed order
	 */
	boolean isAirsLabelProperty(int p_index, String p_property);

}
