/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data is stored in a tree.
 * <p>
 * Objects that have can be used to select elements from the tree must implement ITreeParent.
 * Objects in the tree must implement ITreeChild.
 * 
 * @author Jon Boley
 *
 */
public abstract class TreeModel<U extends ITreeChild<U>, T extends ITreeParent<U>> {

	/**
	 * The heads of the trees.
	 */
	private List<T> f_heads = new ArrayList<T>();

	/**
	 * Default constructor.
	 */
	public TreeModel() {
	}

	/**
	 * Load the model from p_selector.
	 * 
	 * @param p_selector
	 * @return
	 */
	public abstract boolean loadModel(Object p_selector);

	/**
	 * Return all the roots after loading the model based on p_inputElement.
	 * 
	 * @param p_inputElement  can be null, an object that aids the model in loading the trees
	 * @return a list of all the roots
	 */
	public List<T> getElements(Object p_inputElement) {
		if (p_inputElement != null && getHeads().isEmpty()) {
			loadModel(p_inputElement);
		}
		return p_inputElement == null ? new ArrayList<T>() : getHeads();
	}

	/**
	 * Get a list of all the tree root's stored in this model.
	 * 
	 * @return
	 */
	public List<T> getHeads() {
		return f_heads;
	}

	/**
	 * Set the heads of all the Trees.
	 * <p>
	 * Primarily used for testing purposes.
	 * 
	 * @param p_heads  not null, a List of all the root nodes.
	 */
	public void setHeads(List<T> p_heads) {
		f_heads = p_heads;
	}

}
