/**
 * 
 */
package com.airsltd.core.data;

import com.airsltd.core.data.annotations.AirsPersistentClass;
import com.airsltd.core.data.annotations.AirsPersistentField;
import com.airsltd.core.data.annotations.FieldStyle;

/**
 * @author jon
 *
 */
@AirsPersistentClass(keys=6,style=FieldStyle.CAPITALIZED,table="mockTable",sort={1,2})
public class MockPersistedData extends PersistedData {

	@AirsPersistentField
	private AutoIncrementField f_id = new AutoIncrementField(MockPersistedData.class);
	@AirsPersistentField(fieldName="GroupedId")
	private int f_group;
	@AirsPersistentField(size=15)
	private String f_name;
	@AirsPersistentField(index=4)
	private FieldStyle f_enum;
	
	public MockPersistedData() {
	}

	public MockPersistedData(int p_i, String p_string) {
		f_group = p_i;
		f_name = p_string;
	}

	/**
	 * @return the id
	 */
	public AutoIncrementField getId() {
		return f_id;
	}

	/**
	 * @param p_id the id to set
	 */
	public void setId(AutoIncrementField p_id) {
		f_id = p_id;
	}

	/**
	 * @return the group
	 */
	public int getGroup() {
		return f_group;
	}

	/**
	 * @param p_group the group to set
	 */
	public void setGroup(int p_group) {
		f_group = p_group;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return f_name;
	}

	/**
	 * @param p_name the name to set
	 */
	public void setName(String p_name) {
		f_name = p_name;
	}

	/**
	 * @return the enum
	 */
	public FieldStyle getEnum() {
		return f_enum;
	}

	/**
	 * @param p_enum the enum to set
	 */
	public void setEnum(FieldStyle p_enum) {
		f_enum = p_enum;
	}
	
}
