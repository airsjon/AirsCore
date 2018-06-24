/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.IBlockData;

/**
 * This extension of {@link SegmentedListModelExt} allows for the use of data
 * types that implement {@link ISegment} to define the segmentation of the data.
 *
 * @author Jon Boley
 *
 */
public class SegmentedListModel<T extends IBlockData & ISegment<V>, V> extends SegmentedListModelExt<T, V> {

	/**
	 * Construct a Segmented list using the generic {@link BlockProvider} for
	 * Class <Code>T</Code>
	 * 
	 * @param p_class
	 */
	public SegmentedListModel(Class<T> p_class) {
		super(p_class);
	}

	public SegmentedListModel(BlockProvider<T> p_provider) {
		super(p_provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.model.SegmentedListModelExt#getSegment(com.airsltd.core.
	 * data.IBlockData)
	 */
	@Override
	protected V getSegment(T p_data) {
		return p_data.toSegment();
	}

}
