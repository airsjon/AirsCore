/**
 *
 */
package com.airsltd.core.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field that is AirsPersistant will be stored by BlockProvider.
 * <p>
 *
 * @author Jon Boley
 *
 * @see AbstractBlockData
 * @see BlockProvider
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AirsPersistentField {

	/**
	 * The index of the persisted data in the store.
	 * <p>
	 * This value can be -1, which means to take the current index, use it, and
	 * increment. If a value different than -1 is provided, this value is used
	 * and current index is set to the new value + 1.
	 * <p>
	 * Defaults to -1.
	 *
	 * @return the index to be used to access and store the value.
	 */
	int index() default -1;

	/**
	 * The name of the field in the database.
	 * <p>
	 * If the field is empty, the name of the field variable is used (with f_
	 * being dropped).
	 * 
	 * @return
	 */
	String fieldName() default "";

	/**
	 * Used by String fields to set a maximum size.
	 * <p>
	 * The default value is -1 which means a string of any size.
	 *
	 * @return the allowed size for the string as an integer.
	 */
	int size() default -1;

	/**
	 * A calculated field will never be set since it is calculated.
	 * <p>
	 * Usually the getter will involve calculating the value. A setter does not
	 * need to be defined. If an attempt to modify the value is made an
	 * {@link UnsupportedOperationException} will be thrown.
	 *
	 * @return
	 */
	boolean calculated() default false;

	/**
	 * Override standard setter naming.
	 *
	 * @return
	 */
	String setter() default "";

	/**
	 * Override standard getter naming.
	 *
	 * @return
	 */
	String getter() default "";

	/**
	 * Fields that are read only will be read in by the loader, but never
	 * written.
	 * <p>
	 * This field allows for the special case where a 'calculated' field is
	 * caculated by the SQL statement.
	 */
	boolean readOnly() default false;
}
