/**
 *
 */
package com.airsltd.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IPersistentId;

/**
 * @author Jon Boley
 *
 */
public class PersistentIdSegmentedListModel<T extends IBlockData & IPersistentId & ISegment<V>, V>
		extends SegmentedListModel<T, V>implements IFromIdModel<T> {

	private final Map<Long, T> f_indexedMap = new HashMap<Long, T>();

	public PersistentIdSegmentedListModel(Class<T> p_class) {
		this(p_class, new BlockProvider<T>(p_class));
	}

	public PersistentIdSegmentedListModel(Class<T> p_class, BlockProvider<T> p_provider) {
		super(p_provider);
		BlockModel.setFromIdModel(p_class, this);
	}

	/**
	 * Return the T element in segment p_segment of type V with id p_id
	 * 
	 * @param p_segment
	 * @param p_id
	 * @return
	 */
	public T getElement(V p_segment, long p_id) {
		T l_retVal = null;
		for (final T l_current : getContentAsList(p_segment)) {
			if (l_current.getPersistentID() == p_id) {
				l_retVal = l_current;
				break;
			}
		}
		return l_retVal;
	}

	@Override
	public T getElement(long p_id) {
		return f_indexedMap.get(p_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#loadContent(com.airsltd.core.data.
	 * IBlockData, boolean)
	 */
	@Override
	public T loadContent(T p_data, boolean p_update) {
		T l_retVal = p_data;
		if (!isInitialLoad()) {
			final List<T> l_content = getContentAsList(p_data.toSegment());
			final int l_index = l_content == null ? -1 : l_content.indexOf(p_data);
			if (l_index != -1) {
				l_retVal = l_content.get(l_index);
				if (p_update) {
					p_data.copy(l_retVal);
				}
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.model.SegmentedListModelExt#addModelData(java.util.List)
	 */
	@Override
	public Set<T> addModelData(Set<T> p_addData) {
		for (final T l_data : p_addData) {
			f_indexedMap.put(l_data.getPersistentID(), l_data);
		}
		return super.addModelData(p_addData);
	}

	@Override
	public void remModelData(Set<T> p_remData) {
		for (final T l_data : p_remData) {
			f_indexedMap.remove(l_data.getPersistentID());
		}
		super.remModelData(p_remData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#copyData(com.airsltd.core.data.
	 * IBlockData, com.airsltd.core.data.IBlockData)
	 */
	@Override
	public void copyData(T p_oldData, T p_newData) {
		if (p_oldData.getPersistentID() != p_newData.getPersistentID()) {
			f_indexedMap.remove(p_oldData.getPersistentID());
			f_indexedMap.put(p_newData.getPersistentID(), p_oldData);
		}
		super.copyData(p_oldData, p_newData);
	}

}
