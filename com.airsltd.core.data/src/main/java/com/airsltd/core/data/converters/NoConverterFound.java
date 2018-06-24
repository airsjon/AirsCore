/**
 *
 */
package com.airsltd.core.data.converters;

/**
 * @author Jon Boley
 *
 */
public class NoConverterFound extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 9168621595310238312L;

	public NoConverterFound(Class<?> p_class) {
		super("Unable to find converter for class: " + p_class.getName());
	}

}
