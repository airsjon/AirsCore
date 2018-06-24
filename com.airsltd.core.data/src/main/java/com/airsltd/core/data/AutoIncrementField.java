/**
 *
 */
package com.airsltd.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Data field that tracks an auto increment id.
 *
 * @author Jon Boley
 *
 */
public class AutoIncrementField implements Comparable<AutoIncrementField> {

	private static final Random RANDOM = new Random();

	public static final long RECORDED = -2;

	public static final long TORECORD = -1;

	private static Map<Class<?>, AutoIncrementFieldSet> s_set = new HashMap<Class<?>, AutoIncrementFieldSet>();

	private long f_id;
	private int f_hash;
	private final AutoIncrementFieldSet f_set;

	public AutoIncrementField(Class<?> p_class) {
		f_id = -1;
		f_hash = RANDOM.nextInt();
		f_set = getSet(p_class);
	}

	public AutoIncrementField(Class<?> p_class, long p_id) {
		f_id = p_id;
		f_set = getSet(p_class);
		if (p_id < 0) {
			f_hash = RANDOM.nextInt();
		} else {
			if (!f_set.getHashes().containsKey(p_id)) {
				f_hash = RANDOM.nextInt();
				f_set.getHashes().put(p_id, f_hash);
			} else {
				f_hash = f_set.getHashes().get(p_id);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_obj) {
		boolean l_retVal = this == p_obj;
		if (!l_retVal && p_obj instanceof AutoIncrementField) {
			final AutoIncrementField l_otherId = (AutoIncrementField) p_obj;
			l_retVal = f_id >= 0 && l_otherId.getId() >= 0 && f_id == l_otherId.getId();
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return f_hash;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return f_id;
	}

	/**
	 * @param p_id
	 *            the id to set
	 */
	public void setId(long p_id) {
		if (f_id == -1) {
			f_set.getHashes().put(p_id, f_hash);
		} else {
			if (f_set.getHashes().containsKey(p_id)) {
				f_hash = f_set.getHashes().get(p_id);
			} else {
				f_hash = RANDOM.nextInt();
				if (p_id != -1) {
					f_set.getHashes().put(p_id, f_hash);
				}
			}
		}
		f_id = p_id;
	}

	/**
	 * @param p_hash
	 *            the hash to set
	 */
	public void setHash(int p_hash) {
		f_hash = p_hash;
	}

	@Override
	public int compareTo(AutoIncrementField p_gameId) {
		return f_id == -1 && p_gameId.getId() == -1 ? Integer.compare(f_hash, p_gameId.hashCode())
				: Long.compare(f_id, p_gameId.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Long.toString(f_id);
	}

	public static AutoIncrementFieldSet getSet(Class<?> p_class) {
		AutoIncrementFieldSet l_retVal = s_set.get(p_class);
		if (l_retVal == null) {
			l_retVal = new AutoIncrementFieldSet();
			s_set.put(p_class, l_retVal);
		}
		return l_retVal;
	}

	/**
	 * Used to clear all sets.
	 * <p>
	 * Primarily used by unit tests to return system to known state.
	 */
	public static void clear() {
		s_set.clear();
	}

}
