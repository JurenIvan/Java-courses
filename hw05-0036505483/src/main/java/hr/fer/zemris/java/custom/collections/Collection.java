package hr.fer.zemris.java.custom.collections;

/**
 * Collection is a generic interface that represents list of operations supported by
 * some general collection of generic objects.
 * 
 * @author juren
 */

public interface Collection<T> {

	/**
	 * Returns the number of values stored in Collection.
	 * 
	 * @return size of collection
	 */
	int size();

	/**
	 * Method used to store value into Collection.
	 * 
	 * @param value to be added
	 */
	void add(T value);

	/**
	 * Method used to check presence of value in collection.
	 * 
	 * @param value searched for in collection
	 * @return boolean representing presence in collection
	 */
	boolean contains(Object value);

	/**
	 * Method that removes object out of collection based on equals method. Returns
	 * true only if the collection contains given value (as determined by equals
	 * method) and removes one(first) occurrence of it.
	 * 
	 * @param value object that should be removed
	 * @return boolean representation of presentness of Object value in given
	 *         collection
	 */
	boolean remove(Object value);

	/**
	 * Method used to allocate and return new array of objects with size that is
	 * equal to the size of this collection. Then fills the new array with
	 * collection content (Objects) and returns reference to it.
	 * 
	 * @return array of Objects.
	 */
	T[] toArray();

	/**
	 * Method that removes all elements from collection.
	 */
	void clear();

	/**
	 * Method used to create generic instance of private static class in generic collection which
	 * provides iterator-like capabilities
	 * 
	 * @return ElementGetter object that has two methods used to iterate through
	 *         collection
	 * 
	 */
	ElementsGetter<T> createElementsGetter();

	/**
	 * Checks whether the collection is empty or not, returns corresponding boolean
	 * result.
	 * 
	 * @return Returns true if collection contains no objects and false otherwise.
	 */
	default boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * Method that adds all elements from referenced generic collection into the current
	 * collection.
	 * 
	 * @param other collection which we want to copy from
	 * @throws NullPointerException if provided collection is null reference
	 */
	default void addAll(Collection<? extends T> other) {
		if (other == null) {
			new NullPointerException();
		}
		other.forEach((value) -> add(value));
	}

	/**
	 * Method that adds all elements from generic referenced collection that satisfies
	 * criteria in {@link Tester} into the current collection.
	 * 
	 * @param col    collection which we want to copy from
	 * @param tester functional interface with criteria that elements has to pass to
	 *               be copied into collection
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		col.forEach((other) -> {
			if (tester.test(other))
				this.add(other);
		});
	}

	/**
	 * Method that calls processor.process(value) for each value of collection.
	 * 
	 * @param processor model of an object capable of performing some operation on
	 *                  the passed object.
	 */
	default void forEach(Processor<T> processor) {
		ElementsGetter<T> getter = createElementsGetter();

		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}

}
