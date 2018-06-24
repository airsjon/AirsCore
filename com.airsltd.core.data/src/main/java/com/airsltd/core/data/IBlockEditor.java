/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

/**
 * provides means for the system to notify when a value has changed from the
 * original data. When data has changed then the Editor is notified so it can do
 * any view modifications it deems necessary.
 * 
 * @author Jon
 *
 */
public interface IBlockEditor<T extends AbstractBlockData> {

	/**
	 * Insert the value in the interface into the model. p_index points to the
	 * value to retrieve from the interface nad p_dataHolder is the model object
	 * to be updated.
	 *
	 * @param p_index
	 *            the field to modify
	 * @param p_dataHolder
	 *            the model object to recieve the updated data
	 */
	void updateDataHolder(int p_index, T p_dataHolder);

	/**
	 * If the modified state for field p_index has changed the editor is
	 * notified. p_differnt will be true if the field is modified, false if not.
	 * If the editor handles the state change for the view then true is returned
	 * (false will tell the system to do it's default behavior)
	 *
	 * @param p_index
	 * @param p_different
	 */
	boolean fieldChanged(int p_index, boolean p_different);

	/**
	 * The editor is notified if data holder value has changed state with
	 * respect to being equal to the original data
	 *
	 * @param p_different
	 *            false - no change true - changed
	 */
	void dataModified(boolean p_different);
}
