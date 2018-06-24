/**
 *
 */
package com.airsltd.core.data;

/**
 * This class provides a simple store for the original and new values to be
 * updated.
 *
 * @author Jon Boley
 *
 */

public class BlockMod<T> {

	private final T f_oldValue;
	private final T f_newValue;

	public BlockMod(T p_oldValue, T p_newValue) {
		super();
		this.f_oldValue = p_oldValue;
		this.f_newValue = p_newValue;
	}

	/**
	 * @return the oldValue
	 */
	public T getOldValue() {
		return f_oldValue;
	}

	/**
	 * @return the newValue
	 */
	public T getNewValue() {
		return f_newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_obj) {
		boolean l_retVal = false;
		if (p_obj instanceof BlockMod) {
			final BlockMod<?> l_bm = (BlockMod<?>) p_obj;
			l_retVal = BlockData.objectCompare(f_oldValue, l_bm.f_oldValue)
					&& BlockData.objectCompare(f_newValue, l_bm.f_newValue);
		}
		return l_retVal;
	}

	@Override
	public int hashCode() {
		return f_oldValue == null ? 0 : f_oldValue.hashCode();
	}

}
