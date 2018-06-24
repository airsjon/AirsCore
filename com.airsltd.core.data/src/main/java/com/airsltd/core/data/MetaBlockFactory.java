/**
 *
 */
package com.airsltd.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.annotations.AirsPersistentField;
import com.airsltd.core.data.converters.DatabaseConverter;

/**
 * Field access for a Persited data field.
 *
 * @author Jon Boley
 *
 */
public class MetaBlockFactory<T extends PersistedData, U> extends BlockFieldFactory<T, U> {

	private static final String DATA_ERROR = "Data error";
	private static final int FIELDOFFSET = 2;
	private final Field f_field;
	private Method f_getter;
	private Method f_setter;

	public MetaBlockFactory(int p_fieldIndex, Class<T> p_class, Class<U> p_fieldClass, Field p_field,
			IDatabaseConverter<T, U> p_converter, AirsPersistentField p_pField) {
		super(p_fieldIndex, findConverter(p_converter, p_class, p_fieldClass, p_pField));
		f_field = p_field;
		f_field.setAccessible(true);
		loadSetterGetter(p_class, p_field, p_pField);
	}

	private void loadSetterGetter(Class<T> p_class, Field p_field, AirsPersistentField p_pField) {
		try {
			final String f_getterName = p_pField.getter().isEmpty()
					? (isBoolean(p_field) ? "is" : "get") + normalize(p_field.getName()) : p_pField.getter();
			f_getter = p_class.getMethod(f_getterName);
			if (!p_pField.calculated()) {
				final String f_setterName = p_pField.setter().isEmpty() ? "set" + normalize(p_field.getName())
						: p_pField.setter();
				f_setter = p_class.getMethod(f_setterName, p_field.getType());
			}
		} catch (NoSuchMethodException | SecurityException e) {
			CoreInterface.getSystem().handleException(DATA_ERROR, e, NotificationStatus.BLOCK);
		}
	}

	private boolean isBoolean(Field p_field) {
		return p_field.getType().equals(boolean.class) || Boolean.class.isAssignableFrom(p_field.getType());
	}

	private String normalize(String p_name) {
		final String l_string = p_name.startsWith("f_") ? p_name.substring(FIELDOFFSET) : p_name;
		return l_string.substring(0, 1).toUpperCase() + l_string.substring(1);
	}

	private static <T extends PersistedData, U> IDatabaseConverter<? super T, ? super U> findConverter(
			IDatabaseConverter<T, U> p_converter, Class<T> p_class, Class<U> p_fieldClass,
			AirsPersistentField p_pField) {
		return p_converter == null ? DatabaseConverter.findConverter(p_class, p_fieldClass, p_pField) : p_converter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public U value(T p_blockData) {
		Object l_retVal = null;
		try {
			l_retVal = f_getter.invoke(p_blockData);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			CoreInterface.getSystem().handleException(DATA_ERROR, e, NotificationStatus.BLOCK);
		}
		return (U) l_retVal;
	}

	@Override
	public void modifyValue(T p_blockData, U p_newValue) {
		try {
			// a null setter means that the field is calculated.
			if (f_setter != null) {
				f_setter.invoke(p_blockData, p_newValue);
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			CoreInterface.getSystem().handleException(DATA_ERROR, e, NotificationStatus.BLOCK);
		}
	}

}
