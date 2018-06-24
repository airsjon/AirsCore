/**
 * 
 */
package com.airsltd.core.data;

import com.airsltd.core.data.AbstractBlockData;
import com.airsltd.core.data.BlockFieldFactory;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IBlockFieldFactory;
import com.airsltd.core.data.converters.BlockConverters;
import com.airsltd.core.data.converters.StringSQLConvert;

/**
 * @author jon_000
 *
 */
public class MockData3 extends AbstractBlockData {

	private static final IBlockFieldFactory[] BLOCKFIELDFACTORIES = new IBlockFieldFactory[] {
		new BlockFieldFactory<MockData3, Integer>(0, BlockConverters.INTEGERCONVERT) {

			@Override
			public Integer value(MockData3 p_blockData) {
				return p_blockData.f_id;
			}

			@Override
			public void modifyValue(MockData3 p_blockData, Integer p_newValue) {
				p_blockData.f_id = p_newValue;
			}
		},
		new BlockFieldFactory<MockData3, String>(1, new StringSQLConvert(64)) {

			@Override
			public String value(MockData3 p_blockData) {
				return p_blockData.f_description;
			}

			@Override
			public void modifyValue(MockData3 p_blockData, String p_newValue) {
				p_blockData.f_description = p_newValue;
			}
		}
	};
	private static final String[] FIELDNAMES = new String[] { 
		"FieldOne", "FieldTwo" 
	};
	private int	f_id;
	private String f_description;
	
	public MockData3() {
	}
	
	public MockData3(int p_id, String p_description) {
		super();
		f_id = p_id;
		f_description = p_description;
	}

	public MockData3(MockData3 p_mockData3) {
		copy(p_mockData3);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#tableName()
	 */
	@Override
	public String tableName() {
		return "mockTable";
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#keyFields()
	 */
	@Override
	public long keyFields() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#autoIncrementField(int, long)
	 */
	@Override
	public void autoIncrementField(int p_key, long p_id) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#getFieldNames()
	 */
	@Override
	public String[] getFieldNames() {
		return FIELDNAMES;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#copy(com.airsltd.core.data.IBlockData)
	 */
	@Override
	public void copy(IBlockData p_newData) {
		if (p_newData instanceof MockData3) {
			f_id = ((MockData3) p_newData).f_id;
			f_description  = ((MockData3) p_newData).f_description;
		}
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockData#copy()
	 */
	@Override
	public IBlockData copy() {
		return new MockData3(this);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.AbstractBlockData#fieldFactories()
	 */
	@Override
	public IBlockFieldFactory[] fieldFactories() {
		return BLOCKFIELDFACTORIES;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return f_id;
	}

	/**
	 * @param p_id the id to set
	 */
	public void setId(int p_id) {
		f_id = p_id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return f_description;
	}

	/**
	 * @param p_description the description to set
	 */
	public void setDescription(String p_description) {
		f_description = p_description;
	}

	@Override
	public int compareTo(AbstractBlockData p_o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
