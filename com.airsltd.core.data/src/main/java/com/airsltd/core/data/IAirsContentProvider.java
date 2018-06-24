/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.data;

/**
 * This interface describes the functions for updated the data store with any
 * changes made in the model
 *
 * @author Jon
 *
 */
public interface IAirsContentProvider<E> {
	/**
	 * Modify <E> data into the data store
	 *
	 * @param data
	 * @return
	 */
	boolean updateContent(E oldData, E newData);

	/**
	 * Add <E> data into the data store
	 *
	 * @param data
	 * @return
	 */
	boolean addContent(E data);

	/**
	 * Delete <E> data from the data store
	 *
	 * @param data
	 * @return
	 */
	boolean deleteContent(E data);

}
