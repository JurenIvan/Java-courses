package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * Class in which a resizeable array-backed generic collection of Objects is
 * implemented. Duplicate values can be stored but it can't store null objects.
 * Has a bundle of methods used to manipulate over storing, retrieving or
 * deleting data out of collection. Implements interface List which defines some
 * methods for managing list like collections
 * 
 * @author juren
 */
@SuppressWarnings("unchecked")
public class ArrayIndexedCollection<T> implements List<T> {
	/**
	 * Default capacity of Collection is {@value #DEFAULT_CAPACITY}. No particular
	 * reasoning behind it. Can be modified.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Minimum capacity of Collection is {@value #MINIMAL_CAPACITY}. Some
	 * constructors may throw {@link IllegalArgumentException} if collection is
	 * tried to be made with smaller capacity.
	 */
	private static final int MINIMAL_CAPACITY = 1;

	/**
	 * used to store current number of elements stored in collection
	 */
	private int size;

	/**
	 * Array used to store elements in collection
	 */
	private T[] elements;

	/**
	 * Due to importance of not changing collection while iterating through it,
	 * {@link ArrayIndexedCollection} implements counter of made changes.
	 * 
	 * Whenever a new object is added/inserted or an object is removed this variable
	 * changes
	 */
	private long modificationCount;

	/**
	 *
	 * Default constructor. Sets initial capacity to {@value #DEFAULT_CAPACITY} and
	 * allocates needed memory.
	 */
	public ArrayIndexedCollection() {
		this(null, DEFAULT_CAPACITY, false);
	}

	/**
	 * Constructor with setable initialCapacity. If initialCapacity is less than
	 * {@value #MINIMAL_CAPACITY} {@link IllegalArgumentException} is thrown.
	 * 
	 * @param initialCapacity starting capacity of Collection
	 * @throws IllegalArgumentException if {@link initialCapacity} is smaller than
	 *                                  {@value #MINIMAL_CAPACITY}
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(null, initialCapacity, false);
	}

	/**
	 * Constructor that can copy another collection into this one. Sets initial
	 * capacity to other collections size and allocates needed memory.
	 * 
	 * @param other Collection that will be copied from
	 * @throws NullPointerException if argument(Collection) passed is null-reference
	 */
	public ArrayIndexedCollection(Collection<T> other) {
		this(other, other.size(), true);
	}

	/**
	 * Constructor with settable initialCapacity and reference to another Collection
	 * that can be copied. If initialCapacity is less than number of objects stored
	 * in referenced collection, initialCapacity is automatically set to number of
	 * elements in referenced collection. Exception is thrown when null reference to
	 * other Collection is provided.
	 * 
	 * @param other           Collection that Objects will be copied from
	 * @param initialCapacity initialCapacity starting capacity of Collection
	 * @throws NullPointerException if Collection passed is null-reference
	 * 
	 */
	public ArrayIndexedCollection(Collection<T> other, int initialCapacity) {
		this(other, initialCapacity, true);
	}

	/**
	 * "Master" constructor that unifies capabilities of all other constructors and
	 * does all the work. Other constructors just delegate into this one. Sets
	 * capacity of Collection, and allocates that space in memory.
	 * 
	 * It has setable initialCapacity that has to be greater than 1. If
	 * otherCollectionProvided is false (in case that we set the initial capacity
	 * ourselves and don't provide other Collection that has to be copied into it).
	 * 
	 * It can copy other collection into this one if non-null-reference to
	 * collection is provided and otherCollectionProvided is true.
	 * 
	 * If null collection is provided and argument otherCollection is true,
	 * NullPointerException() is thrown.
	 * 
	 * If otherCollectionProvided is false and initialCapacity is smaller than
	 * minimum that is predefined, {@link IllegalArgumentException} is thrown.
	 * 
	 * 
	 * @param other                   Collection that will be copied from into this
	 *                                one
	 * @param initialCapacity         initialCapacity starting capacity of
	 *                                Collection
	 * @param otherCollectionProvided boolean that differentiates whether the
	 *                                Collection is provided or not
	 * 
	 * @throws NullPointerException     if Collection passed is null-reference
	 * @throws IllegalArgumentException if {@link initialCapacity} is smaller than
	 *                                  {@value #MINIMAL_CAPACITY} and
	 *                                  otherCollectionProvided is false
	 */
	
