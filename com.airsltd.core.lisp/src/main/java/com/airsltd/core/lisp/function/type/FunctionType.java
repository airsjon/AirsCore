/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;

/**
 * @author jon_000
 *
 */
public class FunctionType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		ISimpleValue l_retVal = p_value;
		final Type l_type = p_value.getType(p_context);
		switch (l_type) {
		case STRING:
			l_retVal = Type.function(AbstractFunction.getFunction((String) l_retVal.getValue(p_context)));
			break;
		case FUNCTION:
			break;
		default:
			throw new InvalidArgument("Unable to convert to a Function");
		}
		return l_retVal;
	}

}
