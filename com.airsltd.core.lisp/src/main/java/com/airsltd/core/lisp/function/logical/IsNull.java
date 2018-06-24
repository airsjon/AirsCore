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
 * Test if the value being passed is null.
 * <p>
 * In Common Lisp this would be nil.
 *
 * @author Jon Boley
 *
 */
public class IsNull extends AbstractFunction {

	public IsNull() {
		super("ISNULL");
	}

	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != 1) {
			throw new InvalidParameters(getFunctionName() + " needs one argument.");
		}
		final ISimpleValue l_valueToTest = p_arguments.get(0).evaluate(p_context);
		return l_valueToTest == null || l_valueToTest.getValue(p_context) == null ? Type.TRUE : Type.FALSE;
	}

	@Override
	public String getFunctionName() {
		return "Null";
	}

}
