/**
 *
 */
package com.airsltd.core.model;

/**
 * Objects that implement this interface can be segmented by a second object of type <T>.
 * <p>
 * Segmented Data models are effective for many-to-one relations.
 * This interface can also be of value in one-to-one {@link RelationService#}
 * <p>
 * Data that can be segmented can be used in {@link PersistentIdSegmentedListModel},
 * {@link SegmentedListModel}, and {@link SegmentedListModelExt}.
 *
 * @author Jon Boley
 *
 */
public interface ISegment<T> {
	/**
	 * Get the segment that this object belongs in.
	 * 
	 * @return T
	 */
	T toSegment();
}
