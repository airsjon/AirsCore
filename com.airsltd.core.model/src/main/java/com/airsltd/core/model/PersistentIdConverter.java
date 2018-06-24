/**
 *
 */
package com.airsltd.core.model;

import java.text.ParseException;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.IPersistentId;

/**
 * A converter for data that is persisted by Id (long) in a Model of {@link PersistentIdListModel}.
 *
 * @param T  the data being converted
 * @author Jon Boley
 *
 */
public class PersistentIdConverter<T extends IBlockData & IPersistentId> implements IDatabaseConverter<IBlockData, T> {

	/**
	 * the Class of the Object T being converted
	 */
	private final Class<T> f_class;
	
	/**
	 * the constructor.
	 * <p>
	 * The Class of the object being converted needs to be passed.
	 * p_class is used by the converter to find the model for loading/storing data.
	 * 
	 * @param p_class  not null, the Class of the Objects being converted.
	 */
	public PersistentIdConverter(Class<T> p_class) {
		f_class = p_class;
	}

	/**
	 * Helper function to create a converter for any Class that implements IPersistentId and IBlockData.
	 * 
	 * @param p_class  not null, the class of the object that will be converted.
	 * @return a new instance of this converter for an object of class p_class.
	 */
	public static <T extends IBlockData & IPersistentId> PersistentIdConverter<T> fromClass(Class<T> p_class) {
		return new PersistentIdConverter<T>(p_class);
	}

	/**
	 * Convert p_data to a String representation of the ID.
	 */
	@Override
	public String toSql(IBlockData p_parent, T p_data) {
		return p_data == null ? "0" : p_data.getPersistentID() + "";
	}

	/**
	 * Convert an id (long value) stored as an string into an object of class T.
	 * <p>
	 * The Model that is returned by {@link #getModel()} is used to find the object.
	 * 
	 * @return the object that is mapped by the value p_string.
	 */
	@Override
	public T fromSql(IBlockData p_parent, String p_string) throws ParseException {
		return "0".equals(p_string) ? null : getModel().getElement(Long.parseLong(p_string));
	}

	/**
	 * Get the model for this converter.
	 * <p>
	 * The model is loaded from the Static function {@link BlockModel#getFromIdModel(Class)}
	 * @return the model that will convert a long value into an object T
	 */
	private IFromIdModel<T> getModel() {
		return BlockModel.getFromIdModel(f_class);
	}

}
