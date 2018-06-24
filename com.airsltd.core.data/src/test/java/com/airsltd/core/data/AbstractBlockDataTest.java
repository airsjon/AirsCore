package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.data.AbstractBlockData;
import com.airsltd.core.data.IBlockData;

public class AbstractBlockDataTest {

	public enum LocalEnum {ONE, TWO, THREE};

	public static class MockAbstractBlockData extends AbstractBlockData {
		
		public String[] f_data = { "field0", "field1", "field2", "field3", "field4", "field5" };

		@Override
		public String tableName() {
			return "MockTable";
		}

		@Override
		public long keyFields() {
			return 3;
		}

		@Override
		public String[] getFieldNames() {
			return new String[] { "First", "Second", "Third", "Fourth", "Fifth", "Sixth" };
		}

		@Override
		public void autoIncrementField(int p_key, long p_id) {
		}

		/* (non-Javadoc)
		 * @see com.airsltd.core.data.AbstractBlockData#deleteCheckClause(java.lang.String)
		 */
		@Override
		protected String deleteCheckClause(String p_destTableName) {
			return (p_destTableName==null)?super.deleteCheckClause(p_destTableName):p_destTableName+".deleteCheck";
		}

		@Override
		public void copy(IBlockData p_newData) {
		}

		@Override
		public IBlockData copy() {
			return null;
		}

		@Override
		public IBlockFieldFactory[] fieldFactories() {
			return MOCKBLOCKS;
		}
		
		public String[] getData() {
			return f_data;
		}