	private ArrayIndexedCollection(Collection<T> other, int initialCapacity, boolean otherCollectionProvided) {
		if (initialCapacity < MINIMAL_CAPACITY && !otherCollectionProvided) {
			throw new IllegalArgumentException();
		}

		if (other == null && otherCollectionProvided) {
			throw new NullPointerException();
		}

		if (otherCollectionProvided && other.size() >= initialCapacity) {
			if (other.size() == 0) {
				throw new IllegalArgumentException();
			}
			initialCapacity = other.size();
		}

		this.elements =(T[]) new Object[initialCapacity];
		this.size = 0;
		this.modificationCount = 0;

		if (otherCollectionProvided) {
			addAll(other);
		}

	}

	/**
	 * Method used to create {@link ArrayIndexedCollectionElementsGetter} which
	 * provides generic iterator-like capabilities.
	 * 
	 * read more at {@link ArrayIndexedCollectionElementsGetter}
	 * 
	 * @return {@link ElementsGetter}
	 */
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ArrayIndexedCollectionElementsGetter<T>(elements, size, modificationCount, this);
	}

	/**
	 * Static generic class used to provide a iterator-like functionality. It takes a
	 * reference to elements stored in collection and iterates trough them on
	 * demand; user can ask instance of element getter 2 things: hasNextElement and
	 * getNextElement.
	 * 
	 * Can be created via (instance of collection).createElementsGetter() if
	 * collection hasn't got any elements left and user asks for next element, an
	 * exception is thrown
	 * 
	 * @author juren
	 *
	 */
	private static class ArrayIndexedCollectionElementsGetter<T> implements ElementsGetter<T> {

		/**
		 * Stores the index of element that will be returned next
		 */
		private int indexOfCurrentElement;

		/**
		 * Stores the number of elements that can be returned one by one
		 */
		private int numberOfElementsInArray;

		/**
		 * Stores the count of previous modifications made to collection storing the
		 * elements that we go through
		 */
		private long savedModificationCount;

		/**
		 * Stores the reference to collection so that we can check whether a new
		 * modification has been made.
		 */
		private ArrayIndexedCollection<T> reference;

		// Could have been avoided because we have reference to collection.
		/**
		 * Stores the reference to objects that we iterate through.
		 */
		private T[] elements;

		/**
		 * Default constructor of {@link ArrayIndexedCollectionElementsGetter}. Requires
		 * reference to elements through which it iterates, number of elements in array
		 * to be able to stop iterating when end of stored values is reached,
		 * modification count which is later used to check whether any change has been
		 * made in collection, and reference to Collection to be able to check that
		 * difference in modification count.
		 * 
		 * @throws NullPointerException when reference to elements provided is null
		 *                              reference
		 * 
		 * @param elements                reference to elements array which it will
		 *                                iterate through
		 * @param numberOfElementsInArray number of elements stored in array used know
		 *                                when the end of stored elements is reached
		 * @param modificationCount       number of changes made to collection at the
		 *                                time of creation of this class
		 * @param reference               reference to collection that enables us to
		 *                                check(at any time) the current state of
		 *                                modificationCount at the collection instance
		 */
		private ArrayIndexedCollectionElementsGetter(T[] elements, int numberOfElementsInArray,
				long modificationCount, ArrayIndexedCollection<T> reference) {

			Objects.requireNonNull(elements);
			this.reference = reference;
			this.indexOfCurrentElement = 0;
			this.numberOfElementsInArray = numberOfElementsInArray;
			this.elements = elements;
			this.savedModificationCount = modificationCount;
		}

		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != reference.modificationCount) {
				throw new ConcurrentModificationException();
			}

			return indexOfCurrentElement < numberOfElementsInArray;
		}

		@Override
		public T getNextElement() {
			if (!hasNextElement()) {
				throw new NoSuchElementException();
			}

			indexOfCurrentElement++;
			return elements[indexOfCurrentElement - 1];
		}
	}

	/**
	 * Adds the given object into this collection into first empty place in the
	 * elements array. If the elements array is full, it is reallocated by doubling
	 * space.
	 * 
	 * Complexity: average(1), worst-case(O(n))
	 * 
	 * @param value Object that should be saved into collection. Can't be null.
	 * @throws NullPointerException if value is null reference.
	 * 
	 */
	@Override
	public void add(T value) {
		insert(value, size);
	}

	@Override
	public T get(int index) {
		if (index < 0 || index > (size - 1))
			throw new IndexOutOfBoundsException();
		return elements[index];
	}

	/**
	 * Method that removes all elements from collection by overwriting references to
	 * objects with null reference. Objects that were stored get unreferenceable and
	 * then garbage collector removes them from memory.
	 * 
	 * Complexity: O(n)
	 * 
	 */
	public void clear() {
		for (int i = 0; i <= size; i++) {
			elements[i] = null;
		}
		size = 0;
		modificationCount++;
	}

	/**
	 * Method that inserts value into array at given index. Values previously stored
	 * are shifted one place to right (it's index increases). Depending on position
	 * it can throw {@link IndexOutOfBoundsException} or if value is null (not
	 * allowed) it throws {@link NullPointerException}. If collection is full it
	 * uses doubleAndCopy() like functionality shown below and doubles capacity of
	 * array by copying.
	 * 
	 * @param value    Object that should be stored into array
	 * @param position integer referring to a position where we want to insert new
	 *                 value
	 * 
	 * @throws NullPointerException      if null reference is provided as value
	 * @throws IndexOutOfBoundsException if position isn't a number between 0 and
	 *                                   size of collection.
	 */
	@Override
	public void insert(T value, int position) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

		modificationCount++;

		/*
		 * if it has to insert Object in the middle and double the array(0) it does it
		 * simultaneously. (1) it copies everything that was on <0-position> index
		 * then(2) inserts new Object onto position and (3)then copies the rest.
		 * 
		 * I could have written nicer to look but slower to run code with DoubleAndCopy
		 * method. Example of that method is under this one.
		 */
		if (size == elements.length) {

			int oldSize = size;
			T[] temp =(T[]) new Object[elements.length * 2]; // (0) Doubling
			for (size = 0; size < position; size++) { // (1) Copying old Objects
				temp[size] = elements[size];
			}
			temp[position] = value; // (2) Placing new one
			for (; size < oldSize; size++) { // (3) Copy the rest
				temp[size + 1] = elements[size];
			}
			elements = temp;
			size++;
			return;
		}
		// if no expansion is required, every object from index size to position is
		// shifted and Object is inserted in proper position
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
	}

