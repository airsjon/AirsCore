/**
 *
 */
package com.airsltd.core.lisp.function;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jon_000
 *
 */
public class EvaluationContext {
	private final EvaluationContext f_parent;
	private final Map<String, ISimpleValue> f_variables = new HashMap<>();

	public EvaluationContext(EvaluationContext p_parent) {
		f_parent = p_parent;
	}

	public ISimpleValue getValue(String p_variableName) {
		ISimpleValue l_retVal = null;
		if (f_variables.containsKey(p_variableName)) {
			l_retVal = f_variables.get(p_variableName);
		} else {
			if (f_parent != null) {
				l_retVal = f_parent.getValue(p_variableName);
			}
		}
		return l_retVal;
	}

	public void setValue(String p_variableName, ISimpleValue p_newValue) {
		if (!internalSetValue(p_variableName, p_newValue)) {
			createValue(p_variableName, p_newValue);
		};
	}

	protected boolean internalSetValue(String p_variableName, ISimpleValue p_newValue) {
		boolean l_retVal = false;
		if (f_variables.containsKey(p_variableName)) {
			f_variables.put(p_variableName, p_newValue);
			l_retVal = true;
		} else {
			l_retVal = f_parent != null && f_parent.internalSetValue(p_variableName, p_newValue);
		};
		return l_retVal;
	}

	public void createValue(String p_variableName, ISimpleValue p_newValue) {
		f_variables.put(p_variableName, p_newValue);
	}

//	public void addContext(String p_realm, RankValue p_value) {
//		ISimpleValue l_value = null;
//		final Object l_inValue = p_value == null ? null : p_value.getValue();
//		switch (p_value.getVariable().getType()) {
//		case LONG:
//			l_value = Type.functionLong(p_value == null ? 0 : (long) l_inValue);
//			break;
//		case INTEGER:
//			if (l_inValue instanceof Long) {
//				final Long l_longValue = (Long) l_inValue;
//				l_value = Type.functionLong(l_longValue);
//			} else {
//				l_value = Type.functionInteger(p_value == null ? 0 : (int) l_inValue);
//			}
//			break;
//		case FLOAT:
//			l_value = Type.functionFloat(p_value == null ? 0f : (float) l_inValue);
//			break;
//		case STRING:
//			l_value = Type.functionString(p_value == null ? "" : (String) l_inValue);
//			break;
//		case DOUBLE:
//			l_value = Type.functionDouble(p_value == null ? 0d : (double) l_inValue);
//			break;
//		default:
//			throw new UnsupportedOperationException(
//					"Rank Variables can store only Longs, Integers, Floats, Doubles or Strings");
//		}
//		createValue(p_realm + p_value.getVariable().getName(), l_value);
//	}
//
//	protected void clearContext(String p_realm, RankVariable p_variable) {
//		final String l_varName = p_realm + p_variable.getName();
//		if (f_variables.containsKey(l_varName)) {
//			f_variables.remove(l_varName);
//		}
//	}
//
//	/**
//	 * Clear the values for the variables in p_variables.
//	 *
//	 * @param p_string
//	 *            not null, header name for the variables
//	 * @param p_variables
//	 *            not null, {@link List} of the variables to be cleared
//	 */
//	public void clearVariables(String p_string, List<RankVariable> p_variables) {
//		for (final RankVariable l_variable : p_variables) {
//			clearContext(p_string, l_variable);
//		}
//	}

	public Set<String> getVariables() {
		final Set<String> l_retVal = f_parent == null ? new HashSet<String>() : f_parent.getVariables();
		l_retVal.addAll(f_variables.keySet());
		return l_retVal;
	}

	public EvaluationContext with(String p_var, ISimpleValue p_value) {
		createValue(p_var, p_value);
		return this;
	}

}