		@Override
		public int compareTo(AbstractBlockData p_o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public static class MockBlockField implements IBlockField<String> {
		
		private int f_i;
		private MockAbstractBlockData f_data;

		public MockBlockField(MockAbstractBlockData p_data, int p_i) {
			f_data = p_data;
			f_i = p_i;
		}

		@Override
		public String toSqlValue() {
			return getValue();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object p_obj) {
			if (p_obj instanceof MockBlockField) {
				MockBlockField l_field = (MockBlockField) p_obj;
				return toSqlValue().equals(l_field.toSqlValue());
			}
			return super.equals(p_obj);
		}

		@Override
		public String getValue() {
			return f_data.getData()[f_i];
		}

		@Override
		public void setValue(String p_newData) {
			f_data.getData()[f_i]=p_newData;
		}

		@Override
		public void fromSqlValue(String string) throws ParseException {
			if ("throwParseException".equals(string)) {
				throw new ParseException("you said to", 3);
			} else {
				setValue(string);
			}
		}

		@Override
		public int compareTo(Object p_o) {
			return 0;
		}

		
	}

	/**
	 * @author Jon
	 *
	 */
	public static class MockFieldFactory implements IBlockFieldFactory {

		private int f_int;
		
		public MockFieldFactory(int p_int) {
			f_int = p_int;
		}

		@Override
		public IBlockField<String> field(IBlockData p_blockData) {

			return new MockBlockField((MockAbstractBlockData) p_blockData, f_int);
		}

	}

	protected static final IBlockFieldFactory[] MOCKBLOCKS = {
		new MockFieldFactory(0),
		new MockFieldFactory(1),
		new MockFieldFactory(2),
		new MockFieldFactory(3),
		new MockFieldFactory(4),
		new MockFieldFactory(5)
	};
	protected static final IBlockFieldFactory[] MOCKBLOCKSNO = {
		new MockFieldFactory(0),
		new MockFieldFactory(1),
		new MockFieldFactory(2),
		new MockFieldFactory(3),
		new MockFieldFactory(4),
		new MockFieldFactory(5)
	};
	private AbstractBlockData f_testData;
	private AbstractBlockData f_testData2;
	private AbstractBlockData f_testNoSelectData;
	private MockAbstractBlockData f_testData3;
	private ICoreInterface f_oldSystem;

	@Before
	public void setUp() throws Exception {
		f_oldSystem = CoreInterface.getSystem();
		CoreInterface.setSystem(mock(ICoreInterface.class));
		given(CoreInterface.getSystem().getLog()).willReturn(mock(Log.class));
		f_testData = new MockAbstractBlockData();
		f_testData2 = new MockAbstractBlockData();
		f_testData3 = new MockAbstractBlockData();
		
		f_testNoSelectData = new AbstractBlockData() {
			
			@Override
			public String tableName() {
				return "MockTableNoSelect";
			}
			
			@Override
			public long keyFields() {
				return 0;
			}
			
			@Override
			public String[] getFieldNames() {
				return new String[] { "First", "Second", "Third", "Fourth", "Fifth", "Sixth" };
			}
			
			@Override
			public void autoIncrementField(int p_key, long p_id) {
			}
			
			/* (non-Javadoc)
			 * @see com.airsltd.core.data.AbstractBlockData#deleteCheckClause(java.lang.String)
			 */
			@Override
			protected String deleteCheckClause(String p_destTableName) {
				return (p_destTableName==null || p_destTableName.isEmpty()?
						super.deleteCheckClause(p_destTableName):p_destTableName+".")+"deleteCheck";
			}

			@Override
			public void copy(IBlockData p_newData) {
			}

			@Override
			public IBlockData copy() {
				return null;
			}

			@Override
			public IBlockFieldFactory[] fieldFactories() {
				return MOCKBLOCKSNO;
			}

			@Override
			public int compareTo(AbstractBlockData p_o) {
				// TODO Auto-generated method stub
				return 0;
			}

		};
	}

	@After
	public void tearDown() throws Exception {
		CoreInterface.setSystem(f_oldSystem);
	}

	@Test
	public final void testInsertHead() {
		//given
		//when
		//then
		assertEquals("", f_testData.insertHead(0));
		assertEquals("(`First`)", f_testData.insertHead(1));
		assertEquals("(`First`, `Third`)", f_testData.insertHead(5));
		assertEquals("(`First`, `Second`, `Third`, `Fourth`, `Fifth`, `Sixth`)", f_testData.insertHead(-1));
	}
	
	@Test
	public final void testInsertValues() {
		//given
		//when
		//then
		assertEquals("()", f_testData.insertValues(0));
		assertEquals("(field0)", f_testData.insertValues(1));
		assertEquals("(field0, field2)", f_testData.insertValues(5));
		assertEquals("(field0, field1, field2, field3, field4, field5)", f_testData.insertValues(-1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testModifyBody() {
		// given
		
		// when
		// then
		assertEquals("", f_testData.modifyBody(f_testData2));
		
		// when
		((IBlockField<String>)f_testData2.toField(0)).setValue("test");
		// then
		assertEquals("SET `First`=test", f_testData.modifyBody(f_testData2));

		// given
		((IBlockField<String>)f_testData2.toField(2)).setValue("test2");
		// when
		// then
		assertEquals("SET `First`=test, `Third`=test2", f_testData.modifyBody(f_testData2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testUpdateProperties() {
		// given
		
		// when
		// then
		assertEquals(null, f_testData.updateProperties(f_testData2));
		
		// given
		((IBlockField<String>)f_testData2.toField(0)).setValue("test");
		// when
		// then
		List<String> l_testList = new ArrayList<String>();
		l_testList.add("First");
		assertEquals(l_testList, f_testData.updateProperties(f_testData2));

		// given
		((IBlockField<String>)f_testData2.toField(2)).setValue("test");
		// when
		// then
		l_testList.add("Third");
		assertEquals(l_testList, f_testData.updateProperties(f_testData2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void testModified() {
		// given
		// when
		// then
		assertEquals(0, f_testData.modified(f_testData2));
		
		// given
		((IBlockField<String>)f_testData2.toField(0)).setValue("test");
		// when
		// then
		assertEquals(1, f_testData.modified(f_testData2));

		// given
		((IBlockField<String>)f_testData2.toField(2)).setValue("test");
		// when
		// then
		assertEquals(5, f_testData.modified(f_testData2));

		
	}

	@Test
	public final void testSelectClause() {
		// given
		// when
		assertEquals("`First`=field0 AND `Second`=field1", f_testData.selectClause(null, false));
		assertEquals("`First`=field0 AND `Second`=field1", f_testData.selectClause(null, true));
		assertEquals("`First`=field0 AND `Second`=field1", f_testData.selectClause("", false));
		assertEquals("`First`=field0 AND `Second`=field1 AND .deleteCheck", f_testData.selectClause("", true));
		assertEquals("mock1.`First`=field0 AND mock1.`Second`=field1", f_testData.selectClause("mock1", false));
		assertEquals("mock1.`First`=field0 AND mock1.`Second`=field1 AND mock1.deleteCheck", f_testData.selectClause("mock1", true));

		assertEquals("", f_testNoSelectData.selectClause(null, false));
		assertEquals("deleteCheck", f_testNoSelectData.selectClause(null, true));
		assertEquals("", f_testNoSelectData.selectClause("", false));
		assertEquals("deleteCheck", f_testNoSelectData.selectClause("", true));
		assertEquals("", f_testNoSelectData.selectClause("mock1", false));
		assertEquals("mock1.deleteCheck", f_testNoSelectData.selectClause("mock1", true));
		// then

		
		
	}

	@Test
	public final void testKeyJoin() {
		// given
		// when
		// then
		assertEquals("ON dest.`First`=src.`First` AND dest.`Second`=src.`Second`", f_testData.keyJoin());
		assertEquals("", f_testNoSelectData.keyJoin());
	}

	@Test
	public final void testFieldUpdates() {
		// given
		// when
		// then
		assertEquals("dest.`First`=src.`First`", f_testData.fieldUpdates(1));
		assertEquals("dest.`Second`=src.`Second`, dest.`Third`=src.`Third`", f_testData.fieldUpdates(6));
		assertEquals("", f_testData.fieldUpdates(0));
	}

	@Test
	public final void testAutoIncrements() {
		assertEquals(0, f_testData.autoIncrements());
	}
	
	@Test
	public final void testInsertValueCallBack() {
		// given
		// when
		// then
		try {
			assertEquals(5, f_testData.insertValueCallBack(0, 5, null));
			assertEquals(10, f_testData.insertValueCallBack(0, 10, null));
		} catch (SQLException e) {
		}
	}
	
	@Test
	public final void testHashCode() {
		// given
		// when
		// then
		assertEquals("MockTable".hashCode() ^ "field0".hashCode() ^ "field1".hashCode(), f_testData.hashCode());
	}
	
	@Test
	public final void testEqualsObject() {
		// given
		// when
		f_testData3.f_data[1]="not the same";
		// then
		assertFalse(f_testData.equals(null));
		assertTrue(f_testData.equals(f_testData));
		assertTrue(f_testData.equals(f_testData2));
		assertFalse(f_testData.equals(f_testData3));
		assertFalse(f_testData.equals(f_testNoSelectData));
	}
	
	@Test
	public final void testToStringCsv() {
		// given
		// when
		// then
		assertArrayEquals(new String[] { "field0",  "field1", "field2", "field3", "field4", "field5"}, f_testData.toStringCsv());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testFieldEqual() {
		// given
		IBlockData l_newData = mock(IBlockData.class);
		given(l_newData.toField(anyInt())).willReturn(mock (IBlockField.class));
		// when
		
		// then
		assertTrue(f_testData.fieldEqual(-1, null));
		assertFalse(f_testData.fieldEqual(0, null));
		assertFalse(f_testData.fieldEqual(6, l_newData));
		
	}
	
	@Test
	public final void testToFieldInt() {
		// given
		// when
		// then
		assertNull(f_testData.toField(-1));
		assertNull(f_testData.toField(6));
		assertTrue(f_testData.toField(0) instanceof MockBlockField);
	}
	
	@Test
	public final void testToSqlValue() {
		// given
		// when
		// then
		assertEquals("", f_testData.toSqlValue(-1));
		assertEquals("field0", f_testData.toSqlValue(0));
	}
	
	@Test
	public final void testFromStringCsv() {
		// given
		// when
		f_testData.fromStringCsv(new String[] {"onecsv", "twocsv", "threecsv", "fourcsv", "fivecsv", "throwParseException"});
		// then
		assertEquals("onecsv", f_testData.fieldValue(0));
		assertEquals("twocsv", f_testData.fieldValue(1));
		assertEquals("threecsv", f_testData.fieldValue(2));
		assertEquals("fourcsv", f_testData.fieldValue(3));
		assertEquals("fivecsv", f_testData.fieldValue(4));
		assertEquals("field5", f_testData.fieldValue(5));
		
	}
	
	@Test
	public final void testModifyFieldValue() {
		// given
		// when
		f_testData.modifyFieldValue(0,"onecsv");
		f_testData.modifyFieldValue(1,"twocsv");
		f_testData.modifyFieldValue(2,"threecsv");
		f_testData.modifyFieldValue(3,"fourcsv");
		f_testData.modifyFieldValue(4,"fivecsv");
		f_testData.modifyFieldValue(5,"sixcsv");
		f_testData.modifyFieldValue(6,"nosuchfield");
		// then
		assertEquals("onecsv", f_testData.fieldValue(0));
		assertEquals("twocsv", f_testData.fieldValue(1));
		assertEquals("threecsv", f_testData.fieldValue(2));
		assertEquals("fourcsv", f_testData.fieldValue(3));
		assertEquals("fivecsv", f_testData.fieldValue(4));
		assertEquals("sixcsv", f_testData.fieldValue(5));
		assertEquals(null, f_testData.fieldValue(6));
		
	}
	
	@Test
	public final void testHandleParseException() {
		// provided for code coverage as this hook does nothing
		// given
		// when
		f_testData.handleParseException(0, "A string", mock(ParseException.class));
		// then
	}
	
	@Test
	public final void testToString() {
		// given
		MockData3 l_mockData = new MockData3(5, "Test");
		// when
		// then
		assertEquals("mockTable [FieldOne=5, FieldTwo=Test]", l_mockData.toString());
	}
	
	@Test
	public final void testExactMatch() {
		// given
		MockData3 l_mockData = new MockData3(5, "Test");
		MockData3 l_mockData2 = new MockData3(5, "Test");
		// when
		// then
		assertTrue(l_mockData.exactMatch(l_mockData2));
		assertFalse(l_mockData.exactMatch(mock(IBlockData.class)));
		// when
		l_mockData2.setDescription("Another");
		assertTrue(l_mockData.equals(l_mockData2));
		assertFalse(l_mockData.exactMatch(l_mockData2));
	}
}
