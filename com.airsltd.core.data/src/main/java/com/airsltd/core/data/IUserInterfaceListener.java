/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.data;

import java.util.List;
import java.util.Set;

/**
 * A User Interface interacts with the model by implementing these methods and
 * installing itself as a UI in the model.
 *
 * @param T
 *            Class to be displayed in the interface.
 * @author Jon Boley
 *
 */
public interface IUserInterfaceListener<T> {

	/**
	 * Add the data in p_listToAdd onto the interface.
	 *
	 * @param p_listToAdd
	 *            not null, {@link List} of the T data being added
	 */
	void addData(Iterable<T> p_listToAdd);

	/**
	 * Remove the data in p_listToRemove from the interface.
	 *
	 * @param p_listToRemove
	 *            not null, the {@link List} of the T data to be removed
	 */
	void removeData(Iterable<T> p_listToRemove);

	/**
	 * Modify the data in p_listOfModified with p_properties in the interface.
	 *
	 * @param p_listOfModified
	 *            not null, the {@link List} of the T data to be modified
	 * @param p_properties
	 *            not null, a {@link Set} of properties that have been modified
	 *            in the data set
	 */
	void modifyData(Iterable<T> p_listOfModified, Set<String> p_properties);

}
