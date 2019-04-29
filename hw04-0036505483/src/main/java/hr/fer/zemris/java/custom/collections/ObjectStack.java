package hr.fer.zemris.java.custom.collections;

/**
 * ObjectStack class is generic implementation of stack which uses
 * ArrayIndexedCollection to store data. Class represents example of adaptor
 * used in design pattern. Each class instance manages it's own storage and
 * gives user interface to use it properly.
 * 
 * More info on Adaptors: en.wikipedia.org/wiki/Adapter_pattern
 * 
 * @author juren
 */
public class ObjectStack<T> {

	private ArrayIndexedCollection<T> storage;

	/**
	 * Default constructor. As storage backbone, {@link ArrayIndexedCollection} is
	 * used.
	 */
	public ObjectStack() {
		storage = new ArrayIndexedCollection<T>();
	}

	/**
	 * Method checks whether the stack is empty or not
	 * 
	 * @return boolean resembling emptiness of stack
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}

	/**
	 * Method that returns number of Objects currently stored on stack
	 * 
	 * @return integer number determining quantity of objects on stack
	 */
	public int size() {
		return storage.size();
	}

	/**
	 * Method that adds referenced object to the top of stack in O(1) complexity.
	 * 
	 * @param value to be added on stack
	 * @throws NullPointerException if null is referenced as value
	 */
	public void push(T value) {
		storage.add(value);
	}

	/**
	 * Method that retrieves Object that was added to the stack last (that is on
	 * top of a stack), and removes it from the stack in O(1) complexity.
	 * 
	 * @return Object on top of the stack
	 * @throws EmptyStackException if stack is empty and pop is requested
	 */
	public T pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}

		T result = storage.get(storage.size() - 1);
		storage.remove((int) storage.size() - 1);
		return result;
	}

	/**
	 * Method that retrieves Object that was added to the stack lastly (that was on
	 * top of a stack).
	 * 
	 * @return Object on top of the stack
	 * @throws EmptyStackException if stack is empty but peek is requested
	 */
	public T peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return storage.get(storage.size() - 1);
	}

	/**
	 * Method that cleans/frees whole stack.
	 */
	public void clear() {
		storage.clear();
	}

}
