package hr.fer.zemris.java.custom.collections;

/**
 * Collection is a class that represents some general collection of objects.
 * None of the methods, except addAll(), are implemented. Class has a default
 * constructor.
 * 
 * @author juren
 */
public class Collection {

	/**
	 * Checks whether the collection is empty or not, returns corresponding boolean
	 * result.
	 * 
	 * @return Returns true if collection contains no objects and false otherwise.
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * Returns the number of values stored in Collection. Here implemented to return
	 * 0.
	 * 
	 * @return zero
	 */
	public int size() {
		return 0;
	}

	/**
	 * Method used to store value into Collection. Here implemented to do nothing.
	 * 
	 * @param value to be added
	 */
	public void add(Object value) {

	}

	/**
	 * Method used to check presence of value in collection. Here implemented to
	 * return false.
	 * 
	 * @param value searched for in collection
	 * @return boolean representing presence in collection
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Method that removes object out of collection based on equals method. Returns
	 * true only if the collection contains given value (as determined by equals
	 * method) and removes one(first) occurrence of it. Here implemented to return
	 * false.
	 * 
	 * @param value that should be removed
	 * @return false
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Method used to allocate and return new array of Objects with size that is
	 * equal to the size of this collection. Then fills the new array with
	 * collection content (Objects) and returns reference to it. Here implemented to
	 * throw UnsupportedOperationException.
	 * 
	 * @return not implemented yet.
	 * @throws UnsupportedOperationException because it's not implemented yet
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Method that calls processor.process(value) for each value of collection. Here
	 * implemented to do nothing.
	 * 
	 * @param processor model of an object capable of performing some operation on
	 *                  the passed object.
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Method that adds all elements from referenced collection into the current
	 * collection.
	 * 
	 * @param other collection which we want to copy from
	 * @throws NullPointerException if provided collection is null reference
	 */
	public void addAll(Collection other) {
		if (other == null) {
			new NullPointerException();
		}
		
		/**
		 * Local class that extends Processor and overrides it's process method. Used to
		 * add elements to collection.
		 */
		class ProcessorImpl extends Processor {
			
			/**
			 * Method that processes given object by adding it to a Collection
			 */
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		other.forEach(new ProcessorImpl());
	}

	/**
	 * Method that removes all elements from collection. Here implemented to do
	 * nothing.
	 */
	public void clear() {
	}

	// If this(below) section of code gets uncommented, in ObjectStack we can choose
	// whether we want to use
	// LinkedListIndexedCollection or ArrayIndexedCollection just by changing "new
	// ArrayIndexedCollection()" with "new LinkedListIndexedCollection", because
	// then the type of storage variable can be Collection
	// therefore

	// public Object get(int index) {
	// return null;
	// }
	//
	// public void remove(int index) {
	// }

}
