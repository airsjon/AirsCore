/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IPersistentId;

/**
 * Linked model stores data associated with an IPersistentId that is stored in another model.
 * <p>
 * Linked model is useful for tables that are extending other tables.
 * A one-to-one or one-to-oneornone relationship is best modelled with a LinkedModel.
 * 
 * @param U extends IPersistentId, the data that this model is extending
 * @param T extends IBlockData, the data the model is storing
 * @param V any object, data used to aid the model in loading
 * 
 * @author Jon Boley
 *
 */
public abstract class LinkedModel<U extends IBlockData & IPersistentId, T extends IBlockData & ISegment<U>> extends BlockModel<T, U> 
		implements IFromIdModel<T> {

	/**
	 * A map between the object being extended and the object stored.
	 */
	private final Map<U, T> f_data = new HashMap<U, T>();
	private IFromIdModel<U> f_model;

	/**
	 * Simple constructor allowing for an override of the default data store provider.
	 * 
	 * @param p_class, new BlockProvider<>(p_class  not null, the provider that actually reads/writes data to the store.
	 */
	public LinkedModel(Class<U> p_linkClass, Class<T> p_class) {
		this(p_class, new BlockProvider<T>(p_class));
		f_model = BlockModel.getFromIdModel(p_linkClass);
	}

	private LinkedModel(Class<T> p_class, BlockProvider<T> p_blockProvider) {
		super (p_blockProvider);
		BlockModel.setFromIdModel(p_class, this);
	}

	@Override
	public Set<T> addModelData(Set<T> p_addData) {
		for (final T l_toAdd : p_addData) {
			f_data.put(l_toAdd.toSegment(), l_toAdd);
		}
		return p_addData;
	}

	@Override
	public void remModelData(Set<T> p_remData) {
		for (final T l_toRem : p_remData) {
			f_data.remove(l_toRem.toSegment());
		}
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
	public List<T> getContentAsList(U p_selector) {
		if (f_data.isEmpty()) {
			loadModel(p_selector);
		}
		return Arrays.asList(f_data.get(p_selector));
	}

	public Map<U, T> getData() {
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

	protected T loadContentFromMap(Map<U, T> p_map, T p_data, boolean p_update) {
		T l_retVal = p_map.get(p_data.toSegment());
		if (l_retVal == null) {
			l_retVal = p_data;
		} else if (p_update && l_retVal != p_data) {
			p_data.copy(l_retVal);
			l_retVal = null;
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#clearData(java.lang.Object)
	 */
	@Override
	protected void clearData(U p_element) {
		if (f_data.containsKey(p_element)) {
			remUIData(Arrays.asList(f_data.get(p_element)));
			f_data.remove(p_element);
			super.clearData(p_element);
		}
	}

	@Override
	public T getElement(long p_id) {
		U l_data = f_model.getElement(p_id);
		return l_data==null?null:getElement(l_data);
	}
	
	/**
	 * Return the object associated with p_element.
	 * <br>
	 * If this object does not exist, then create it and add it to the store.
	 * 
	 * @param p_element  not null, the RankGame to extend
	 * @return the extension for RankGame
	 */
	public T getElement(U p_element) {
		T l_retVal = f_data.get(p_element);
		if (l_retVal == null) {
			l_retVal = extendData(p_element);
			f_data.put(p_element, l_retVal);
		} else if (!f_data.containsKey(p_element)) {
			l_retVal = modifyData(l_retVal);
		}
		return l_retVal;
	}

	/**
	 * When the data has been found, a copy for modification needs to be returned.
	 * 
	 * @param p_element
	 * @return
	 */
	protected abstract T modifyData(T p_element);

	/**
	 * When the data has not been found, it needs to be extended.
	 * 
	 * @param p_element
	 * @return
	 */
	protected abstract T extendData(U p_element);

}
