/**
 *
 */
package com.airsltd.core.lisp.function.logical;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Xor extends AbstractFunction {

	private static final int MINARGUMENTS = 2;

	public Xor() {
		super("XOR");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() < MINARGUMENTS) {
			throw new InvalidParameters("XOR needs two or more arguments");
		}
		final List<ISimpleValue> l_translatedValues = translate(Type.BOOLEAN, p_arguments, p_context);
		boolean l_value = Type.TRUE.equals(l_translatedValues.get(0));
		for (final ISimpleValue l_currentValue : l_translatedValues.subList(1, l_translatedValues.size())) {
			l_value ^= Type.TRUE.equals(l_currentValue);
		}
		return l_value ? Type.TRUE : Type.FALSE;
	}

	@Override
	public String getFunctionName() {
		return "Xor";
	}

}
