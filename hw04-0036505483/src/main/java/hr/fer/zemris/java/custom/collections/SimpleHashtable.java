package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class representing implementation of HashMap. Contains a bundle of methods
 * used to manipulate over pairs of data whose key is generic K and value V.
 * 
 * @author juren
 *
 * @param <K> type of Key
 * @param <V> type of Value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Default number of slots of hashmap is {@value #DEFAULT_CAPACITY}. No
	 * particular reasoning behind it. Can be modified.
	 */
	public static final int DEFAULT_SLOT_NUMBER = 16;

	/**
	 * Default maximum load factor of {@link SimpleHashtable}
	 */
	public static final double DEFAULT_LOAD_FACTOR = 0.75;

	/**
	 * Used to store slots where lists of pairs key-value are stored
	 */
	private TableEntry<K, V> slots[];

	/**
	 * Number of elements currently stored
	 */
	private int size;

	/**
	 * Due to importance of not changing collection while iterating through it,
	 * {@link SimpleHashtable} implements counter of made changes.
	 * 
	 * Whenever a new object is added/inserted or an object is removed this variable
	 * changes
	 */
	private long modificationCount;

	/**
	 * Default constructor for {@link SimpleHashtable}. Sets number of slots to
	 * value predefined as constant ( value: {@value #DEFAULT_SLOT_NUMBER}}
	 */
	public SimpleHashtable() {
		this(DEFAULT_SLOT_NUMBER);
	}

	/**
	 * Constructor for {@link SimpleHashtable}. Allows user to manually set number
	 * of slots. Number of slots has to be greater than 1 and less or equal than 2
	 * to the power of 30.
	 * 
	 * @param numberOfSlots sets starting number of slots to first power of 2
	 *                      greater or equal to itself
	 * @throws IllegalArgumentException if provided argument is smaller than 1 or
	 *                                  greater than 2 to the power od 30
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int numberOfSlots) {
		if (numberOfSlots < 1 || numberOfSlots > Integer.MAX_VALUE / 2 + 1) {
			throw new IllegalArgumentException(
					"Starting number of slots has to be positive number smaller or equal to 1073741824.");
		}

		numberOfSlots = getNextPowerOfTwo(numberOfSlots);
		this.slots = new TableEntry[numberOfSlots];
		this.size = 0;
		modificationCount = 0;
	}

	/**
	 * Returns the number of values currently stored in {@link SimpleHashtable}.
	 * 
	 * @return size of collection
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Checks whether {@link SimpleHashtable} contains pair of key-value. Returns
	 * boolean.
	 * 
	 * @param key Object searched for in {@link SimpleHashtable}
	 * @return true if such pair is present, false otherwise
	 */
	public boolean containsKey(Object key) {
		return findTableEntry(key) != null;
	}

	/**
	 * Method used to put pair of key, value into {@link SimpleHashtable}. Values
	 * can repeat, but if two pairs with same key are tried to be added, the first
	 * one gets overwritten.
	 * 
	 * @param key   of pair. Must not be null
	 * @param value of type V that is stored in pair
	 * 
	 * @throws NullPointerException if key is equal to null
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);

		putIntoTable(new TableEntry<>(key, value));

		if (size >= DEFAULT_LOAD_FACTOR * slots.length) {
			doubleTheNumberOfSlots();
		}
	}

	/**
	 * Checks whether the {@link SimpleHashtable} is empty or not, returns
	 * corresponding boolean result.
	 * 
	 * @return Returns true if {@link SimpleHashtable} contains no objects and false
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Implementation of iterator that iterates through {@link SimpleHashtable}.
	 * Iterator is able to remove ONE element(last one what it iterated over). Has
	 * standard hasNext and Next methods alongside already described remove().
	 * 
	 * @author juren
	 *
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>> {
		/**
		 * Stores the count of previous modifications made to collection storing the
		 * elements that we go through
		 */
		private long iteratorModificationCount;
		/**
		 * Stores index of last slot iterator went through
		 */
		private int slotNumberIndex;
		/**
		 * Stores last {@link TableEntry} that it returned
		 */
		private TableEntry<K, V> current;
		/**
		 * Stores pre-calculated next {@link TableEntry}
		 */
		private TableEntry<K, V> nextEntry;

		/**
		 * Default constructor of iterator
		 */
		public IteratorImpl() {
			iteratorModificationCount = modificationCount;
			current = null;
			slotNumberIndex = -1;
			current = null;

			// pleb code
//			for (slotNumberIndex++; slotNumberIndex < slots.length; slotNumberIndex++) {
//				if (slots[slotNumberIndex] != null) {
//					nextEntry = slots[slotNumberIndex];
//					break;
//				}
//			}

			// cool kids code
			for (; slotNumberIndex < slots.length && (nextEntry = slots[++slotNumberIndex]) == null;)
				;
		}

		/**
		 * Method that returns boolean representing availability of next element.
		 * 
		 * @return boolean true if there are more elements in {@link SimpleHashtable},
		 *         false otherwise
		 */
		public boolean hasNext() {
			if (iteratorModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
			return nextEntry != null;
		}

		/**
		 * Method that returns next {@link TableEntry} stored in
		 * {@link SimpleDocletException}. Order of returning {@link TableEntry} is
		 * dependent on hashCode().
		 * 
		 * 
		 */
		public TableEntry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			current = nextEntry;
			if (nextEntry.next != null) {
				nextEntry = nextEntry.next;
				return current;
			}

			nextEntry = null;
			slotNumberIndex++;
			for (; slotNumberIndex < slots.length; slotNumberIndex++) {
				if (slots[slotNumberIndex] != null) {
					nextEntry = slots[slotNumberIndex];
					return current;
				}
			}
			return current;
		}

		/**
		 * Method that removes {@link TableEntry} that iterator returned lastly. Can
		 * remove only one {@link TableEntry} before next next() call is performed.
		 * 
		 * @throws ConcurrentModificationException if {@link SimpleHashtable} has been
		 *                                         modified
		 */
		public void remove() {
			if (iteratorModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}

			if (current != null) {
				SimpleHashtable.this.remove(current.key);
				current = null;
			} else {
				throw new IllegalStateException();
			}

			iteratorModificationCount = modificationCount;
		}
	}

	/**
	 * Method that returns value stored in {@link SimpleHashtable} under the
	 * provided key.
	 * 
	 * @param key under which a value is stored
	 * @return value stored alongside key value
	 */
	public V get(Object key) {
		if (key == null)
			return null;
		TableEntry<K, V> entry = findTableEntry(key);
		return entry != null ? entry.value : null;
	}

	/**
	 * Method that deletes {@link TableEntry} with given key out of
	 * {@link SimpleHashtable}. If no such {@link TableEntry} is found, nothing
	 * happens.
	 * 
	 * @param key
	 */
	public void remove(Object key) {
		if (key == null)
			return;

		int slotNumber = slotNumber(key, slots.length);
		if (slots[slotNumber] == null)
			return;

		if (slots[slotNumber].getKey().equals(key)) {
			slots[slotNumber] = slots[slotNumber].next;
			size--;
			modificationCount++;
			return;
		}

		TableEntry<K, V> entryThis = slots[slotNumber];
		TableEntry<K, V> entryToBeRemoved;
		
		while (entryThis != null) {
			entryToBeRemoved = entryThis.next;
			if (entryToBeRemoved == null) {
				return;
			}
			if (entryToBeRemoved.getKey().equals(key)) {
				entryThis.next = entryToBeRemoved.next;
				size--;
				modificationCount++;
				return;
			}
			entryThis = entryThis.next;
		}
	}

	/**
	 * Checks whether Collection contains Object value. Returns boolean.
	 * 
	 * @param value Object searched for in {@link SimpleHashtable}
	 * @return true if value is present, false otherwise
	 */
	public boolean containsValue(Object value) {
		TableEntry<K, V> traveller;

		for (var slot : slots) {
			traveller = slot;
			while (traveller != null) {
				if (traveller.getValue().equals(value))
					return true;
				traveller = traveller.next;
			}
		}
		return false;
	}

	/**
	 * Method that finds {@link TableEntry} with given key,
	 * 
	 * @param key that identify the pair
	 * @return {@link TableEntry} with given key
	 */
	private TableEntry<K, V> findTableEntry(Object key) {
		if (key == null)
			return null;

		TableEntry<K, V> entry = slots[slotNumber(key, slots.length)];

		while (entry != null) {
			if (entry.getKey().equals(key)) {
				return entry;
			}
			entry = entry.next;
		}
		return null;
	}

	/**
	 * Method that calculates slot index with given key and numberOfSlots
	 * 
	 * @param key
	 * @param numberOfSlots
	 * @return index of slot of particular key
	 */
	private int slotNumber(Object key, int numberOfSlots) {
		if (key == null)
			return -1;
		return Math.abs(key.hashCode()) % numberOfSlots;
	}

	/**
	 * Method that calculates first power of 2 greater or equal than given number
	 * 
	 * @param n
	 * @return power of 2 greater of equal to n
	 * @throws IllegalArgumentException if n is negative or greater than 2 to the
	 *                                  power of 20
	 */

	private static int getNextPowerOfTwo(int n) {
		if (n < 1)
			throw new IllegalArgumentException();
		int powerOfTwo = 1;
		while (powerOfTwo < n) {
			powerOfTwo <<= 1;
		}
		return powerOfTwo;
	}

	/**
	 * Method used to double hashmaps slot number to keep load factor below 0.75
	 */
	@SuppressWarnings("unchecked")
	private void doubleTheNumberOfSlots() {
		size = 0;
		TableEntry<K, V> oldie[] = slots;
		slots = new TableEntry[slots.length * 2];
		TableEntry<K, V> traveller;

		for (var slot : oldie) {
			traveller = slot;
			while (traveller != null) {
				putIntoTable(new TableEntry<K, V>(traveller.key, traveller.value));
				traveller = traveller.next;
			}
		}
	}

	/**
	 * Method that puts given {@link TableEntry} into {@link SimpleHashtable}.
	 * 
	 * @param toBeInserted {@link TableEntry} that gets inserted into
	 *                     {@link SimpleHashtable}
	 * @throws NullPointerException if {@link TableEntry} reference is null, or its
	 *                              key value is null.
	 */
	private void putIntoTable(TableEntry<K, V> toBeInserted) {
		Objects.requireNonNull(toBeInserted);
		Objects.requireNonNull(toBeInserted.getKey());

		int slot = slotNumber(toBeInserted.getKey(), slots.length);
		TableEntry<K, V> slotEntry = slots[slot];

		if (slotEntry == null) {
			slots[slot] = toBeInserted;
			size++;
			modificationCount++;
			return;
		}

		while (slotEntry != null) {
			if (slotEntry.getKey().equals(toBeInserted.getKey())) {
				slotEntry.setValue(toBeInserted.getValue());
				return;
			}
			if (slotEntry.next == null) {
				slotEntry.next = toBeInserted;
				size++;
				modificationCount++;
				return;
			}
			slotEntry = slotEntry.next;
		}
	}

	/**
	 * Method that clears storage of {@link SimpleHashtable} and
	 * deletes(dereferences) all entries so that garbage collector can free the
	 * memory.
	 */
	public void clear() {
		modificationCount++;

		for (int i = 0; i < slots.length; i++) {
			slots[i] = null;
		}
		size = 0;

	}

	/**
	 * public static class that models the pairs of data stored in
	 * {@link SimpleHashtable} with one reference variable that enables creation of
	 * Lists
	 * 
	 * @author juren
	 *
	 * @param <K> generic type of key
	 * @param <V> generic type of value
	 */
	public static class TableEntry<K, V> {
		/**
		 * used to store key
		 */
		private K key;
		/**
		 * used to store value
		 */
		private V value;
		/**
		 * used to store reference to another {@link TableEntry} and therefore build a
		 * linkedListF
		 */
		private TableEntry<K, V> next;

		/**
		 * Constructor for Pair class. Takes key and value.
		 * 
		 * @param key   key of {@link TableEntry}
		 * @param value value of {@link TableEntry}
		 * 
		 * @throws NullPointerException if provided key is null
		 */
		public TableEntry(K key, V value) {
			Objects.requireNonNull(key);
			this.key = key;
			this.value = value;
			this.next = null;
		}

		/**
		 * @return the key
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @return the value
		 */
		public V getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		var iteratorOverAll = iterator();
		sb.append("[");

		while (iteratorOverAll.hasNext()) {
			TableEntry<K, V> entry = iteratorOverAll.next();
			sb.append(entry);
			sb.append(", ");
		}

		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}
		sb.append("]");
		return sb.toString();
	}

}
