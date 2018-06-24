/**
 *
 */
package com.airsltd.core.data;

/**
 * Return a 4 valued parameterized object.
 *
 * @author Jon Boley
 * @see Tuple
 */
public class Tuple4<T, U, V, W> {

	private static final int HASHSEEDFIRST = 0xF32A8741;
	private static final int HASHSEEDSECOND = 0x04FF329A;
	private static final int HASHSEEDTHIRD = 0xBCDA0003;
	private static final int HASHSEEDFOURTH = 0x3451ADDE;
	private static final int HASHPRIME = 31;

	private T f_primary;
	private U f_secondary;
	private V f_tertiary;
	private W f_quadinary;

	public Tuple4() {
		// default constructor
	}

	public Tuple4(T p_primary, U p_secondary, V p_tertiary, W p_quadinary) {
		super();
		f_primary = p_primary;
		f_secondary = p_secondary;
		f_tertiary = p_tertiary;
		f_quadinary = p_quadinary;
	}

	/**
	 * @return the primary
	 */
	public T getPrimary() {
		return f_primary;
	}

	/**
	 * @param p_primary
	 *            the primary to set
	 */
	public void setPrimary(T p_primary) {
		f_primary = p_primary;
	}

	/**
	 * @return the secondary
	 */
	public U getSecondary() {
		return f_secondary;
	}

	/**
	 * @param p_secondary
	 *            the secondary to set
	 */
	public void setSecondary(U p_secondary) {
		f_secondary = p_secondary;
	}

	/**
	 * @return the tertiary
	 */
	public V getTertiary() {
		return f_tertiary;
	}

	/**
	 * @param p_tertiary
	 *            the tertiary to set
	 */
	public void setTertiary(V p_tertiary) {
		f_tertiary = p_tertiary;
	}

	/**
	 * @return the quadinary
	 */
	public W getQuadinary() {
		return f_quadinary;
	}

	/**
	 * @param p_quadinary
	 *            the quadinary to set
	 */
	public void setQuadinary(W p_quadinary) {
		f_quadinary = p_quadinary;
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
		result = prime * result + (f_quadinary == null ? HASHSEEDFOURTH : f_quadinary.hashCode());
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
		} else if (obj instanceof Tuple4) {
			final Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
			l_retVal = BlockData.objectCompare(f_primary, other.getPrimary());
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_secondary, other.getSecondary());
			}
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_tertiary, other.getTertiary());
			}
			if (l_retVal) {
				l_retVal = BlockData.objectCompare(f_quadinary, other.getQuadinary());
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
		return "Tuple4 [f_primary=" + f_primary + ", f_secondary=" + f_secondary + ", f_tertiary=" + f_tertiary
				+ ", f_quadinary=" + f_quadinary + "]";
	}

}
