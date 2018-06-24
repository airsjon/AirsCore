/**
 *
 */
package com.airsltd.core.model;

import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IPersistentId;

/**
 * @author Jon Boley
 *
 */
public class PersistentIdListModelExt<T extends IBlockData & IPersistentId, V> extends ListModel<T, V>
		implements IFromIdModel<T> {

	public PersistentIdListModelExt(Class<T> p_class) {
		this(p_class, new BlockProvider<T>(p_class));
	}

	public PersistentIdListModelExt(Class<T> p_class, BlockProvider<T> p_provider) {
		super(p_provider);
		BlockModel.setFromIdModel(p_class, this);
	}

	public T getElement(V p_selector, long p_id) {
		T l_retVal = null;
		for (final T l_current : getContentAsList(p_selector)) {
			if (l_current.getPersistentID() == p_id) {
				l_retVal = l_current;
				break;
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#loadContent(com.airsltd.core.data.
	 * IBlockData, boolean)
	 */
	@Override
	public T loadContent(T p_data, boolean p_update) {
		final Set<T> l_map = getSelectorMap(getSelector(p_data));
		return loadContentFromMap(l_map, p_data, p_update);
	}

	/**
	 * Get the Segment that p_data belongs to.
	 * <p>
	 * For Classes that do not implement the {@link ISegment} interface, this
	 * method allows a complex retrievel method. This interface can also be used
	 * for Classes that have multiple segments.
	 * 
	 * @param p_data
	 * @return
	 * @see PersistentIdListModel
	 * @see PersistentIdSegmentedListModel
	 */
	protected V getSelector(T p_data) {
		return null;
	}

	protected Set<T> getSelectorMap(V p_object) {
		return getData();
	}

	@Override
	public T getElement(long p_id) {
		return getElement(null, p_id);
	}

}
