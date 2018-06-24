/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.math;

/**
 * @author Jon Boley
 *
 */
public class Counter<T extends Enum<T>> {

	private final int[] f_counts;
	private final Class<T> f_class;

	public Counter(Class<T> p_class) {
		f_counts = new int[p_class.getEnumConstants().length];
		f_class = p_class;
	}

	public static <T extends Enum<T>> Counter<T> fromClass(Class<T> p_class) {
		return new Counter<T>(p_class);
	}

	public void count(T p_enum) {
		f_counts[p_enum.ordinal()]++;
	}

	public int getCount(T p_enum) {
		return f_counts[p_enum.ordinal()]++;
	}

	@Override
	public String toString() {
		final String l_newLine = System.getProperty("line.separator");
		final StringBuilder l_builder = new StringBuilder();
		l_builder.append("[");
		l_builder.append("Counter ");
		boolean l_first = true;
		for (final T l_enum : f_class.getEnumConstants()) {
			if (l_first) {
				l_first = false;
			} else {
				l_builder.append(",");
				l_builder.append(l_newLine);
				l_builder.append("  ");
			}
			l_builder.append(l_enum);
			l_builder.append(":");
			l_builder.append(getCount(l_enum));
		}
		l_builder.append("]");
		return l_builder.toString();
	}
}
