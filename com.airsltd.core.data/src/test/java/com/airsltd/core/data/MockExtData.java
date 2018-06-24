/**
 * 
 */
package com.airsltd.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jon Boley
 *
 */
public class MockExtData extends MockData implements IBlockDataExt<MockExtData> {
	
	private int f_addCount = 0;
	private int f_removeCount = 0;
	private List<MockExtData> f_modifyCount = new ArrayList<>();

	public MockExtData() {
		super();
	}

	public MockExtData(int p_i, int p_j) {
		super(p_i, p_j);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockDataExt#addRecord()
	 */
	@Override
	public void addRecord() {
		f_addCount++;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockDataExt#modifyRecord(com.airsltd.core.data.IBlockData)
	 */
	@Override
	public void modifyRecord(MockExtData p_oldData) {
		f_modifyCount.add(p_oldData);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.data.IBlockDataExt#removeRecord()
	 */
	@Override
	public void removeRecord() {
		f_removeCount++;
	}

	/**
	 * @return the addCount
	 */
	public int getAddCount() {
		return f_addCount;
	}

	/**
	 * @return the removeCount
	 */
	public int getRemoveCount() {
		return f_removeCount;
	}

	/**
	 * @return the modifyCount
	 */
	public List<MockExtData> getModifyCount() {
		return f_modifyCount;
	}

		public void clear() {
		f_modifyCount.clear();
		f_addCount = 0;
		f_removeCount = 0;
	}

}
