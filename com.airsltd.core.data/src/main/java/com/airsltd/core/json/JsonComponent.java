/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;
import com.google.gson.JsonObject;

/**
 * @author Jon Boley
 *
 */
public class JsonComponent {

	private SimpleDateFormat f_dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @return the dateFormat
	 */
	public SimpleDateFormat getDateFormat() {
		return f_dateFormat;
	}

	/**
	 * @param p_dateFormat
	 *            the dateFormat to set
	 */
	public void setDateFormat(SimpleDateFormat p_dateFormat) {
		f_dateFormat = p_dateFormat;
	}

	public String convertType(JsonObject p_jsonObject, String p_string) {
		final String l_retVal = convertString(p_jsonObject, p_string);
		return l_retVal.length() > 0 ? l_retVal.substring(0, 1) : "";
	}

	public Date convertDate(JsonObject p_jsonObject, String p_string) {
		Date l_retVal = null;
		final String l_parse = convertString(p_jsonObject, p_string);
		try {
			l_retVal = l_parse.isEmpty() ? null : f_dateFormat.parse(l_parse);
		} catch (final ParseException exception) {
			CoreInterface.getSystem().handleException(
					"JSON Date Conversion Failure [" + p_string + ", " + l_parse + "]", exception,
					NotificationStatus.LOG);
		}
		return l_retVal;
	}

	public float convertFloat(JsonObject p_jsonObject, String p_string) {
		float rVal = 0f;
		if (p_jsonObject.has(p_string) && p_jsonObject.get(p_string).isJsonPrimitive()) {
			try {
				rVal = p_jsonObject.get(p_string).getAsFloat();
			} catch (final NumberFormatException l_nfe) {
				CoreInterface.getSystem().handleException("JSON Float Conversion Failure [" + p_string + ", "
						+ p_jsonObject.get(p_string).getAsString() + "]", l_nfe, NotificationStatus.LOG);
			}
		}
		return rVal;
	}

	public String convertString(JsonObject p_jsonObject, String p_string) {
		return p_jsonObject.has(p_string) && p_jsonObject.get(p_string).isJsonPrimitive()
				? p_jsonObject.get(p_string).getAsString() : "";
	}

	public int convertInt(JsonObject p_jsonObject, String p_string) {
		int rVal = 0;
		if (p_jsonObject.has(p_string) && p_jsonObject.get(p_string).isJsonPrimitive()) {
			try {
				rVal = p_jsonObject.get(p_string).getAsInt();
			} catch (final NumberFormatException l_nfe) {
				CoreInterface.getSystem().handleException("JSON Integer Conversion Failure [" + p_string + ", "
						+ p_jsonObject.get(p_string).getAsString() + "]", l_nfe, NotificationStatus.LOG);
			}
		}
		return rVal;
	}

	public boolean convertBoolean(JsonObject p_jsonElements, String p_string) {
		return "true".equals(convertString(p_jsonElements, p_string));
	}

}
