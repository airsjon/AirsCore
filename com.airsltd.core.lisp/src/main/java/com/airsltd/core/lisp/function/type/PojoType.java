/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;

/**
 * @author Jon Boley
 *
 */
public class PojoType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		final ISimpleValue l_value = p_value;
		if (p_value.getType(p_context) != Type.POJO) {
			return Type.functionPojo(p_value.getValue(p_context));
		}
		return l_value;
	}

}
