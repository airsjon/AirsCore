/**
 * 
 */
package com.airsltd.core.model;

import java.util.HashMap;
import java.util.Map;

import com.airsltd.core.data.IPersistentId;
import com.airsltd.core.data.converters.IIdConverter;

/**
 * @author Jon Boley
 *
 */
public class PersistentIdConverterFactory implements IIdConverter {

	@SuppressWarnings("rawtypes")
	private Map<Class, Object> f_converters = new HashMap<Class, Object>();
	
	/* (non-Javadoc)
	 * @see com.airsltd.core.data.converters.IIdConverter#getConverter(java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends IPersistentId> Object getInternalConverter(Class<T> p_asSubclass) {
		Object l_retVal = f_converters.get(p_asSubclass);
		if (l_retVal == null) {
			l_retVal = new PersistentIdConverter(p_asSubclass);
			f_converters.put(p_asSubclass,l_retVal);
		}
		return l_retVal;
	}

	@Override
	public Object getConverter(Class<? extends IPersistentId> p_asSubclass) {
		return getInternalConverter(p_asSubclass);
	}

}