//	/**
//	 * Private method used to double array size and copy existing data into new one
//	 * by doubling capacity of array. Used to beautify code. Not efficient because
//	 * it can double the execution time of adding into array (when we want to add at
//	 * index 0)
//	 */
//	private void doubleAndCopy() {
//		size = 0;
//		Object[] temp = new Object[elements.length * 2];
//		for (Object elem : elements) {
//			temp[size++] = elem;
//		}
//		elements = temp;
//		return;
//	}

	@Override
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		for (int i = 0; i < size; i++) {
			if (elements[i].equals(value))
				return i;
		}
		return -1;
	}

	@Override
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		size--;
		for (int i = index; i < size; i++) {
			elements[i] = elements[i + 1];
		}
		elements[size] = null;
		modificationCount++;
	}

	/**
	 * Method that returns number of objects currently stored in collection.
	 * 
	 * @return integer number of objects stored
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Checks whether Collection contains Object value. Returns boolean.
	 * 
	 * @param value Object searched for in Collection
	 * @return true if value is present, false otherwise
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}

	/**
	 * Finds (by utilizing .equals()) and removes first occurrence of Object in
	 * Collection.
	 * 
	 * @returns true if Object is removed, false otherwise
	 * 
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index != -1) {
			remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Method used to allocate and return new array of Objects with size that is
	 * equal to the size of this collection. Then fills the new array with
	 * collection content (Objects) and returns it.
	 * 
	 * @return array of Objects
	 */
	@Override
	public T[] toArray() {
		T[] result =(T[]) new Object[size];
		for (int i = 0; i < size; i++) {
			result[i] = (T) elements[i];
		}
		return result;
	}

	
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

}
