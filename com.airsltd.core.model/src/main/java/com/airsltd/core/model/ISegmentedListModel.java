/**
 *
 */
package com.airsltd.core.model;

import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.IBlockData;

/**
 * Models that implement this interface provide core methods for segmented data.
 * <p>
 * Models implement this interface will also provide access to all the data in the model at once.
 * 
 * @author Jon Boley
 *
 */
public interface ISegmentedListModel<T extends IBlockData, V> {

	/**
	 * Load the data segmented by p_selection into p_data.
	 * <p>
	 * A destructive method, loadSegment will update p_data with all the data associated with p_selectiong.
	 * This allows for lazy loading of segmented data stores.
	 * 
	 * @param p_selection  can be null, the data segment to be loaded.
	 * @param p_data  not null, the list to store the data into.
	 */
	void loadSegment(V p_selection, Set<T> p_data);

	/**
	 * Determine if p_selection has been loaded.
	 * <p>
	 * An implementation could also take this opportunity to determine if a segment should be reloaded.
	 * 
	 * @param p_selection  not null, the data segment to check.
	 */
	void checkSegment(V p_selection);

	/**
	 * Return all the data in a model.
	 * <p>
	 * This can be a time expensive method. 
	 * Try to keep it out of UI threads.
	 * 
	 * @return a List of all the objects in the model.
	 */
	Set<T> getContentAsList();

	/**
	 * Return all the data in the model as a map.
	 * <p>
	 * Implementations should be careful to specify how they work with lazy load methods.
	 * 
	 * @return a Map<V, List<T>> of all the data in the model.
	 */
	Map<V, Set<T>> getData();

}
