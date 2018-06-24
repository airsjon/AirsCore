/**
 *
 */
package com.airsltd.core.data;

/**
 * Create a Tuple of three values.
 * <p>
 *
 * @author Jon Boley
 * @see Tuple
 */
public class Tuple3<S, T, U> {

	private static final int HASHSEEDFIRST = 0xF32A8741;
	private static final int HASHSEEDSECOND = 0x04FF329A;
	private static final int HASHSEEDTHIRD = 0xBCDA0003;
	private static final int HASHPRIME = 31;

	private S f_primary;
	private T f_secondary;
	private U f_tertiary;

	public Tuple3(S p_primary, T p_secondary, U p_tertiary) {
		f_primary = p_primary;
		f_secondary = p_secondary;
		f_tertiary = p_tertiary;
	}

	public Tuple3() {
		// default constructor
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

	/**
	 * @return the tertiary
	 */
	public U getTertiary() {
		return f_tertiary;
	}

	/**
	 * @param p_tertiary
	 *            the tertiary to set
	 */
	public void setTertiary(U p_tertiary) {
		f_tertiary = p_tertiary;
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
		result = prime * result + (f_tertiary == null ? HASHSEEDTHIRD : f_tertiary.hashCode());
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
		} else if (obj instanceof Tuple3) {
			final Tuple3<?, ?, ?> other = (Tuple3<?, ?, ?>) obj;
			l_retVal = BlockData.objectCompare(f_primary, other.getPrimary());
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_secondary, other.getSecondary());
			}
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_tertiary, other.getTertiary());
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tuple3 [f_primary=" + f_primary + ", f_secondary=" + f_secondary + ", f_tertiary=" + f_tertiary + "]";
	}

}
