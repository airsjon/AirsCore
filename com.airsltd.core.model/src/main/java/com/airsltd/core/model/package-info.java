/**
 * Implementation of the Model component.
 * <p>
 * Interfaces defined.
 * <table>
 * <tr><td>{@link com.airsltd.core.model.ITreeParent ITreeParent}</td><td>An object that can be a parent in the {@link TreeModel}</td></tr>
 * <tr><td>{@link com.airsltd.core.model.ITreeChild ITreeChild}</td><td>An object that can be a child in the {@link TreeModel}</td></tr>
 * <tr><td>{@link com.airsltd.core.model.IFromIdModel IFromIdModel}</td><td>A model that can map long to the data in the model</td></tr>
 * <tr><td>{@link com.airsltd.core.model.ISegment ISegment}</td><td>Data that can implement this interface can be segmented</td></tr>
 * <tr><td>{@link com.airsltd.core.model.ISegmentedListModel ISegmentedListModel}</td><td>Models that implement this interface can store segmented data</td></tr>
 * </table>
 * Models defined.
 * <table>
 * <tr><td>{@link com.airsltd.core.model.BlockModel BlockModel}</td><td>Base model that provides many common methods</td></tr>
 * <tr><td>{@link com.airsltd.core.model.ListModel ListModel}</td><td>Model where all the data is in a list</td></tr>
 * <tr><td>{@link com.airsltd.core.model.NamedListModel NamedListModel}</td><td>Model where all the data is in a associative list</td></tr>
 * 
 * </table>
 * 
 * Copyright (c) 2013-2018 Jon Boley
 * 
 */
package com.airsltd.core.model;