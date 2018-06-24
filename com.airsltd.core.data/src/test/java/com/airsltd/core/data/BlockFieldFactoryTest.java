package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockFieldFactoryTest {

	class MockBlockFieldFactory extends BlockFieldFactory<IBlockData,Object> {

		public MockBlockFieldFactory(int p_field,
				IDatabaseConverter<IBlockData, ? super Object> p_converter) {
			super(p_field, p_converter);
		}

		@Override
		public Object value(IBlockData p_blockData) {
			return "test";
		}

		@Override
		public void modifyValue(IBlockData p_blockData, Object p_newValue) {
		}

	}

	private MockBlockFieldFactory f_testFactory;
	private IDatabaseConverter<IBlockData, ? super Object> f_converter;;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		f_converter = mock(IDatabaseConverter.class);
		f_testFactory = new MockBlockFieldFactory(0, f_converter);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testField() {
		// given
		IBlockData l_data = mock(IBlockData.class);
		// when
		IBlockField<Object> l_value = f_testFactory.field(l_data);
		// then
		assertEquals("test",l_value.getValue());
		
	}

}
