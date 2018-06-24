/**
 *
 */
package com.airsltd.core.data;

import java.util.ArrayList;
import java.util.List;

import com.airsltd.core.data.annotations.AirsPersistentClass;
import com.airsltd.core.data.annotations.FieldStyle;

/**
 * Framework data for a class that is persisted.
 *
 * @author Jon Boley
 *
 */
public class PersistedDataState {

	private String f_tableName;
	private long f_keysFields;
	private int[] f_sortFields;
	private long f_readOnlyFields;
	private FieldStyle f_fieldStyle;
	private String[] f_fieldNames;
	private IBlockFieldFactory[] f_fieldFactories;
	private List<Integer> f_autoIncrementFields = new ArrayList<Integer>();

	public PersistedDataState(Class<?> p_class) {
		final AirsPersistentClass l_pClass = p_class.getAnnotation(AirsPersistentClass.class);
		f_tableName = l_pClass.table();
		f_keysFields = l_pClass.keys();
		f_fieldStyle = l_pClass.style();
		f_sortFields = l_pClass.sort();
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return f_tableName;
	}

	/**
	 * @param p_tableName
	 *            the tableName to set
	 */
	public void setTableName(String p_tableName) {
		f_tableName = p_tableName;
	}

	/**
	 * @return the keysFields
	 */
	public long getKeysFields() {
		return f_keysFields;
	}

	/**
	 * @param p_keysFields
	 *            the keysFields to set
	 */
	public void setKeysFields(long p_keysFields) {
		f_keysFields = p_keysFields;
	}

	/**
	 * @return the fieldStyle
	 */
	public FieldStyle getFieldStyle() {
		return f_fieldStyle;
	}

	/**
	 * @param p_fieldStyle
	 *            the fieldStyle to set
	 */
	public void setFieldStyle(FieldStyle p_fieldStyle) {
		f_fieldStyle = p_fieldStyle;
	}

	/**
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return f_fieldNames;
	}

	/**
	 * @param p_fieldNames
	 *            the fieldNames to set
	 */
	public void setFieldNames(String[] p_fieldNames) {
		f_fieldNames = p_fieldNames;
	}

	/**
	 * @return the fieldFactories
	 */
	public IBlockFieldFactory[] getFieldFactories() {
		return f_fieldFactories;
	}

	/**
	 * @param p_fieldFactories
	 *            the fieldFactories to set
	 */
	public void setFieldFactories(IBlockFieldFactory[] p_fieldFactories) {
		f_fieldFactories = p_fieldFactories;
	}

	/**
	 * @return the autoIncrementFields
	 */
	public List<Integer> getAutoIncrementFields() {
		return f_autoIncrementFields;
	}

	/**
	 * @param p_autoIncrementFields
	 *            the autoIncrementFields to set
	 */
	public void setAutoIncrementFields(List<Integer> p_autoIncrementFields) {
		f_autoIncrementFields = p_autoIncrementFields;
	}

	/**
	 * @return the readOnlyFields
	 */
	public long getReadOnlyFields() {
		return f_readOnlyFields;
	}

	/**
	 * @param p_readOnlyFields
	 *            the readOnlyFields to set
	 */
	public void setReadOnlyFields(long p_readOnlyFields) {
		f_readOnlyFields = p_readOnlyFields;
	}

	/**
	 * @return the sortFields
	 */
	public int[] getSortFields() {
		return f_sortFields;
	}

	/**
	 * @param p_sortFields
	 *            the sortFields to set
	 */
	public void setSortFields(int[] p_sortFields) {
		f_sortFields = p_sortFields;
	}

}
