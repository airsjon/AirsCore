/**
 *
 */
package com.airsltd.core.data;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IBlockField;
import com.airsltd.core.data.IBlockFieldFactory;

/**
 * @author Jon Boley
 *
 */
public abstract class BlockFieldFactory<T extends IBlockData, U> implements IBlockFieldFactory {

	private int f_field;
	private IDatabaseConverter<? super T, ? super U> f_converter;

	public BlockFieldFactory(int p_field, IDatabaseConverter<? super T, ? super U> p_converter) {
		super();
		f_field = p_field;
		f_converter = p_converter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IBlockFieldFactory#field(com.airsltd.core.data.
	 * IBlockData)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IBlockField<U> field(IBlockData p_blockData) {
		return new BlockField<U, T>((T) p_blockData, this);
	}

	public abstract U value(T p_blockData);

	public abstract void modifyValue(T p_blockData, U p_newValue);

	/**
	 * @return the field
	 */
	public int getField() {
		return f_field;
	}

	/**
	 * @param p_field
	 *            the field to set
	 */
	public void setField(int p_field) {
		f_field = p_field;
	}

	/**
	 * @return the converter
	 */
	public IDatabaseConverter<? super T, ? super U> getConverter() {
		return f_converter;
	}

	/**
	 * @param p_converter
	 *            the converter to set
	 */
	public void setConverter(IDatabaseConverter<? super T, ? super U> p_converter) {
		f_converter = p_converter;
	}

}
