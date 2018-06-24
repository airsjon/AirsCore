/**
 *
 */
package com.airsltd.core.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface IBlockData defines an object which can be stored to a database in
 * chunks. Insert statements will be formed in the following manner: <br>
 * <br>
 * {insertHead}{first insertValues}, {second insertValues}, ... {nth
 * insertValues}
 *
 * @author Jon
 *
 */
public interface IBlockData {

	/**
	 * Get the field names for this data using the bit-set fields<br>
	 * Normal format: '<i>field 1</i>,<i>field 2</i>, ...,<i>field n</i>'
	 *
	 * @param is
	 *            a bit-set representing all the fields to be included in the
	 *            list. A value of -1 means that all fields are to be included
	 * @return a String that represents the SQL command to execute
	 */
	String fieldNames(long fields);

	/**
	 * Get the insert head for this data using the bit-set fields<br>
	 * Normal format: '(<i>field 1</i>,<i>field 2</i>, ...,<i>field n</i>)'
	 *
	 * @param is
	 *            a bit-set representing all the fields to be included in the
	 *            update. A value of -1 means that all fields are to be included
	 * @return a String that represents the SQL command to execute
	 */
	String insertHead(long fields);

	/**
	 * Get the insert string for this data using the bit-set fields<br>
	 * Normal format: '(<i>data</i>,<i>data</i>)'<br>
	 * When used for block inputes these will be linked together as<br>
	 * <i>insert one</i>, <i>insert two</i>, ..., <i>insert n</i> in groups
	 * (default is 40)
	 *
	 * Note you need to make sure your data string is SQL safe and properly
	 * enclosed
	 *
	 * @param fields
	 *            is a bit-set representing all the fields to be included in the
	 *            update. A value of -1 means that all fields are to be included
	 * @return a String that represents the data to be inserted for this object
	 */
	String insertValues(long fields);

	/**
	 * This method allows for the use of '?' for data values. Then after the SQL
	 * statement has been created, all the data elements are called with the
	 * PreparedStatement and current argument offset to load the
	 * {@link PreparedStatement} up.
	 *
	 * @param fields
	 *            that have been modified/inserted
	 * @param curOffset
	 *            the current index into arguments used
	 * @param ps
	 *            the {@link PreparedStatement} to update with values
	 * @return the new offset to be used for the next data
	 */
	int insertValueCallBack(long fields, int curOffset, PreparedStatement ps) throws SQLException;

	/**
	 * Provide the SQL code to select a particular record.
	 * <p>
	 * Used for both
	 * removing and updating records Normal format: '<i>destTableName</i>.
	 * <i>datafield1</i>=<i>datavalue1</i> and <i>tableName.datafield2</i>=
	 * <i>datavalue2</i> and ... <i>tableName.datafieldn</i>=<i>datavaluen</i>
	 * 
	 * @param destTableName
	 *            String representing the name of the table to do the selecting
	 *            on. If null or empty no table name is appended to the data
	 *            fields.
	 * @param f_delete
	 *            is the select clause being used to generate a delete call
	 * @return a String represent the SQL command to use for selecting records
	 */
	String selectClause(String destTableName, boolean f_delete);

	/**
	 * return field name of all data fields that will be modified. Useful for
	 * update calls to viewers that use properties to determine if they need to
	 * be updated.
	 *
	 * @param newData
	 *            new {@link IBlockData} representing the new data
	 * @return {@link List} of {@link String} of the properties that will be
	 *         changed
	 */
	List<String> updateProperties(IBlockData newData);

	/**
	 * return SQL code to modify the table with the new data, only modifying
	 * those fields that have changed.<br>
	 * Normal format: 'SET <i>fieldName1</i>='newData1', <i>fieldName2</i>
	 * ='newData2', ..., <i>fieldNameN</i>='newDataN'<br>
	 * Used only for in-line updates
	 *
	 * @param newData
	 *            new {@link IBlockData} representing the new data
	 * @return String of the SQL code to modify the data
	 */
	String modifyBody(IBlockData newData);

	/**
	 * This returns a number that has a bit set for each field that has been
	 * modified. If -1 is returned then a primary key has been modified.
	 *
	 * @param newData
	 *            {@link IBlockData} of the new data to overwrite the current
	 *            {@link IBlockData}
	 * @return long a bit set of all the fields that have changed
	 */
	long modified(IBlockData newData);

	/**
	 * return the name of the SQL Table where this data resides
	 *
	 * @return String representation of the Table name
	 */
	String tableName();

	/**
	 * SQL code to join the temporary table airsModify to the source table
	 * {@link IBlockData<T>.tableName} <br>
	 * Normal format: 'ON <i>tableName.keyField1</i>=airsModify.<i>keyField2</i>
	 * and <i>tableName.keyField1</i>=airsModify.<i>keyField2</i> ... and
	 * <i>tableName.keyFieldn</i>=airsModify.<i>keyFieldn</i>
	 * 
	 * @return String representation of the temporary table join
	 */
	String keyJoin();

	/**
	 * Bit set of which fields are part of the key and need to be copied into
	 * the temporary table
	 *
	 * @return long bit set of all the fields that need to be copied to maintain
	 *         the key
	 */
	long keyFields();

	/**
	 * SQL code to set the table with values from the temporary table<br>
	 * Normal format '<i>tableName.modField1<i>=airsModify.<i>modField1</i>, ...
	 * <i>tableName.modField1<i>=airsModify.<i>modField1</i>
	 *
	 * @param fieldset
	 * @return String representation of the update to the table from teh
	 *         temporary table.
	 */
	String fieldUpdates(long fieldset);

	/**
	 * When an AutoIncrement field is update in the database this is called on
	 * each record.
	 *
	 * @param key
	 *            0 based index of the key that was generated
	 * @param id
	 *            the generated key
	 */
	void autoIncrementField(int key, long id);

	/**
	 * return an array of all the field names
	 *
	 * @return
	 */
	String[] getFieldNames();

	/**
	 * return the fields that need an auto increment index. This value should be
	 * constant for all records that may be created.
	 *
	 * @return
	 */
	long autoIncrements();

	/**
	 * return the fields that are read only and should not be updated or stored.
	 *
	 * @return
	 */
	long readOnlyFields();

	/**
	 * Copy data from another instance
	 *
	 * @param p_newData
	 */
	void copy(IBlockData p_newData);

	/**
	 * Make a copy of this object
	 * 
	 * @return
	 */
	IBlockData copy();

	/**
	 * Return the field data for p_cnt.
	 *
	 * @param p_cnt
	 * @return
	 */
	IBlockField<?> toField(int p_cnt);

	/**
	 * Return the field in a string format that could be used to store on an SQL
	 * database.
	 *
	 * @param p_fieldIndex
	 *            the index of the field
	 * @return an SQL string that represents the data in field p_fieldIndex
	 */
	String toSqlValue(int p_fieldIndex);

	/**
	 * Return a nice field name.
	 * <p>
	 * Useful when displaying for an end user.
	 *
	 * @return a String meant for displaying to an end user.
	 */
	String[] getNiceFieldNames();

	/**
	 *
	 * @param p_other
	 * @return
	 */
	boolean exactMatch(IBlockData p_other);

	/**
	 * The data has just been added to the block provider.
	 * <p>
	 * Update any data fields that needed to be modified.
	 * 
	 * @see AutoIncrementField
	 */
	void dataAdded();
}
