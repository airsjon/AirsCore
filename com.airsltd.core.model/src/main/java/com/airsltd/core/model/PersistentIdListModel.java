/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IPersistentId;

/**
 * Simplification of {@link PersistentIdListModelExt} when the Selector class is
 * an {@link Object}. If you want to segment the data override the method
 * {@link PersistentIdListModelExt#getSelector(IBlockData)}.
 *
 * @author Jon Boley
 *
 */
public abstract class PersistentIdListModel<T extends IBlockData & IPersistentId>
		extends PersistentIdListModelExt<T, Object> {

	private final Map<Long, T> f_elementMap = new HashMap<Long, T>();

	public PersistentIdListModel(Class<T> p_class) {
		super(p_class, new BlockProvider<T>(p_class));
		BlockModel.setFromIdModel(p_class, this);
	}

	public PersistentIdListModel(Class<T> p_class, BlockProvider<T> p_provider) {
		super(p_class, p_provider);
	}

	@Override
	public T getElement(long p_id) {
		return getElements(Arrays.asList(p_id)).get(0);
	}

	private void loadCheck() {
		if (f_elementMap.isEmpty()) {
			loadModel("");
		}
	}

	public List<T> getElements(List<Long> p_ids) {
		loadCheck();
		final List<T> l_retVal = new ArrayList<T>();
		for (final long l_id : p_ids) {
			l_retVal.add(f_elementMap.get(l_id));
		}
		return l_retVal;
	}

	public Collection<T> getElements() {
		loadCheck();
		return f_elementMap.values();
	}

	public Map<Long, T> getElementData() {
		loadCheck();
		return f_elementMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.ListModel#getContentAsList(java.lang.Object)
	 */
	@Override
	public List<T> getContentAsList(Object p_selector) {
		return new ArrayList<T>(getElements());
	}

	public List<Long> missing(List<Long> p_ids) {
		loadCheck();
		final List<Long> l_retVal = new ArrayList<Long>();
		for (final long l_id : p_ids) {
			if (!f_elementMap.containsKey(l_id) && !l_retVal.contains(l_id)) {
				l_retVal.add(l_id);
			}
		}
		return l_retVal;
	}

	@Override
	public Set<T> addModelData(Set<T> p_addData) {
		for (final T l_data : p_addData) {
			f_elementMap.put(l_data.getPersistentID(), l_data);
		}
		return super.addModelData(p_addData);
	}

	@Override
	public void remModelData(Set<T> p_remData) {
		loadCheck();
		for (final T l_data : p_remData) {
			f_elementMap.remove(l_data.getPersistentID());
		}
		super.remModelData(p_remData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#copyData(com.airsltd.core.data.
	 * IBlockData, com.airsltd.core.data.IBlockData)
	 */
	/**
	 * Update the model indexed map to point at the new data if the ID has
	 * changed.
	 * <p>
	 * Currently, if the id is changed to that of another object, then the old
	 * object is removed and the other object is over written.
	 */
	@Override
	public void copyData(T p_oldData, T p_newData) {
		loadCheck();
		boolean f_update = false;
		if (p_oldData.getPersistentID() != p_newData.getPersistentID()) {
			f_elementMap.remove(p_oldData.getPersistentID());
			f_update = true;
		}
		super.copyData(p_oldData, p_newData);
		if (f_update) {
			f_elementMap.put(p_oldData.getPersistentID(), p_oldData);
		}
	}

	/**
	 * Clear the Id -> T elemement map. Useful for refresh and debugging
	 * purposes.
	 */
	public void clearElements() {
		f_elementMap.clear();
	}

}
