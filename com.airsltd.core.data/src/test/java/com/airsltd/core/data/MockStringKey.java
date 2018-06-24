/**
 * 
 */
package com.airsltd.core.data;

/**
 * @author Jon Boley
 *
 */
public class MockStringKey implements Comparable<MockStringKey>, IStringKey {

	private String f_key;
	
	public MockStringKey(String p_key) {
		super();
		f_key = p_key;
	}

	@Override
	public String toKey() {
		return f_key;
	}

	@Override
	public int compareTo(MockStringKey p_o) {
		return toKey().compareTo(p_o.toKey());
	}
	
	@Override
	public String toString() {
		return f_key;
	}

}
