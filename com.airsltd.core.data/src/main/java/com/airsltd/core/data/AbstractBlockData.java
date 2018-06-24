/**
 *
 */
package com.airsltd.core.data;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.airsltd.core.NotificationStatus;

/**
 * Provides most of the core methods for a standard implementation of
 * {@link ICsvData} and {@link IBlockData}. A set of helper methods for storing
 * and reading from an SQL database are also provided.
 *
 * @see IPersistentId
 *
 * @author Jon Boley
 *
 */
abstract class AbstractBlockData implements ICsvData, Comparable<AbstractBlockData> {

	private static final int NULLHASH = 0xF3281340;
	private static final String ANDSTRING = " AND ";

	public AbstractBlockData() {
	}

	public AbstractBlockData(String[] p_args) {
		fromStringCsv(p_args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#autoIncrements()
	 */
	@Override
	public final long autoIncrements() {
		long l_retVal = 0;
		final String[] l_fieldNames = getFieldNames();
		for (int l_cnt = 0; l_cnt != l_fieldNames.length; l_cnt++) {
			final Object l_data = toField(l_cnt).getValue();
			if (l_data instanceof AutoIncrementField && ((AutoIncrementField) l_data).getId() < 0) {
				l_retVal += 1L << l_cnt;
			}
		}
		return l_retVal;
	}

	/**
	 * This hook allows the database to add a check to be added to deletion attempts.
	 * <p>
	 * This allows the model to not be the sole determinant in whether
	 * a record is deleted or not.
	 * 
	 * @param destTableName
	 *
	 * @return this method must return a non-null value. A return of the empty
	 *         string means that no check is to be done.
	 */
	protected String deleteCheckClause(String p_destTableName) {
		return "";
	}

	private String deleteCheckClauseInternal(boolean p_delete, boolean p_firstp, String p_string,
			String p_destTableName) {
		String l_retVal = p_firstp ? "" : p_string;
		if (p_delete) {
			final String l_deleteCheck = deleteCheckClause(p_destTableName);
			if (!l_deleteCheck.isEmpty()) {
				l_retVal += (p_firstp ? "" : ANDSTRING) + l_deleteCheck;
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int l_retVal = tableName().hashCode();
		final long l_fieldset = this instanceof IKeyedData ? ((IKeyedData) this).primaryKeyFields() : keyFields();
		final int l_fields = getFieldNames().length;
		for (int l_cnt = 0; l_cnt != l_fields; l_cnt++) {
			if ((l_fieldset & 1L << l_cnt) != 0) {
				final Object l_val = toField(l_cnt).getValue();
				l_retVal ^= l_val == null ? NULLHASH : l_val.hashCode();
			}
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_obj) {
		boolean l_retVal = false;
		if (this == p_obj) {
			l_retVal = true;
		} else {
			if (p_obj instanceof AbstractBlockData) {
				final AbstractBlockData l_oData = (AbstractBlockData) p_obj;
				if (tableName().equals(l_oData.tableName())) {
					l_retVal = equalsByField(l_oData);
				}
			}
		}
		return l_retVal;
	}

	protected boolean equalsByField(AbstractBlockData p_oData) {
		final long l_fieldset = this instanceof IKeyedData ? ((IKeyedData) this).primaryKeyFields() : keyFields();
		boolean l_retVal = true;
		final int l_fields = getFieldNames().length;
		for (int l_cnt = 0; l_cnt != l_fields; l_cnt++) {
			if ((l_fieldset & 1L << l_cnt) != 0 && !fieldEqual(l_cnt, p_oData)) {
				l_retVal = false;
				break;
			}
		}
		return l_retVal;
	}

	public final boolean fieldEqual(int p_cnt, IBlockData p_newData) {
		final IBlockField<?> l_field = toField(p_cnt);
		final IBlockField<?> l_oField = p_newData == null ? null : p_newData.toField(p_cnt);
		return l_field == null ? l_oField == null : l_field.equals(l_oField);
	};

	public int fieldCompare(int p_field, IBlockData p_compareData) {
		final IBlockField<?> l_field = toField(p_field);
		final IBlockField<?> l_oField = p_compareData == null ? null : p_compareData.toField(p_field);
		return l_field == null ? l_oField == null ? 0 : -1 : l_field.compareTo(l_oField);
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

	public Object fieldValue(int p_cnt) {
		final IBlockField<?> l_field = toField(p_cnt);
		return l_field == null ? null : l_field.getValue();
	}

	public void modifyFieldValue(int p_cnt, Object p_value) {
		final IBlockField<? extends Object> l_field = toField(p_cnt);
		if (l_field != null) {
			try {
				l_field.getClass().getMethod("setValue", Object.class).invoke(l_field, p_value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				CoreInterface.getSystem().handleException("Internal error modifing field", e, NotificationStatus.BLOCK);
			}
		}
	}

	public abstract IBlockFieldFactory[] fieldFactories();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#fieldUpdates(long)
	 */
	@Override
	public final String fieldUpdates(long p_fieldset) {
		final StringBuilder sb = new StringBuilder();
		boolean firstp = true;
		final String[] fieldNames = getFieldNames();
		int cnt = 0;
		for (final String field : fieldNames) {
			if ((p_fieldset & 1L << cnt) != 0) {
				if (firstp) {
					firstp = false;
				} else {
					sb.append(", ");
				}
				sb.append("dest.`" + field + "`=src.`" + field + "`");
			}
			cnt++;
		}
		return firstp ? "" : sb.toString();
	}

	public static String fieldList(long p_fieldset, String[] p_fieldNames) {
		final StringBuilder sb = new StringBuilder();
		boolean firstp = true;
		int cnt = 0;
		for (final String field : p_fieldNames) {
			if ((p_fieldset & 1L << cnt) != 0) {
				if (firstp) {
					firstp = false;
				} else {
					sb.append(", ");
				}
				sb.append("`" + field + "`");
			}
			cnt++;
		}
		return firstp ? "" : sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#fieldNames(long)
	 */
	@Override
	public String fieldNames(long p_fields) {
		return fieldList(p_fields, getFieldNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#insertHead(long)
	 */
	@Override
	public final String insertHead(long p_fields) {
		final String l_retVal = fieldNames(p_fields);
		return l_retVal.isEmpty() ? l_retVal : "(" + l_retVal + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#insertValueCallBack(long, int,
	 * java.sql.PreparedStatement)
	 */
	@Override
	public int insertValueCallBack(long p_fields, int p_curOffset, PreparedStatement p_ps) throws SQLException {
		return p_curOffset;
	}

	@Override
	public final String insertValues(long p_fields) {
		final StringBuilder l_sb = new StringBuilder("(");
		boolean l_firstp = true;
		final String[] l_fieldNames = getFieldNames();
		for (int l_cnt = 0; l_cnt != l_fieldNames.length; l_cnt++) {
			if ((p_fields & 1L << l_cnt) != 0) {
				if (l_firstp) {
					l_firstp = false;
				} else {
					l_sb.append(", ");
				}
				l_sb.append(toSqlValue(l_cnt));
			}
		}
		l_sb.append(")");
		return l_firstp ? "()" : l_sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#keyJoin()
	 */
	@Override
	public final String keyJoin() {
		final long l_fieldset = keyFields();
		final StringBuilder l_sb = new StringBuilder("ON ");
		boolean l_firstp = true;
		final String[] l_fieldNames = getFieldNames();
		int l_cnt = 0;
		for (final String l_field : l_fieldNames) {
			if ((l_fieldset & 1L << l_cnt) != 0) {
				if (l_firstp) {
					l_firstp = false;
				} else {
					l_sb.append(ANDSTRING);
				}
				l_sb.append("dest.`" + l_field + "`=src.`" + l_field + "`");
			}
			l_cnt++;
		}
		return l_firstp ? "" : l_sb.toString();
	}

	@Override
	public final long modified(IBlockData p_newData) {
		long l_retVal = 0;
		final String[] l_fieldNames = getFieldNames();
		for (int l_cnt = 0; l_cnt != l_fieldNames.length; l_cnt++) {
			if (!fieldEqual(l_cnt, p_newData)) {
				l_retVal += 1L << l_cnt;
			}
		}
		return l_retVal;
	}

	@Override
	public final String modifyBody(IBlockData p_newData) {
		final StringBuilder l_sb = new StringBuilder();
		boolean l_firstp = true;
		final String[] l_fieldNames = getFieldNames();
		int l_cnt = 0;
		for (final String l_fieldName : l_fieldNames) {
			if ((p_newData.readOnlyFields() & 1 << l_cnt) == 0 && !fieldEqual(l_cnt, p_newData)) {
				if (l_firstp) {
					l_sb.append("SET ");
					l_firstp = false;
				} else {
					l_sb.append(", ");
				}
				l_sb.append("`" + l_fieldName + "`=" + ((AbstractBlockData) p_newData).toSqlValue(l_cnt));
			}
			l_cnt++;
		}
		return l_firstp ? "" : l_sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#selectClause(java.lang.String)
	 */
	@Override
	public final String selectClause(String p_destTableName, boolean p_delete) {
		final String l_tableName = p_destTableName != null && !p_destTableName.isEmpty() ? p_destTableName + "." : "";
		final long l_fieldset = keyFields();
		final StringBuilder l_sb = new StringBuilder();
		boolean l_firstp = true;
		final String[] l_fieldNames = getFieldNames();
		int l_cnt = 0;
		for (final String l_field : l_fieldNames) {
			if ((l_fieldset & 1L << l_cnt) != 0) {
				if (l_firstp) {
					l_firstp = false;
				} else {
					l_sb.append(ANDSTRING);
				}
				l_sb.append(l_tableName + "`" + l_field + "`=" + toSqlValue(l_cnt));
			}
			l_cnt++;
		}
		return deleteCheckClauseInternal(p_delete, l_firstp, l_sb.toString(), p_destTableName);
	}

	@Override
	public final String toSqlValue(int p_fieldIndex) {
		final IBlockField<?> l_field = toField(p_fieldIndex);
		return l_field == null ? "" : toField(p_fieldIndex).toSqlValue();
	};

	@Override
	public String[] toStringCsv() {
		final int l_fields = getFieldNames().length;
		final String[] l_retVal = new String[l_fields];
		for (int cnt = 0; cnt != l_fields; cnt++) {
			l_retVal[cnt] = toSqlValue(cnt);
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IBlockData#updateProperties(com.airsltd.core.data.
	 * IBlockData)
	 */
	@Override
	public final List<String> updateProperties(IBlockData p_newData) {
		final String[] l_fieldNames = getFieldNames();
		final List<String> l_retVal = new ArrayList<String>();
		int cnt = 0;
		for (final String l_fieldName : l_fieldNames) {
			if (!fieldEqual(cnt, p_newData)) {
				l_retVal.add(l_fieldName);
			}
			cnt++;
		}
		return l_retVal.isEmpty() ? null : l_retVal;
	}

	@Override
	public void fromStringCsv(String[] p_strings) {
		final int l_length = Math.min(p_strings.length, getFieldNames().length);
		for (int l_index = 0; l_index != l_length; l_index++) {
			final IBlockField<?> l_field = toField(l_index);
			try {
				final String l_current = BlockData.unquote(p_strings[l_index]);
				l_field.fromSqlValue(l_current);
			} catch (final ParseException e) {
				handleParseException(l_index, p_strings[l_index], e);
			}
		}
	}

	/**
	 * This handle allows a SuperClass the ability to handle parse exceptions
	 * while converting from a string value to the data fields. <br>
	 * <br>
	 * Default behavior is to ignore the parse exception and continue to load
	 * the remaining fields.
	 *
	 * @param p_index
	 *            index of the field that was being parsed
	 * @param p_string
	 *            the {@link String} that was being parsed
	 * @param p_e
	 *            the {@link ParseException} that occured
	 */
	public void handleParseException(int p_index, String p_string, ParseException p_e) {
		CoreInterface.getSystem().getLog().info("Parsing Error: " + p_string + " around " + p_index, p_e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#getNiceFieldNames()
	 */
	@Override
	public String[] getNiceFieldNames() {
		return getFieldNames();
	}

	@Override
	public String toString() {
		final StringBuilder l_sb = new StringBuilder();
		l_sb.append(tableName());
		l_sb.append(" [");
		int index = 0;
		for (final String l_field : getNiceFieldNames()) {
			if (index != 0) {
				l_sb.append(", ");
			}
			l_sb.append(l_field);
			l_sb.append("=");
			final Object l_val = toField(index++).getValue();
			l_sb.append(l_val == null ? "null" : l_val.toString());
		}
		l_sb.append("]");
		return l_sb.toString();
	}

	@Override
	public final boolean exactMatch(IBlockData p_other) {
		return p_other instanceof AbstractBlockData && p_other.getClass() == getClass() && modified(p_other) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IBlockData#copy(com.airsltd.core.data.IBlockData)
	 */
	@Override
	public void copy(IBlockData p_newData) {
		if (p_newData != null && p_newData.getClass() == getClass()) {
			final int l_fields = getFieldNames().length;
			for (int l_cnt = 0; l_cnt != l_fields; l_cnt++) {
				modifyFieldValue(l_cnt, ((AbstractBlockData) p_newData).fieldValue(l_cnt));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#copy()
	 */
	@Override
	public IBlockData copy() {
		IBlockData l_newValue = null;
		try {
			l_newValue = getClass().newInstance();
			((AbstractBlockData) l_newValue).copy(this);
		} catch (InstantiationException | IllegalAccessException e) {
			CoreInterface.getSystem().handleException("Unable to create copy of " + this, e, NotificationStatus.BLOCK);
		}
		return l_newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#dataAdded()
	 */
	@Override
	public void dataAdded() {
	}

	@Override
	public long readOnlyFields() {
		return 0;
	}

}
