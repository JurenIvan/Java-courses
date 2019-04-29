package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * The class which represents a structure that maps keys to values. Every pair
 * of key-value is stored in special private class, and retrieving data is of
 * complexity O(n).
 * 
 * @author juren
 *
 * @param <K> type of key
 * @param <V> type of value
 */
public class Dictionary<K, V> {

	/**
	 * {@link ArrayIndexedCollection} where pairs of data is stored
	 */
	private ArrayIndexedCollection<Pair<K, V>> dictionary;

	/**
	 * Special private class that is used to store data (pairs of keys and values).
	 * 
	 * @author juren
	 *
	 * @param <K> type of key
	 * @param <V> type of value
	 */
	private static class Pair<K, V> {
		/**
		 * used to store key.
		 */
		private K key;
		/**
		 * used to store value.
		 */
		private V value;

		/**
		 * Constructor for Pair class. Takes key and value. Key can not be null.
		 * 
		 * @throws NullPointerException if key is null.
		 */
		public Pair(K key, V value) {
			Objects.requireNonNull(key);
			this.key = key;
			this.value = value;
		}

		/**
		 * Standard getter for key.
		 * 
		 * @return key
		 */
		public K getKey() {
			return this.key;
		}

		/**
		 * Standard getter for value.
		 * 
		 * @return value
		 */
		public V getValue() {
			return this.value;
		}
	}

	/**
	 * Default dictionary constructor.
	 */
	public Dictionary() {
		this.dictionary = new ArrayIndexedCollection<>();
	}

	/**
	 * Method that returns boolean if {@link Dictionary} contains zero entries.
	 * 
	 * @return boolean representing emptiness of {@link Dictionary}
	 */
	public boolean isEmpty() {
		return dictionary.size() == 0;
	}

	/**
	 * Method that returns number of elements currently stored in {@link Dictionary}
	 * 
	 * @return number of elements
	 */
	public int size() {
		return dictionary.size();
	}

	/**
	 * Method that clears storage of {@link Dictionary} and deletes all entries.
	 */
	public void clear() {
		dictionary.clear();
	}

	/**
	 * Method that puts data into {@link Dictionary}. Key must not be null.
	 * 
	 * @param key   must not be null, if not unique, old value is overwritten.
	 * @param value that will be stored in pair with key.
	 * 
	 * @throws NullPointerException if key provided is null reference
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);
		int indexOfPair = getPairIndex(key);
		if (indexOfPair != -1) {
			dictionary.remove(indexOfPair);
		}
		dictionary.add(new Pair<>(key, value));
	}

	/**
	 * Method that returns value stored in {@link Dictionary} under the provided key
	 * If no such value is found or key provided is null, method returns null.
	 * 
	 * @param key
	 * @return value stored alongside key value
	 * 
	 */
	public V get(Object key) {
		if (key == null)
			return null;
		int indexOfPair = getPairIndex(key);
		if (indexOfPair == -1)
			return null;
		return dictionary.get(indexOfPair).getValue();
	}

	/**
	 * Method that returns index of pair stored in collection with given key. If no
	 * such key is found, method returns -1.
	 * 
	 * @param key of pair that is searched for
	 * @return index of array where searched pair is stored
	 */
	private int getPairIndex(Object key) {
		for (int i = 0; i < dictionary.size(); i++) {
			if (dictionary.get(i).getKey().equals(key)) {
				return i;
			}
		}
		return -1;
	}

}
