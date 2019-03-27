/**
 *
 */
package com.airsltd.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;

/**
 * <p>
 * The {@link SegmentedListModel} provides a model which contains a list of data
 * <code>T</code> associated with a particular object of type <code>V</code>. In
 * addition, the Model will have a current segment 'loaded'. This allows for
 * easy access to the current segment
 * {@link SegmentedListModel#getContentAsList()}
 * </p>
 * <p>
 * @param T extends IBlinkData, the type of data stored in this model.
 * @param V is the data that the model is segmented on.
 *
 * @author Jon Boley
 *
 */
public abstract class SegmentedListModelExt<T extends IBlockData, V> extends BlockModel<T, V>
		implements ISegmentedListModel<T, V> {

	private final Map<V, List<T>> f_data = new HashMap<V, List<T>>();
	/**
	 * When a database is completely loaded, we no longer need to load segments
	 * even if they have no data.
	 */
	private boolean f_completeLoad;

	/**
	 * Construct a Segmented list using the generic {@link BlockProvider} for
	 * Class <Code>T</Code>
	 * 
	 * @param p_class
	 */
	public SegmentedListModelExt(Class<T> p_class) {
		super(new BlockProvider<T>(p_class));
	}

	public SegmentedListModelExt(BlockProvider<T> p_provider) {
		super(p_provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#addModelData(java.util.List)
	 */
	@Override
	public Set<T> addModelData(Set<T> p_addData) {
		if (!p_addData.isEmpty()) {
			for (final T l_data : p_addData) {
				final V l_segment = getSegment(l_data);
				addSegmentData(l_segment, l_data);
			}
		}
		return p_addData;
	}

	private void addSegmentData(V p_segment, T p_data) {
		if (!f_data.containsKey(p_segment)) {
			f_data.put(p_segment, new ArrayList<T>());
		}
		f_data.get(p_segment).add(p_data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#remModelData(java.util.List)
	 */
	@Override
	public void remModelData(Set<T> p_remData) {
		if (!p_remData.isEmpty()) {
			for (final T l_data : p_remData) {
				final V l_segment = getSegment(l_data);
				remSegmentData(l_segment, l_data);
			}
		}
	}

	protected abstract V getSegment(T p_data);

	private void remSegmentData(V p_segment, T p_data) {
		if (f_data.containsKey(p_segment)) {
			f_data.get(p_segment).remove(p_data);
		}
	}

	/**
	 * Return all the segments that are included in p_data.
	 *
	 * @param p_data
	 *            not null, a list of data that we are scanning for segments
	 * @return a {@link List} of all the segments found in p_data.
	 */
	public List<V> allSegments(List<T> p_data) {
		final List<V> l_retVal = new ArrayList<V>();
		for (final T l_element : p_data) {
			final V l_segment = getSegment(l_element);
			if (!l_retVal.contains(l_segment)) {
				l_retVal.add(l_segment);
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#getContentAsList(java.lang.Object)
	 */
	@Override
	public List<T> getContentAsList(V p_selector) {
		checkSegment(p_selector);
		final List<T> l_retVal = f_data.get(p_selector);
		return l_retVal == null ? new ArrayList<T>() : l_retVal;
	}

	@Override
	public List<T> getContentAsList() {
		final List<T> l_retVal = new ArrayList<T>();
		for (final V l_key : f_data.keySet()) {
			l_retVal.addAll(f_data.get(l_key));
		}
		return l_retVal;
	}

	@Override
	public void loadSegment(V p_selection, List<T> p_data) {
		f_data.put(p_selection, p_data);
	}

	@Override
	public void checkSegment(V p_selection) {
		if (!f_data.containsKey(p_selection)) {
			if (f_completeLoad) {
				f_data.put(p_selection, new ArrayList<T>());
			} else {
				loadModel(p_selection);
			}
		}
	}

	/**
	 * @return the data
	 */
	@Override
	public Map<V, List<T>> getData() {
		return f_data;
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
			final List<T> l_content = getContentAsList(getSegment(p_data));
			final int l_index = l_content.indexOf(p_data);
			if (l_index != -1) {
				l_retVal = l_content.get(l_index);
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
		final List<T> curList = getData().get(p_qualifier);
		if (curList != null) {
			curList.clear();
		} else {
			getData().put(p_qualifier, new ArrayList<T>());
		}
	}

	/**
	 * When the model completely loads the database, return true.
	 * 
	 * @return true if the database is completely loads it's data.
	 */
	final public boolean isCompleteLoad() {
		return f_completeLoad;
	}
	
	/**
	 * Set the model to load complete database
	 * 
	 * @param p_completeLoad
	 */
	public void setCompleteLoad(boolean p_completeLoad) {
		f_completeLoad = p_completeLoad;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.model.BlockModel#loadModel(java.lang.Object)
	 */
	@Override
	public boolean loadModel(V p_qualifier) {
		final boolean l_loadModel = super.loadModel(p_qualifier);
		return l_loadModel;
	}

}
