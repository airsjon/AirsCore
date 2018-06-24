/**
 *
 */
package com.airsltd.core.lisp.function;

/**
 * @author Jon Boley
 *
 */
public abstract class LispValue implements ISimpleValue {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.ISimpleValue#getValue(com.airsltd.
	 * aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public Object getValue(EvaluationContext p_context) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.ISimpleValue#evaluate(com.airsltd.
	 * aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue evaluate(EvaluationContext p_context) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.ISimpleValue#as(java.lang.Class,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public <T> T as(Class<T> p_class, EvaluationContext p_context) {
		throw new UnsupportedOperationException("Lisp objects can not be cast to Java types.");
	}

}
