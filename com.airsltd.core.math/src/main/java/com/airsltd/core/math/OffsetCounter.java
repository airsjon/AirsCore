/**
 *
 */
package com.airsltd.core.math;

/**
 * Provide a counter that will span a range n with m values such that r * (s+1)
 * + (m-r) * s = n and r <= m
 * <p>
 *
 * @author Jon Boley
 *
 */
public class OffsetCounter {

	private long f_start;
	private int f_count;
	private int f_index;

	/**
	 * Generate a counter that will cover a span of size p_span with the
	 * p_groups values (s+1), (s+1), s, s, ...
	 * 
	 * @param p_span
	 *            >0
	 * @param p_groups
	 *            >0
	 */
	public OffsetCounter(long p_start, int p_span, int p_groups) {
		super();
		f_start = p_start;
		f_index = p_span / p_groups;
		f_count = p_span % p_groups;
		/*
		 * Increment f_index to make room for the actual node itself. When #next
		 * is called the user will get the next value for that node.
		 */
		f_index++;
		if (f_count > 0) {
			f_index++;
		}
	}

	/**
	 * Add the current span to the counter and return the new value.
	 *
	 * @return
	 */
	public long next() {
		f_start += f_index;
		if (f_count != 0) {
			f_count--;
			if (f_count == 0) {
				f_index--;
			}
		}
		return f_start;
	}

	/**
	 * Increment the counter and return the new value.
	 * 
	 * @return
	 */
	public long inc() {
		return ++f_start;
	}

	public long getCurrentValue() {
		return f_start;
	}

}
