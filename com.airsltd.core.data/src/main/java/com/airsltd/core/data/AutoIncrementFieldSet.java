/**
 *
 */
package com.airsltd.core.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jon_000
 *
 */
public class AutoIncrementFieldSet {

	private final Map<Long, Integer> f_hashes = new HashMap<Long, Integer>();

	/**
	 * @return the hashes
	 */
	public Map<Long, Integer> getHashes() {
		return f_hashes;
	}

}
