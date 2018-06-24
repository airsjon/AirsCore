/**
 *
 */
package com.airsltd.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * BlockHistory saves all the data modifications that are to be done to a
 * BlockProvider so that any secondary data modifications can be done after this
 * history has updated properly
 *
 * @author Jon
 *
 */
public class BlockHistory<T extends IBlockData> {
	protected List<T> f_adds;
	protected List<T> f_removes;
	protected List<BlockMod<T>> f_updates;

	public BlockHistory(BlockProvider<T> p_provider) {
		Set<T> l_toCopy = p_provider.getBlockData();
		f_adds = new ArrayList<T>(l_toCopy.size());
		f_adds.addAll(l_toCopy);
		l_toCopy = p_provider.getBlockRemoveData();
		f_removes = new ArrayList<T>(l_toCopy.size());
		f_removes.addAll(l_toCopy);
		final Set<BlockMod<T>> l_updateCopy = p_provider.getBlockUpdateData();
		f_updates = new ArrayList<BlockMod<T>>(l_updateCopy.size());
		f_updates.addAll(l_updateCopy);
	}

	/**
	 * @return the adds
	 */
	public List<T> getAdds() {
		return f_adds;
	}

	/**
	 * @return the removes
	 */
	public List<T> getRemoves() {
		return f_removes;
	}

	/**
	 * @return the updates
	 */
	public List<BlockMod<T>> getUpdates() {
		return f_updates;
	}

	public List<T> getUpdatesOriginals() {
		final List<T> l_retVal = new ArrayList<T>();
		for (final BlockMod<T> l_bm : f_updates) {
			l_retVal.add(l_bm.getOldValue());
		}
		return l_retVal;
	}

}
