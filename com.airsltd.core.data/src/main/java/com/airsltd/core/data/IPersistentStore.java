package com.airsltd.core.data;

/**
 * Class that implement this interface provide persistent storage of the Data T.
 *
 * @author Jon
 *
 * @param <T>
 */
public interface IPersistentStore<T> {

	/**
	 * Load the persistent content associated with the key values in data. This
	 * allows the model to return the current item that would be associated with
	 * this data.
	 *
	 * @param data
	 *            the value that is being loaded
	 * @param update
	 *            informs the store system to update the data with the values
	 *            from the data argument
	 * @return one of three values<br>
	 *         {@code null} -> the data was loaded from the store, and the data
	 *         was updated by the load (this is useful for stores whose model
	 *         data is automatically update when the data is loaded) by
	 *         returning null the data system knows that nothing has to be
	 *         updated<br>
	 *         {@code data} -> returned when there was no corresponding value in
	 *         the store for this {@code data} so the store needs to be updated
	 *         <br>
	 *         {@code a new object of type <T>} -> represents the value in the
	 *         store for this object and also no need for any updating
	 */
	T loadContent(final T data, boolean update);

	/**
	 * Add the data T into the persistent store. If the data exists but the key
	 * value is -1 then the key value will be updated to the actual value in the
	 * database. (Note: if the data that this store is persisting implements
	 * IPersistentID, then this store is guaranteed to update the ID in both
	 * standard add and block add.)
	 *
	 * @param data
	 * @return true if the content was added, false if not.
	 */

	boolean addContent(final T data);

	/**
	 * Remove the data T from the persistent store
	 *
	 * @param data
	 * @return true if the content was removed, false if not.
	 */

	boolean removeContent(final T data);

	/**
	 * Modify the data oldData with the data from newData
	 *
	 * @param oldData
	 * @param newData
	 * @return true if the content was updated in the persistent store
	 */
	boolean updateContent(T oldData, final T newData);

	/**
	 * Is the current data allowed to be editted. Over ride this method to put a
	 * read only check on data before a user is allowed to edit it.
	 *
	 * @param data
	 * @return true if the data can be editted
	 */
	boolean canEdit(final T data);
}
