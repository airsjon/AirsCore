/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.IPersistentId;
import com.airsltd.core.data.annotations.AirsPersistentField;

/**
 * @author Jon Boley
 *
 */
public abstract class DatabaseConverter<T extends IBlockData, U> implements IDatabaseConverter<T, U> {

	private static final Map<Class<?>, IDatabaseConverter<? extends IBlockData, ?>> SIMPLECONVERTERS = new HashMap<Class<?>, IDatabaseConverter<? extends IBlockData, ?>>();
	private static IIdConverter s_converter;

	static {
		new IntegerConverter();
		new FloatConverter();
		new DoubleConverter();
		new LongConverter();
		new TimestampConverter();
		new DateConverter();
		new AutoIncrementConverter();
		new BooleanConverter();
		new CharacterConverter();
		new PasswordConvert();
		new PropertyConverter();
		new URIConverter();

	}

	/**
	 * @return the converter
	 */
	public static IIdConverter getConverter() {
		return s_converter;
	}

	/**
	 * @param p_converter the converter to set
	 */
	public static void setConverter(IIdConverter p_converter) {
		s_converter = p_converter;
	}

	public DatabaseConverter() {
	}

	public DatabaseConverter(Class<?> p_class) {
		addConverter(p_class);
	}

	protected void addConverter(Class<?> p_class) {
		SIMPLECONVERTERS.put(p_class, this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IBlockData, U> IDatabaseConverter<T, U> findConverter(Class<T> p_parentClass,
			Class<U> p_childClass, AirsPersistentField p_pField) {
		IDatabaseConverter<T, U> l_retVal = null;
		if (p_childClass.equals(String.class)) {
			l_retVal = (IDatabaseConverter<T, U>) new StringSQLConvert(p_pField.size());
		} else if (p_childClass.isEnum()) {
			l_retVal = new EnumConverter(p_childClass);
		} else if (IPersistentId.class.isAssignableFrom(p_childClass)) {
			if (s_converter==null) {
				throw new NoIDConverterLoadedException();
			} else {
				l_retVal = (IDatabaseConverter<T, U>) s_converter.
					getConverter(p_childClass.asSubclass(IPersistentId.class));
			}
		} else if (p_childClass.isArray()) {
			final Class<?> l_componentType = p_childClass.getComponentType();
			l_retVal = (IDatabaseConverter<T, U>) ArrayConverter.getConverter(l_componentType);
		} else {
			l_retVal = (IDatabaseConverter<T, U>) SIMPLECONVERTERS.get(p_childClass);
		}
		if (l_retVal == null) {
			throw new NoConverterFound(p_childClass);
		}
		return l_retVal;
	}

	protected static void parseExceptionThrow(NumberFormatException e, String p_inputString, String p_type,
			int p_location) throws ParseException {
		final ParseException l_pException = new ParseException("Unable to parse " + p_inputString + " as a " + p_type,
				p_location);
		l_pException.addSuppressed(e);
		throw l_pException;
	}

}
