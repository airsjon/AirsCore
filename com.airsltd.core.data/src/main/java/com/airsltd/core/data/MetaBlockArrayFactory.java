/**
 *
 */
package com.airsltd.core.data;

import java.lang.reflect.Field;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.annotations.AirsPersistentField;
import com.airsltd.core.data.converters.DatabaseConverter;

/**
 * @author Jon Boley
 *
 */
public class MetaBlockArrayFactory<T extends PersistedData, U> extends BlockFieldFactory<T, U> {

	private final Field f_fieldData;
	private final int f_arrayIndex;

	public MetaBlockArrayFactory(int p_fieldIndex, int p_arrayIndex, Class<T> p_class, Class<U> p_fieldClass,
			Field p_field, IDatabaseConverter<T, U> p_converter, AirsPersistentField p_pField) {
		super(p_fieldIndex, findConverter(p_converter, p_class, p_fieldClass, p_pField));
		f_arrayIndex = p_arrayIndex;
		f_fieldData = p_field;
		f_fieldData.setAccessible(true);
	}

	private static <T extends PersistedData, U> IDatabaseConverter<? super T, ? super U> findConverter(
			IDatabaseConverter<T, U> p_converter, Class<T> p_class, Class<U> p_fieldClass,
			AirsPersistentField p_pField) {
		return p_converter == null ? DatabaseConverter.findConverter(p_class, p_fieldClass, p_pField) : p_converter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public U value(T p_blockData) {
		U l_retVal = null;
		try {
			l_retVal = ((U[]) f_fieldData.get(p_blockData))[f_arrayIndex];
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CoreInterface.getSystem().handleException("Data error", e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modifyValue(T p_blockData, U p_newValue) {
		try {
			((U[]) f_fieldData.get(p_blockData))[f_arrayIndex] = p_newValue;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CoreInterface.getSystem().handleException("Data error", e, NotificationStatus.BLOCK);
		}
	}

}
