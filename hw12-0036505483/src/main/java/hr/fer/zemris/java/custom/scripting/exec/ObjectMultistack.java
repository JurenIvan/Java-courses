package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ObjectMultistack class is kind of a map that allows the user to store multiple
 * values for same key and it provides a stack-like abstraction . Keys for
 * ObjectMultistack shall be instances of the class String. Values that will be
 * associated with those keys will be instances of class ValueWrapper
 * 
 * @author juren
 *
 */
public class ObjectMultistack {

	/**
	 * Map storing pairs of keys to "stacks" of data.
	 */
	private Map<String, MultistackEntry> backboneMap;

	/**
	 * Standard constructor.
	 */
	public ObjectMultistack() {
		backboneMap = new HashMap<>();
	}

	/**
	 * Method that pushes provided value onto stack called with keyName in O(1)
	 * complexity.
	 * 
	 * @param keyName      name of virtual stack where value is stored
	 * @param valueWrapper value that needs to be stored.
	 * 
	 * @throws NullPointerException if any of arguments are null. Note that
	 *                              valueWrapper can hold null, but can not be null
	 *                              itself.
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		Objects.requireNonNull(keyName, "Null can not be key");
		Objects.requireNonNull(valueWrapper, "Null can not be stored");
		backboneMap.put(keyName, new MultistackEntry(valueWrapper, getTopOfStack(keyName)));
	}

	/**
	 * Method that retrieves and removes value stored on top of stack called keyName
	 * 
	 * @param keyName name of virtual stack where value is stored
	 * @return value stored on top of stack called keyName or null if no such exists
	 * @throws EmptyStackException if no values are stored in map binded to key
	 */
	public ValueWrapper pop(String keyName) {
		MultistackEntry topOfStack = getTopOfStack(keyName);
		if (topOfStack == null)
			throw new EmptyStackException();

		if (topOfStack.getNext() == null) {
			backboneMap.remove(keyName);
		} else {
			backboneMap.put(keyName, topOfStack.getNext());
		}
		return topOfStack.getValue();
	}

	/**
	 * Method that retrieves value stored on top of stack called keyName
	 * 
	 * @param keyName name of virtual stack where value is stored
	 * @return value stored on top of stack called keyName or null if no such exists
	 * @throws EmptyStackException if no values are stored in map binded to key
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry topOfStack = getTopOfStack(keyName);
		if (topOfStack == null)
			throw new EmptyStackException();

		return topOfStack.getValue();
	}

	/**
	 * Method that returns {@link MultistackEntry} that is stored in
	 * {@link #backboneMap} map on first place
	 * 
	 * @param keyName key to find value in map
	 * @return {@link MultistackEntry} on top of map, or null if no such value is
	 *         found
	 */
	public MultistackEntry getTopOfStack(String keyName) {
		if (keyName == null)
			return null;
		return backboneMap.get(keyName);
	}

	/**
	 * Method that returns boolean representing emptiness of stack called keyName
	 * 
	 * @param keyName name of virtual stack that we want to check
	 * @return boolean representing emptiness of stack called keyName
	 * @throws NullPointerException if keyname is null;
	 */
	public boolean isEmpty(String keyName) {
		Objects.requireNonNull(keyName, "Null can not be key");
		return backboneMap.get(keyName) == null;
	}

	/**
	 * Class that Models node in stack like structure. Has data container and
	 * reference to next Node.
	 * 
	 * @author juren
	 *
	 */
	static class MultistackEntry {
		/**
		 * Variable that stores data
		 */
		private ValueWrapper value;
		/**
		 * Variable that stores reference to next Node
		 */
		private MultistackEntry next;

		/**
		 * @return the value
		 */
		public ValueWrapper getValue() {
			return value;
		}

		/**
		 * @return the next
		 */
		public MultistackEntry getNext() {
			return next;
		}

		/**
		 * Standard constructor that models basic Node
		 * 
		 * @param value stored in node
		 * @param next  reference to next node
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			super();
			this.value = value;
			this.next = next;
		}

	}

}
