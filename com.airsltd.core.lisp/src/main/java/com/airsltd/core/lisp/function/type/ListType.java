/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;

/**
 * @author jon_000
 *
 */
public class ListType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.type.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		final ISimpleValue l_value = p_value;
		if (p_value.getType(p_context) != Type.LIST) {
			throw new InvalidArgument("Unable to convert to a List");
		}
		return l_value;
	}

}
