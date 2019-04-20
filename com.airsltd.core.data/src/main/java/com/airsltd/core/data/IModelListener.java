/**
 * copyright 2012, Jon Boley all rights reserved
 */
package com.airsltd.core.data;

import java.util.List;
import java.util.Set;

/**
 * An object that implements IModelListener provides Model support for the Data system.
 * <p>
 * Models are the data objects that contain the data loaded in the application.
 *
 * @author Jon Boley
 *
 */
public interface IModelListener<T extends IBlockData> {

	/**
	 * Inform the Model that a block of updates are about to be processed
	 */
	void blockUIStart();

	/**
	 * Inform the Model that the block of updates has completed
	 */
	void blockUIStop();

	/**
	 * {@link List} of p_addData has been successfully added to the DataStream.
	 * <p>
	 * Update the model accordingly.
	 *
	 * @param p_addData  not null, Set of T elements that have been added to the data stream.
	 */
	void addData(Set<T> p_addData);

	/**
	 * {@link List} of {@link BlockMod}<T> has been modified.
	 * <p>
	 * Update the model accordingly.
	 *
	 * @param p_data  not null, Set of the BlockMod data
	 * @param p_listeners  not null, the listeners needing to be update on a modification
	 */
	Set<String> prepareUpdateData(Set<BlockMod<T>> p_data, List<IUserInterfaceListener<T>> p_listeners);

	/**
	 * {@link List} of p_remData has been removed from the DataStream.
	 * <p> 
	 * Update the model so that all the data in p_remData has been removed.
	 * 
	 * @param p_remData  not null, Set of all the data to be removed
	 */
	void removeData(Set<T> p_remData);

	/**
	 * Notify any UI Listeners that data has been updated.
	 * <p>
	 * For all data modified all UI's that have been associated with the model need to be notified.
	 * This notification includes a Set of properties that aid the UI in deciding what needs to be done.
	 *
	 * @param p_listeners  not null, list of the UI Listeners to notify
	 * @param p_properties  not null, a Set of properties that have been changed
	 * @param p_modifiedData  not null, the data that has been updated
	 */
	void notifyListeners(List<IUserInterfaceListener<T>> p_listeners, Set<String> p_properties, Set<T> p_modifiedData);

	/**
	 * Verify if the data already exists in the data model. 
	 * <p>
	 * A Model can lazy load data and it is the responsibility of loadContent to determine if the data needs to be loaded.
	 * Implementations should scan the Model until we find the actual data.
	 *
	 * @param p_data  not null, p_data to find in the model
	 * @param p_update  boolean, if true update the data with the model data's values otherwise return the data found
	 * @return p_data if nothing modified (data did not exist), null if p_data updated with data from model, or a new T if data found
	 * 
	 * @see IPersistentStore#loadContent
	 */
	T loadContent(T p_data, boolean p_update);
}
