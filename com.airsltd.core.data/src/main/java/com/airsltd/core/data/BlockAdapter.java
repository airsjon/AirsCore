/**
 *
 */
package com.airsltd.core.data;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author jon_000
 *
 */
public class BlockAdapter<T extends AbstractBlockData> extends TypeAdapter<T> {

	private final Class<T> f_class;

	public BlockAdapter(Class<T> p_class) {
		super();
		f_class = p_class;
	}

	@Override
	public void write(JsonWriter p_out, T p_value) throws IOException {
		p_out.beginObject();
		int l_index = 0;
		for (final String l_fieldName : p_value.getFieldNames()) {
			final String l_value = p_value.toField(l_index++).toSqlValue();
			p_out.name(l_fieldName).value(BlockData.unquote(l_value));
		}
		p_out.endObject();
	}

	@Override
	public T read(JsonReader p_in) throws IOException {
		p_in.beginObject();
		final List<String> l_fields = new ArrayList<String>();
		final List<String> l_value = new ArrayList<String>();
		while (p_in.hasNext()) {
			l_fields.add(p_in.nextName());
			l_value.add(p_in.nextString());
		}
		p_in.endObject();
		T l_newObject = null;
		try {
			l_newObject = f_class.newInstance();
			processFields(l_newObject, l_fields, l_value);
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new IOException("Unable to instantiate " + f_class, e1);
		}
		return l_newObject;
	}

	private void processFields(T p_newObject, List<String> p_fields, List<String> p_value) throws IOException {
		final List<String> l_fieldNames = Arrays.asList(p_newObject.getFieldNames());
		for (final String l_fieldName : p_fields) {
			final int l_index = l_fieldNames.indexOf(l_fieldName);
			if (l_index >= 0) {
				try {
					p_newObject.toField(l_index).fromSqlValue(p_value.get(l_index));
				} catch (final ParseException e) {
					throw new IOException("Parse error on field: " + l_fieldName + " on input: " + p_value.get(l_index),
							e);
				}
			} else {
				throw new IOException("Invalid field [" + l_fieldName + "] in JSON description of " + f_class);
			}
		}
	}

}
