/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enum's with this annotation are stored as Enum type in MySql.
 * 
 * @author Jon Boley
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AirsEnumNamed {

	/**
	 * Style informs the data system how to convert enum names to database
	 * names.
	 * 
	 * @return
	 */
	FieldStyle style() default FieldStyle.UPPERCASE;

}
