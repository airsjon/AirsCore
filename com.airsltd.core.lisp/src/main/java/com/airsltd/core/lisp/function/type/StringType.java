/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;

/**
 * Converter for any object to a String type.
 *
 * @author Jon Boley
 *
 */
public class StringType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		ISimpleValue l_value = p_value;
		if (p_value.getType(p_context) != Type.STRING) {
			l_value = new SimpleValue(p_value.getValue(p_context).toString(), Type.STRING);
		}
		return l_value;
	}

}
