package com.airsltd.core.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.BlockAdapter;
import com.airsltd.core.data.MockData3;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BlockAdapterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private BlockAdapter<MockData3> f_adapter;

	@Before
	public void setUp() throws Exception {
		f_adapter = new BlockAdapter<MockData3>(MockData3.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteJsonWriter() throws IOException {
		try (// given
				StringWriter l_writer = new StringWriter();
				StringWriter l_writer2 = new StringWriter();
				JsonWriter m_out = new JsonWriter(l_writer);
				JsonWriter m_out2 = new JsonWriter(l_writer2)) {
			// when
			f_adapter.write(m_out, new MockData3(5, "'Test'"));
			f_adapter.write(m_out2, new MockData3(4, "Alf"));
			// then
			assertEquals("{\"FieldOne\":\"5\",\"FieldTwo\":\"'Test'\"}", l_writer.toString());
			assertEquals("{\"FieldOne\":\"4\",\"FieldTwo\":\"Alf\"}", l_writer2.toString());
		};
	}

	@Test
	public void testReadJsonReader() throws IOException {
		try (
			// given
			StringReader l_reader = new StringReader("{\"FieldOne\":\"5\",\"FieldTwo\":\"Test\"}");
			JsonReader l_in = new JsonReader(l_reader)) {
			// when
			MockData3 l_inObject = f_adapter.read(l_in);
			// then
			assertEquals(5, l_inObject.getId());
			assertEquals("Test", l_inObject.getDescription());
		}
	}

	@Test(expected=IOException.class)
	public void testReadJsonReader_BadNumber() throws IOException {
		try (
			// given
			StringReader l_reader = new StringReader("{\"FieldOne\":\"BF\",\"FieldTwo\":\"Test\"}");
			JsonReader l_in = new JsonReader(l_reader)) {
			// when
			f_adapter.read(l_in);
			// then
			fail("Exception not throw when parsing BF as a number");
		}
	}

	@Test(expected=IOException.class)
	public void testReadJsonReader_BadFieldName() throws IOException {
		try (
			// given
			StringReader l_reader = new StringReader("{\"FieldOne\":\"BF\",\"FieldThree\":\"Test\"}");
			JsonReader l_in = new JsonReader(l_reader)) {
			// when
			f_adapter.read(l_in);
			// then
			fail("Exception not throw when parsing BF as a number");
		}
	}

}
