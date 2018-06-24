/**
 *
 */
package com.airsltd.core.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.annotations.AirsPersistentField;

/**
 * @author Jon Boley
 *
 */
public class PrimitiveArrayFactory implements IBlockFieldFactory {

	private int f_currentIndex;
	private Class<?> f_componentType;
	private Field f_field;
	private AirsPersistentField f_pField;

	public PrimitiveArrayFactory(int p_currentIndex, Class<?> p_componentType, Field p_field,
			AirsPersistentField p_pField) {
		f_currentIndex = p_currentIndex;
		f_componentType = p_componentType;
		f_field = p_field;
		f_pField = p_pField;
		f_field.setAccessible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IBlockFieldFactory#field(com.airsltd.core.data.
	 * IBlockData)
	 */
	@Override
	public IBlockField<?> field(IBlockData p_blockData) {
		return new PrimitiveArrayField(p_blockData, this);
	}

	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex() {
		return f_currentIndex;
	}

	/**
	 * @param p_currentIndex
	 *            the currentIndex to set
	 */
	public void setCurrentIndex(int p_currentIndex) {
		f_currentIndex = p_currentIndex;
	}

	/**
	 * @return the componentType
	 */
	public Class<?> getComponentType() {
		return f_componentType;
	}

	/**
	 * @param p_componentType
	 *            the componentType to set
	 */
	public void setComponentType(Class<?> p_componentType) {
		f_componentType = p_componentType;
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return f_field;
	}

	/**
	 * @param p_field
	 *            the field to set
	 */
	public void setField(Field p_field) {
		f_field = p_field;
	}

	/**
	 * @return the pField
	 */
	public AirsPersistentField getpField() {
		return f_pField;
	}

	/**
	 * @param p_pField
	 *            the pField to set
	 */
	public void setpField(AirsPersistentField p_pField) {
		f_pField = p_pField;
	}

	public Object value(IBlockData p_data) {
		Object l_retVal = null;
		try {
			l_retVal = f_field.get(p_data);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CoreInterface.getSystem().handleException("Data error", e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	public void modify(IBlockData p_data, Object p_value) {
		try {
			f_field.set(p_data, p_value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CoreInterface.getSystem().handleException("Data error", e, NotificationStatus.BLOCK);
		}
	}

	public String toSql(Object p_value) {
		String l_retVal = null;
		if (f_componentType == int.class) {
			l_retVal = Arrays.toString((int[]) p_value);
		} else if (f_componentType == float.class) {
			l_retVal = Arrays.toString((float[]) p_value);
		} else if (f_componentType == boolean.class) {
			l_retVal = Arrays.toString((boolean[]) p_value);
		} else if (f_componentType == double.class) {
			l_retVal = Arrays.toString((double[]) p_value);
		} else if (f_componentType == byte.class) {
			l_retVal = Arrays.toString((byte[]) p_value);
		} else if (f_componentType == char.class) {
			l_retVal = Arrays.toString((char[]) p_value);
		} else if (f_componentType == short.class) {
			l_retVal = Arrays.toString((short[]) p_value);
		} else if (f_componentType == long.class) {
			l_retVal = Arrays.toString((long[]) p_value);
		}
		return l_retVal;
	}

	public Object fromSql(String p_string) {
		Object l_retVal = null;
		final String l_string = p_string.replaceAll("[\\[\\]]", "");
		try (Scanner l_scanner = new Scanner(l_string)) {
			l_scanner.useDelimiter(Pattern.compile("\\,\\s+"));
			if (f_componentType == int.class) {
				l_retVal = readIntArray(l_scanner);
			} else if (f_componentType == float.class) {
				l_retVal = readFloatArray(l_scanner);
			} else if (f_componentType == boolean.class) {
				l_retVal = readBooleanArray(l_scanner);
			} else if (f_componentType == double.class) {
				l_retVal = readDoubleArray(l_scanner);
			} else if (f_componentType == byte.class) {
				l_retVal = readByteArray(l_scanner);
			} else if (f_componentType == char.class) {
				l_retVal = readCharArray(l_scanner);
			} else if (f_componentType == short.class) {
				l_retVal = readShortArray(l_scanner);
			} else if (f_componentType == long.class) {
				l_retVal = readLongArray(l_scanner);
			}
		}
		return l_retVal;
	}

	protected long[] readLongArray(Scanner p_scanner) {
		final List<Long> l_list = new ArrayList<Long>();
		while (p_scanner.hasNextLong()) {
			l_list.add(p_scanner.nextLong());
		}
		final long[] l_retVal = new long[l_list.size()];
		int l_index = 0;
		for (final Long l_long : l_list) {
			l_retVal[l_index++] = l_long;
		}
		return l_retVal;
	}

	protected Object readShortArray(Scanner p_scanner) {
		final List<Short> l_list = new ArrayList<Short>();
		while (p_scanner.hasNextShort()) {
			l_list.add(p_scanner.nextShort());
		}
		final short[] l_retVal = new short[l_list.size()];
		int l_index = 0;
		for (final Short l_short : l_list) {
			l_retVal[l_index++] = l_short;
		}
		return l_retVal;
	}

	protected Object readCharArray(Scanner p_scanner) {
		throw new UnsupportedOperationException("Character arrays are not currently readable");
	}

	protected Object readByteArray(Scanner p_scanner) {
		final List<Byte> l_list = new ArrayList<Byte>();
		while (p_scanner.hasNextByte()) {
			l_list.add(p_scanner.nextByte());
		}
		final byte[] l_retVal = new byte[l_list.size()];
		int l_index = 0;
		for (final Byte l_byte : l_list) {
			l_retVal[l_index++] = l_byte;
		}
		return l_retVal;
	}

	protected Object readDoubleArray(Scanner p_scanner) {
		final List<Double> l_list = new ArrayList<Double>();
		while (p_scanner.hasNextDouble()) {
			l_list.add(p_scanner.nextDouble());
		}
		final double[] l_retVal = new double[l_list.size()];
		int l_index = 0;
		for (final Double l_double : l_list) {
			l_retVal[l_index++] = l_double;
		}
		return l_retVal;
	}

	protected Object readBooleanArray(Scanner p_scanner) {
		final List<Boolean> l_list = new ArrayList<Boolean>();
		while (p_scanner.hasNextBoolean()) {
			l_list.add(p_scanner.nextBoolean());
		}
		final boolean[] l_retVal = new boolean[l_list.size()];
		int l_index = 0;
		for (final Boolean l_boolean : l_list) {
			l_retVal[l_index++] = l_boolean;
		}
		return l_retVal;
	}

	protected Object readFloatArray(Scanner p_scanner) {
		final List<Float> l_list = new ArrayList<Float>();
		while (p_scanner.hasNextFloat()) {
			l_list.add(p_scanner.nextFloat());
		}
		final float[] l_retVal = new float[l_list.size()];
		int l_index = 0;
		for (final Float l_float : l_list) {
			l_retVal[l_index++] = l_float;
		}
		return l_retVal;
	}

	protected Object readIntArray(Scanner p_scanner) {
		final List<Integer> l_list = new ArrayList<Integer>();
		while (p_scanner.hasNextInt()) {
			l_list.add(p_scanner.nextInt());
		}
		final int[] l_retVal = new int[l_list.size()];
		int l_index = 0;
		for (final Integer l_int : l_list) {
			l_retVal[l_index++] = l_int;
		}
		return l_retVal;
	}

	public int compareTo(Object p_o) {
		return 0;
	}
}
