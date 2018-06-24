/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

/**
 * A child that can access it's parent.
 *
 * @author Jon Boley
 *
 */
public interface ITreeChild<T> {

	/**
	 * Return the parent of this object.
	 *
	 * @return the {@link ITreeParent} of this object.
	 */
	ITreeParent<T> getParent();

	/**
	 * Set the parent for this object.
	 *
	 * @param p_parent
	 *            can be null, the new parent of this object
	 */
	void setParent(ITreeParent<T> p_parent);

}
