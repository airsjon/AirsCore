/**
 *
 */
package com.airsltd.core.lisp.function.math;

import java.util.List;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * For numbers in the range [0, 1] return the threshold.
 * <p>
 * Threshold is defined as { x | x <= .5, 1 -x | x > .5 }. No check is made that
 * the value satisfies the condition that x is in [0, 1].
 *
 * @author Jon Boley
 *
 */
public class Threshold extends MathFunction {

	public Threshold() {
		super("THRESHOLD");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != 1) {
			throw new InvalidParameters(getFunctionName() + " needs one parameter.");
		}
		f_type = Type.FLOAT;
		f_value = .5f;
		final ISimpleValue l_value = p_arguments.get(0).evaluate(p_context);
		f_value = bestClass(l_value, p_context);
		switch (f_type) {
		case FLOAT:
			f_value = processFloat(0f, (float)f_value);
			break;
		case DOUBLE:
			f_value = processDouble(0f, (double)f_value);
			break;
		default:
		}
		return loopFunction(p_arguments, p_context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Threshold";
	}

	@Override
	protected Integer processInteger(int p_value, int p_next) {
		throw new UnsupportedOperationException("Threshold function call Integer not possible?");
	}

	@Override
	protected Float processFloat(float p_value, float p_next) {
		return p_next > .5f ? 1f - p_next : p_next;
	}

	@Override
	protected Double processDouble(double p_value, double p_next) {
		return p_next > .5f ? 1d - p_next : p_next;
	}

	@Override
	protected Long processLong(long p_value, long p_next) {
		throw new UnsupportedOperationException("Threshold function call Long not possible?");
	}

}
