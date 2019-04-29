package hr.fer.zemris.java.custom.collections;

/**
 * Generic interface that put's together all methods used to store and retrieve
 * data from some kind of List.
 * 
 * @author juren
 *
 * @param <T> generic type of objects stored in collection
 */
public interface List<T> extends Collection<T> {

	/**
	 * Returns the generic object that is stored in list at position index. Valid indexes
	 * are 0 to size-1. If index is invalid IndexOutOfBoundsException() is thrown.
	 * 
	 * Complexity: O(1)
	 * 
	 * @param index of value that should be returned
	 * @return Object stored at that index
	 * @throws IndexOutOfBoundsException() if index is invalid
	 */
	T get(int index);

	/**
	 * Method that inserts value into list at given index. Depending on position it
	 * can throw {@link IndexOutOfBoundsException} or if value is null (not allowed)
	 * it throws {@link NullPointerException}.
	 * 
	 * @param value    Object that should be stored into array
	 * @param position integer referring to a position where we want to insert new
	 *                 value
	 * 
	 * @throws NullPointerException      if null reference is provided as value
	 * @throws IndexOutOfBoundsException if position isn't a number between 0 and
	 *                                   size of collection.
	 */
	void insert(T value, int position);

	/**
	 * Method that searches the collection and returns the index of Object whose
	 * value is equal to value passed as argument. If there is more than one Object
	 * stored with the same value as searched for,one with the lowest/smallest index
	 * is returned. If no such Object is found, method returns -1. Argument can be
	 * null and the result is -1. Objects are compared using .equals() method.
	 * 
	 * Complexity: Average(n/2), O(n)
	 * 
	 * @param value Object that is searched for in Collection
	 * @return integer index of TreeNode with value same as in the argument
	 */
	int indexOf(Object value);

	/**
	 * Removes element at specified index from collection. Element that was
	 * previously at location index+1 after this operation moves index-th location.
	 * 
	 * Legal indexes are 0 to size-1.
	 * 
	 * @param index of element to be removed
	 * @throws IndexOutOfBoundsException if index isn't legal.Legal indexes are 0 to
	 *                                   size-1.
	 */
	void remove(int index);
}
