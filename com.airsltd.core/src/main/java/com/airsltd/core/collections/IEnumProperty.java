/**
 *
 */
package com.airsltd.core.collections;

import java.util.Set;

/**
 * An object that has boolean properties of type T
 *
 * @author Jon Boley
 *
 */
public interface IEnumProperty<T extends Enum<T>> {
	/**
	 * Return true if the property is set.
	 *
	 * @param p_property
	 *            non null, the property to check for
	 * @return true if the property is set.
	 */
	boolean isProperty(T p_property);

	/**
	 * Set the property value of this to p_value.
	 *
	 * @param p_property
	 *            non null, the property to set/unset
	 * @param p_value
	 *            the value to set the property to
	 */
	void setProperty(T p_property, boolean p_value);

	/**
	 * Return an enumerated set of all the properties set in this.
	 *
	 * @return an EnumSet<T> of all properties that are set.
	 */
	Set<T> getQuality();
}
