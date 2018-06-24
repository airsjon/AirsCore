/**
 *
 */
package com.airsltd.core.data;

import java.text.ParseException;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IBlockField;

/**
 * A closure around a particular data that provides access to the data stored in
 * field f_factory.
 *
 * @author Jon Boley
 *
 */
public class BlockField<U, T extends IBlockData> implements IBlockField<U> {

	private static final int HASHPRIME = 31;
	private final T f_data;
	private final BlockFieldFactory<T, U> f_factory;

	public BlockField(T p_data, BlockFieldFactory<T, U> p_factory) {
		super();
		f_data = p_data;
		f_factory = p_factory;
	}

	@Override
	public final U getValue() {
		return f_factory.value(f_data);
	}

	@Override
	public final void setValue(U f_newData) {
		f_factory.modifyValue(f_data, f_newData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockField#toSqlValue()
	 */
	@Override
	public String toSqlValue() {
		return f_factory.getConverter().toSql(f_data, getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromSqlValue(String string) throws ParseException {
		f_factory.modifyValue(f_data, (U) f_factory.getConverter().fromSql(f_data, string));
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
		result = prime * result + (f_data == null ? 0 : f_data.tableName().hashCode());
		result = prime * result + f_factory.getField();
		final U l_data = getValue();
		result = prime * result + (l_data == null ? 0 : l_data.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals(Object obj) {
		boolean l_retVal = false;
		if (this == obj) {
			l_retVal = true;
		} else {
			if (obj != null && getClass() == obj.getClass()) {
				final BlockField other = (BlockField) obj;
				if (f_data == null) {
					l_retVal = other.f_data == null;
				} else {
					l_retVal = sameField(other) && sameData(other);
				}
			}
		}
		return l_retVal;
	}

	protected boolean sameData(BlockField<U, T> p_other) {
		final U l_data = getValue();
		final U l_otherData = p_other.getValue();
		return l_data == null ? l_otherData == null : l_data.equals(l_otherData);
	}

	/**
	 * This is never called with f_data being null
	 *
	 * @param p_other
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected boolean sameField(BlockField p_other) {
		return p_other.f_data != null && f_data.tableName().equals(p_other.f_data.tableName())
				&& f_factory.getField() == p_other.f_factory.getField();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object p_o) {
		final U l_first = getValue();
		final U l_second = ((IBlockField<U>) p_o).getValue();
		int l_retVal = 0;
		if (l_first == null) {
			l_retVal = l_second == null ? 0 : -1;
		} else if (l_second == null) {
			l_retVal = 1;
		} else {
			final Class<? extends Object> l_class = l_first.getClass();
			final boolean l_b = Comparable.class.isAssignableFrom(l_class);
			l_retVal = l_b ? ((Comparable<U>) l_first).compareTo(l_second) : 0;
		}
		return l_retVal;
	}

}
