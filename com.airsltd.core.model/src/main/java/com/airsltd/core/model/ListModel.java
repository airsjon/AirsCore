/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;

/**
 * A List model tracks a list of data with a BlockProvider backing store.
 * Standard implementation ignores the Input data and tracks the complete data
 * as a list.
 * 
 * @author Jon Boley
 *
 */
public class ListModel<T extends IBlockData, V> extends BlockModel<T, V> {

	private final Set<T> f_data = new HashSet<T>();

	public ListModel(BlockProvider<T> p_provider) {
		super(p_provider);
	}

	@Override
	public Set<T> addModelData(Set<T> p_addData) {
		f_data.addAll(p_addData);
		return p_addData;
	}

	@Override
	public void remModelData(Set<T> p_remData) {
		f_data.removeAll(p_remData);
	}

	/**
	 * This implementation creates a copy of the container. So modification will
	 * not change the actual model.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#getContentAsList(java.lang.Object)
	 */
	@Override
	public List<T> getContentAsList(V p_selector) {
		if (f_data.isEmpty()) {
			loadModel(p_selector);
		}
		return new ArrayList<T>(f_data);
	}

	public Set<T> getData() {
		return f_data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.model.PersistentIdListModelExt#loadContent(com.airsltd.
	 * core.data.IBlockData, boolean)
	 */
	@Override
	public T loadContent(T p_data, boolean p_update) {
		return loadContentFromMap(f_data, p_data, p_update);
	}

	protected T loadContentFromMap(Set<T> p_map, T p_data, boolean p_update) {
		T l_retVal = p_map.contains(p_data)?associate(p_map, p_data):null;
		if (l_retVal == null) {
			l_retVal = p_data;
		} else if (p_update && l_retVal != p_data) {
			p_data.copy(l_retVal);
			l_retVal = null;
		}
		return l_retVal;
	}

	private T associate(Set<T> p_map, T p_data) {
		T l_retVal = null;
		for (T l_element : p_map) {
			if (l_element.equals(p_data)) {
				l_retVal = l_element;
				break;
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#clearData(java.lang.Object)
	 */
	@Override
	protected void clearData(V p_qualifier) {
		remUIData(f_data);
		f_data.clear();
		super.clearData(p_qualifier);
	}

}
