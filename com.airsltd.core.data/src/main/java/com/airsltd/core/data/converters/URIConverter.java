/**
 *
 */
package com.airsltd.core.data.converters;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;

/**
 * @author Jon Boley
 *
 */
public class URIConverter extends DatabaseConverter<IBlockData, URI> {

	public URIConverter() {
		super(URI.class);
		addConverter(URI.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.converters.IDatabaseConverter#toSql(com.airsltd.
	 * core.data.IBlockData, java.lang.Object)
	 */
	@Override
	public String toSql(IBlockData p_parent, URI p_data) {
		return BlockData.toSql(p_data.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.converters.IDatabaseConverter#fromSql(com.airsltd.
	 * core.data.IBlockData, java.lang.String)
	 */
	@Override
	public URI fromSql(IBlockData p_parent, String p_string) throws ParseException {
		try {
			return new URI(p_string);
		} catch (final URISyntaxException e) {
			final ParseException l_e = new ParseException("Invalid URI string", 0);
			l_e.addSuppressed(e);
			throw l_e;
		}
	}

}
