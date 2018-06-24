/**
 *
 */
package com.airsltd.core.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.airsltd.core.data.annotations.AirsPersistentArray;
import com.airsltd.core.data.annotations.AirsPersistentClass;
import com.airsltd.core.data.annotations.AirsPersistentField;

/**
 * This class moves the declaration of AbstractBlockData to use annotations.
 * <p>
 *
 * @author Jon Boley
 * @see AirsPersistentClass
 * @see AirsPersistentField
 */

public class PersistedData extends AbstractBlockData {

	private static final int FIELDOFFSET = 2;
	protected static Map<Class<?>, PersistedDataState> s_states = new HashMap<Class<?>, PersistedDataState>();
	protected final Class<? extends PersistedData> f_class;

	public PersistedData() {
		f_class = getClass();
		if (!s_states.containsKey(f_class)) {
			s_states.put(f_class, new PersistedDataState(f_class));
			loadFields();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#tableName()
	 */
	@Override
	public String tableName() {
		return s_states.get(f_class).getTableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#getFieldNames()
	 */
	@Override
	public String[] getFieldNames() {
		return s_states.get(f_class).getFieldNames();
	}

	/**
	 * Using the annotation AirsPersistentField load the field names and
	 * factories.
	 */
	public void loadFields() {
		int l_currentIndex = 0;
		final List<String> l_fieldNames = new ArrayList<String>();
		final List<IBlockFieldFactory> l_factories = new ArrayList<IBlockFieldFactory>();
		long l_readOnlyFields = 0L;
		for (final Field l_field : getAllFields()) {
			final AirsPersistentField l_pField = l_field.getAnnotation(AirsPersistentField.class);
			l_currentIndex = calculateIndex(l_currentIndex, l_pField);
			final AirsPersistentArray l_pArray = l_field.getAnnotation(AirsPersistentArray.class);
			final String l_calculatedFieldName = calculateFieldName(l_field, l_pField);
			if (l_pArray != null) {
				l_currentIndex = processFieldArray(l_fieldNames, l_factories, l_currentIndex, l_field, l_pField,
						l_calculatedFieldName, l_pArray);
			} else {
				l_fieldNames.add(l_calculatedFieldName);
				l_factories.add(calculateFactory(l_currentIndex, l_field, l_pField));
				if (l_pField.readOnly()) {
					l_readOnlyFields ^= 1 << l_currentIndex;
				}
				l_currentIndex++;
			}
		}
		final PersistedDataState l_persistedDataState = s_states.get(f_class);
		l_persistedDataState.setFieldNames(l_fieldNames.toArray(new String[] {}));
		l_persistedDataState.setFieldFactories(l_factories.toArray(new IBlockFieldFactory[] {}));
		l_persistedDataState.setReadOnlyFields(l_readOnlyFields);
	}

	private int processFieldArray(List<String> p_fieldNames, List<IBlockFieldFactory> p_factories, int p_currentIndex,
			Field p_field, AirsPersistentField p_pField, String p_calculatedFieldName, AirsPersistentArray p_pArray) {
		final String[] l_fieldNames = p_pArray.fieldNames();
		int l_fieldCnt = p_pArray.fields();
		if (l_fieldCnt == -1) {
			l_fieldCnt = l_fieldNames.length;
		}
		for (int cnt = 0; cnt != l_fieldCnt; cnt++) {
			p_fieldNames.add(extractFieldName(cnt, l_fieldNames, p_calculatedFieldName));
			p_factories.add(calculateArrayFactory(p_currentIndex, cnt, p_field, p_pField));
		}
		return p_currentIndex + l_fieldCnt;
	}

	protected String extractFieldName(int p_cnt, String[] p_fieldNames, String p_calculatedFieldName) {
		String l_retVal = p_calculatedFieldName + (p_cnt + 1);
		if (p_fieldNames.length > 0 && p_cnt < p_fieldNames.length) {
			l_retVal = p_fieldNames[p_cnt];
		}
		return l_retVal;
	}

	protected List<Field> getAllFields() {
		return getAllFields(getClass());
	}

	protected List<Field> getAllFields(Class<?> p_class) {
		final List<Field> l_retVal = p_class == null ? new ArrayList<Field>() : getAllFields(p_class.getSuperclass());
		if (p_class != null) {
			for (final Field l_field : p_class.getDeclaredFields()) {
				if (l_field.getAnnotation(AirsPersistentField.class) != null) {
					l_retVal.add(l_field);
				}
			}
		}
		return l_retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected IBlockFieldFactory calculateFactory(int p_currentIndex, Field p_field, AirsPersistentField p_pField) {
		IBlockFieldFactory l_retVal = overrideFactory(p_currentIndex);
		if (l_retVal == null) {
			final Class<?> l_fieldClass = p_field.getType();
			if (isPrimativeArray(l_fieldClass)) {
				l_retVal = new PrimitiveArrayFactory(p_currentIndex, l_fieldClass.getComponentType(), p_field,
						p_pField);
			} else {
				l_retVal = new MetaBlockFactory(p_currentIndex, this.getClass(), p_field.getType(), p_field,
						overrideConverter(p_currentIndex, this.getClass(), p_field.getType(), p_pField), p_pField);
				if (p_field.getType().isAssignableFrom(AutoIncrementField.class)) {
					s_states.get(f_class).getAutoIncrementFields().add(p_currentIndex);
				}
			}
		}
		return l_retVal;
	}

	protected IBlockFieldFactory calculateArrayFactory(int p_index, int p_arrayIndex, Field p_field,
			AirsPersistentField p_pField) {
		IBlockFieldFactory l_retVal = overrideFactory(p_index + p_arrayIndex);
		if (l_retVal == null) {
			final Class<?> l_fieldClass = p_field.getType();
			if (!l_fieldClass.isArray()) {
				throw new UnsupportedOperationException("Unable to use AirsPersistentArray on non-array object");
			}
			final Class<?> l_elementClass = l_fieldClass.getComponentType();
			final Class<? extends PersistedData> l_containerClass = this.getClass();
			l_retVal = generateArrayFactory(l_elementClass, l_containerClass, p_index, p_arrayIndex, p_field, p_pField);
		}
		return l_retVal;
	}

	/**
	 * Helper function to cast into MetaBlock via p_elementClass and
	 * p_containerClass
	 * 
	 * @param p_elementClass
	 * @param p_containerClass
	 * @param p_index
	 * @param p_arrayIndex
	 * @param p_field
	 * @param p_pField
	 * @return
	 */
	private <T extends PersistedData, U> IBlockFieldFactory generateArrayFactory(Class<U> p_elementClass,
			Class<T> p_containerClass, int p_index, int p_arrayIndex, Field p_field, AirsPersistentField p_pField) {
		return new MetaBlockArrayFactory<T, U>(p_index, p_arrayIndex, p_containerClass, p_elementClass, p_field,
				overrideConverter(p_index + p_arrayIndex, p_containerClass, p_elementClass, p_pField), p_pField);
	}

	private boolean isPrimativeArray(Class<?> p_fieldClass) {
		return p_fieldClass.isArray() && p_fieldClass.getComponentType().isPrimitive();
	}

	/**
	 * This method can be overwritten to provide special case Factories.
	 * 
	 * @param p_currentIndex
	 * @return
	 */
	protected IBlockFieldFactory overrideFactory(int p_currentIndex) {
		return null;
	}

	/**
	 * This method can be overwritten to provide special case converters.
	 *
	 * @param p_currentIndex
	 * @param p_parentClass
	 * @param p_fieldClass
	 * @return
	 */
	protected <T extends PersistedData, U> IDatabaseConverter<T, U> overrideConverter(int p_currentIndex,
			Class<T> p_parentClass, Class<U> p_fieldClass, AirsPersistentField p_pField) {
		return null;
	}

	private String calculateFieldName(Field p_field, AirsPersistentField p_pField) {
		String l_retVal = p_pField.fieldName();
		if (l_retVal.isEmpty()) {
			l_retVal = p_field.getName();
			if (l_retVal.startsWith("f_")) {
				l_retVal = l_retVal.substring(FIELDOFFSET);
			}
			switch (s_states.get(f_class).getFieldStyle()) {
			case CAPITALIZED:
				l_retVal = l_retVal.substring(0, 1).toUpperCase() + l_retVal.substring(1);
				break;
			case LOWERCASE:
				l_retVal = l_retVal.toLowerCase();
				break;
			case UPPERCASE:
				l_retVal = l_retVal.toUpperCase();
				break;
			case ASIS:
			default:
			}
		}
		return l_retVal;
	}

	private int calculateIndex(int p_currentIndex, AirsPersistentField p_pField) {
		final int l_retVal = p_pField.index();
		return l_retVal == -1 ? p_currentIndex : l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.AbstractBlockData#fieldFactories()
	 */
	@Override
	public IBlockFieldFactory[] fieldFactories() {
		return s_states.get(f_class).getFieldFactories();
	}

	@Override
	public long keyFields() {
		return s_states.get(f_class).getKeysFields();
	}

	@Override
	public void autoIncrementField(int p_key, long p_id) {
		final List<Integer> l_autoIncrements = s_states.get(f_class).getAutoIncrementFields();
		if (p_key >= 0 && p_key < l_autoIncrements.size()) {
			((AutoIncrementField) fieldValue(l_autoIncrements.get(p_key))).setId(p_id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#toField(int)
	 */
	@Override
	public IBlockField<?> toField(int p_cnt) {
		final IBlockFieldFactory[] l_fieldFactories = fieldFactories();
		return p_cnt >= 0 && p_cnt < l_fieldFactories.length ? l_fieldFactories[p_cnt].field(this) : null;
	}

	@Override
	public long readOnlyFields() {
		return s_states.get(f_class).getReadOnlyFields();
	}

	@Override
	public int compareTo(AbstractBlockData p_o) {
		final int[] l_columns = s_states.get(f_class).getSortFields();
		return sortBy(l_columns, p_o);
	}

	public int sortBy(int[] l_columns, AbstractBlockData p_other) {
		int l_retVal = 0;
		for (int l_field : l_columns) {
			int l_multiplier = 1;
			if (l_field < 0) {
				l_field = -1 - l_field;
				l_multiplier = -1;
			}
			l_retVal = l_multiplier * fieldCompare(l_field, p_other);
			if (l_retVal != 0) {
				break;
			}
		}
		return l_retVal;
	}

	public Comparator<PersistedData> compareByColumns(final int[] p_columns) {
		return (p_o1, p_o2) -> p_o1.sortBy(p_columns, p_o2);
	}

	public Comparator<PersistedData> compareByColumns() {
		return (p_o1, p_o2) -> p_o1.compareTo(p_o2);
	}
}
