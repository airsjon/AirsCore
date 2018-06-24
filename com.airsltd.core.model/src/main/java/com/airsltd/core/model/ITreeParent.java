/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.List;

/**
 * An object that has children.
 * <p>
 * An object that is an {@link ITreeParent} will be capable of getting the
 * children and determining if it has children.
 *
 * @author Jon Boley
 *
 */
public interface ITreeParent<T> {

	/**
	 * Return a {@link List} of the children that this parent has.
	 *
	 * @return {@link List} of T
	 */
	List<T> getChildren();

	/**
	 * Return <code>true</code> if the parent has any children.
	 *
	 * @return
	 */
	boolean hasChildren();

}
