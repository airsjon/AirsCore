/**
 *
 */
package com.airsltd.core.model;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.INamed;

/**
 * {@link NamedListModel} provides an extension to {@link ListModel} for data that can be named.
 * <p>
 * This allows for easy lookup of data from the list by name
 * with {@link NamedListModel#getElement(String, Object)}.
 *
 * @author Jon Boley
 *
 */
public class NamedListModel<T extends IBlockData & INamed, V> extends ListModel<T, V> {

	/**
	 * Create the model using p_provider
	 * 
	 * @param p_provider  not null, the BlockProvider that links to the data store.
	 */
	public NamedListModel(BlockProvider<T> p_provider) {
		super(p_provider);
	}

	/**
	 * Get the data in the subset select with p_selector and named p_name.
	 * <p>
	 * Iterating through the list of data associated with p_selector, the first object that is named p_name is returned.
	 * 
	 * @param p_name  String, the name of the object to be returned.
	 * @param p_selector can be null, the selector used to limit the search.
	 * @return the first INamed object that equals p_name is returned, null is returned if no such object exists.
	 */
	public T getElement(String p_name, V p_selector) {
		T l_retVal = null;
		for (final T l_element : getContentAsList(p_selector)) {
			if (p_name.equals(l_element.toName())) {
				l_retVal = l_element;
				break;
			}
		}
		return l_retVal;
	}

}
