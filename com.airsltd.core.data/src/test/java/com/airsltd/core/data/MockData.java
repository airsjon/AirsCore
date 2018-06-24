package com.airsltd.core.data;

import static org.mockito.BDDMockito.*;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import com.airsltd.core.data.IBlockData;

public class MockData implements INamed, IBlockData, IPersistentId {
	private static final String[] FIELDNAMES = new String[] { "Id", "Data" };
	private static final IBlockField<?> s_mockField = mock(IBlockField.class);
	public int someId;
	public int someData;
	public int someHiddenData;
	public boolean f_auto;

	public MockData() {
	}

	public MockData(int i, int j) {
		someId = i;
		someData = j;
	}

	@Override
	public String insertHead(long fields) {
		switch (new Long(fields & 3).intValue()) {
		case 1:
			return "(id)";
		case 2:
			return "(data)";
		case 3:
			return "(id,data)";
		}
		return "";
	}

	@Override
	public String insertValues(long fields) {
		switch (new Long(fields & 3).intValue()) {
		case 1:
			return "(" + someId + ")";
		case 2:
			return "(" + someData + ")";
		case 3:
			return "(" + someId + "," + someData + ")";
		}
		return "";
	}

	@Override
	public String selectClause(String destTableName, boolean f_delete) {
		String deleteCheck = f_delete ? " AND f_published=0" : "";
		if (destTableName != null && !destTableName.isEmpty())
			return destTableName + ".id=" + someId + deleteCheck;
		else
			return "id=" + someId + deleteCheck;
	}

	@Override
	public String modifyBody(IBlockData newData) {
		if (newData instanceof MockData) {
			MockData md = (MockData) newData;
			if (someId != md.someId) {
				if (md.someData != someData) {
					return "SET id=" + md.someId + ", data=" + md.someData;
				} else
					return "SET id=" + md.someId;

			} else if (md.someData != someData) {
				return "SET data=" + md.someData;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long modified(IBlockData newData) {
		long l_retVal = 0;
		if (newData instanceof MockData) {
			MockData md = (MockData) newData;
			if (someId != md.someId) {
				l_retVal = 1;
			}
			if (someData != md.someData) {
				l_retVal += 2;
			}
		}
		return l_retVal;
	}

	@Override
	public String tableName() {
		return "mockTable";
	}

	@Override
	public String keyJoin() {
		return "ON `mockTable`.id = airsModify.id";
	}

	@Override
	public String fieldUpdates(long fieldset) {
		return "dest.data = src.data";
	}

	@Override
	public long keyFields() {
		return 1;
	}

	@Override
	public void autoIncrementField(int key, long id) {
		if (f_auto && key == 0)
			someId = (int) id;
	}

	@Override
	public String[] getFieldNames() {
		return FIELDNAMES;
	}

	@Override
	public int insertValueCallBack(long fields, int curOffset,
			PreparedStatement ps) {
		return curOffset;
	}

	@Override
	public long autoIncrements() {
		return f_auto ? 1 : 0;
	}

	/**
	 * Normally return null, but if the original value has the id 5 a list
	 * containing "someId" will be returned.
	 */
	@Override
	public List<String> updateProperties(IBlockData p_newData) {
		return (someId == 5) ? Arrays.asList(new String[] { "someId" }) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_arg0) {
		boolean rVal = false;
		if (p_arg0 instanceof MockData) {
			MockData l_arg = (MockData) p_arg0;
			rVal = (someId == l_arg.someId);
		}
		// TODO Auto-generated method stub
		return rVal || super.equals(p_arg0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new Integer(someId).hashCode();
	}

	@Override
	public void copy(IBlockData p_newData) {
		if (p_newData instanceof MockData) {
			MockData l_new = (MockData) p_newData;
			someId = l_new.someId;
			someData = l_new.someData;
			someHiddenData = l_new.someHiddenData;
		}
	}

	@Override
	public IBlockData copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBlockField<?> toField(int p_cnt) {
		return s_mockField;
	}

	@Override
	public String fieldNames(long p_fields) {
		return "*";
	}

	@Override
	public long primaryKeyFields() {
		return 1;
	}

	@Override
	public long getPersistentID() {
		return someId;
	}

	@Override
	public String toSqlValue(int p_fieldIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getNiceFieldNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exactMatch(IBlockData p_other) {
		return equals(p_other);
	}

	@Override
	public String toName() {
		return someId + "";
	}

	@Override
	public void dataAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long readOnlyFields() {
		return 0;
	}

}
