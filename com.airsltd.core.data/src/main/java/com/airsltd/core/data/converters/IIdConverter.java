package com.airsltd.core.data.converters;

import com.airsltd.core.data.IPersistentId;

public interface IIdConverter {

	Object getConverter(Class<? extends IPersistentId> p_asSubclass);

}
