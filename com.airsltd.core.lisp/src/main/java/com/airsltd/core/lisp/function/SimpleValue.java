/**
 *
 */
package com.airsltd.core.lisp.function;

import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class SimpleValue implements ISimpleValue {

	private Object f_value;
	private Type f_type;

	public SimpleValue(Object p_value, Type p_type) {
		f_value = p_value;
		f_type = p_type;
	}

	public SimpleValue() {
		f_value = null;
		f_type = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IValue#getValue(java.util.List)
	 */
	@Override
	public final Object getValue(EvaluationContext p_context) {
		return f_value;
	}

	/**
	 * @param p_value
	 *            the value to set
	 */
	public void setValue(Object p_value) {
		f_value = p_value;
	}

	/**
	 * @param p_type
	 *            the type to set
	 */
	public void setType(Type p_type) {
		f_type = p_type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IValue#getType(java.util.List)
	 */
	@Override
	public final Type getType(EvaluationContext p_context) {
		return f_type;
	}

	@Override
	public ISimpleValue evaluate(EvaluationContext p_context) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return f_value != null ? f_value.toString() : "null";
	}

	public boolean lispEquals(Object p_arg0, EvaluationContext p_context) {
		boolean l_retVal = p_arg0 instanceof SimpleValue;
		if (l_retVal) {
			final SimpleValue l_arg0 = (SimpleValue) p_arg0;
			l_retVal = getType(p_context) == l_arg0.getType(p_context) && (getValue(p_context) == null
					? l_arg0.getValue(p_context) == null : getValue(p_context).equals(l_arg0.getValue(p_context)));
		}
		return l_retVal;
	}

	@Override
	public boolean equals(Object p_arg0) {
		return lispEquals(p_arg0, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return f_type.hashCode() + f_value.hashCode();
	}

	@Override
	public <T> T as(Class<T> p_class, EvaluationContext p_context) {
		return f_type == Type.POJO ? p_class.cast(f_value) : Type.toType(p_class, this, p_context);
	}

}
