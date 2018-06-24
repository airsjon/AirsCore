/**
 *
 */
package com.airsltd.core.data;

import java.io.Serializable;

/**
 * Simple means of returning two parameterized values.
 * <p>
 * Implementation note: current implementation allows two Tuples of different
 * parameters to be equal when the values themselves are equal. So a
 * Tuple<Object, Object> of 3 and 4 could be equal to a Tuple<Integer, Integer>
 * of 3 and 4.
 *
 * @author Jon Boley
 *
 */
public class Tuple<S, T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8151328938476780847L;
	private static final int HASHSEEDFIRST = 0x9FAA0234;
	private static final int HASHSEEDSECOND = 0xD0032221;
	private static final int HASHPRIME = 31;
	private S f_primary;
	private T f_secondary;

	public Tuple() {
		// default constructor
	}

	public Tuple(S p_primary, T p_secondary) {
		f_primary = p_primary;
		f_secondary = p_secondary;
	}

	public S getPrimary() {
		return f_primary;
	}

	public void setPrimary(S p_primary) {
		f_primary = p_primary;
	}

	public T getSecondary() {
		return f_secondary;
	}

	public void setSecondary(T p_secondary) {
		f_secondary = p_secondary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tuple [f_primary=" + f_primary + ", f_secondary=" + f_secondary + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = HASHPRIME;
		int result = 1;
		result = prime * result + (f_primary == null ? HASHSEEDFIRST : f_primary.hashCode());
		result = prime * result + (f_secondary == null ? HASHSEEDSECOND : f_secondary.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean l_retVal = false;
		if (this == obj) {
			l_retVal = true;
		} else if (obj instanceof Tuple) {
			final Tuple<?, ?> other = (Tuple<?, ?>) obj;
			l_retVal = BlockData.objectCompare(f_primary, other.f_primary);
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_secondary, other.f_secondary);
			}
		}
		return l_retVal;
	}

}
