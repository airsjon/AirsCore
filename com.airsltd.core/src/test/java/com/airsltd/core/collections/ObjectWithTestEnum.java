package com.airsltd.core.collections;

import java.util.EnumSet;
import java.util.Set;

public class ObjectWithTestEnum implements IEnumProperty<TestEnum> {

	private Set<TestEnum> f_properties = EnumSet.noneOf(TestEnum.class);
	
	public ObjectWithTestEnum(TestEnum p_first) {
		setProperty(p_first, true);
	}
	
	public ObjectWithTestEnum(TestEnum p_first, TestEnum p_second) {
		setProperty(p_first, true);
		setProperty(p_second, true);
	}

	public ObjectWithTestEnum(boolean p_all) {
		if (p_all) {
			f_properties = EnumSet.allOf(TestEnum.class);
		}
	}

	@Override
	public boolean isProperty(TestEnum p_property) {
		return f_properties.contains(p_property);
	}

	@Override
	public void setProperty(TestEnum p_property, boolean p_value) {
		if (p_value) {
			f_properties.add(p_property);
		} else {
			f_properties.remove(p_property);
		}
	}

	@Override
	public Set<TestEnum> getQuality() {
		return f_properties;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder l_sb = new StringBuilder();
		l_sb.append("{ ObjectWithTestEnum : ");
		for (TestEnum l_property : f_properties) {
			l_sb.append(l_property);
		}
		l_sb.append("}");
		return l_sb.toString();
	}

}
