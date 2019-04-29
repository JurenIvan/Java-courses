package hr.fer.zemris.java.custom.collections;

/**
 * Class in which a resizeable array-backed collection of Objects is
 * implemented. Duplicate values can be stored but it can't store null objects.
 * Has a bundle of methods used to manipulate over storing, retrieving or
 * deleting data out of collection.
 * 
 * @author juren
 */
public class ArrayIndexedCollection extends Collection {
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

	private int size;
	private Object[] elements;

	/**
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
	public ArrayIndexedCollection(Collection other) {
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
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
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
	private ArrayIndexedCollection(Collection other, int initialCapacity, boolean otherCollectionProvided) {
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

		this.size = 0;
		this.elements = new Object[initialCapacity];

		if (otherCollectionProvided) {
			addAll(other);
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
	public void add(Object value) {
		insert(value, size);
	}

	/**
	 * Returns the object that is stored in array at position index. Valid indexes
	 * are 0 to size-1. If index is invalid IndexOutOfBoundsException() is thrown.
	 * 
	 * Complexity: O(1)
	 * 
	 * @param index of value that should be returned
	 * @return Object stored at that index
	 * @throws IndexOutOfBoundsException() if index is invalid
	 */
	public Object get(int index) {
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
	}

	/**
	 * Method that inserts value into array at given array. Values previously stored
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
	public void insert(Object value, int position) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

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
			Object[] temp = new Object[elements.length * 2]; // (0) Doubling
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
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		size--;
		for (int i = index; i < size; i++) {
			elements[i] = elements[i + 1];
		}
		elements[size] = null;

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
	 * Finds (by utilising .equals()) and removes first occurrence of Object in
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
	public Object[] toArray() {
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			result[i] = elements[i];
		}
		return result;
	}

	/**
	 * Method that processes EACH element stored in array using processor.process()
	 * 
	 * @param processor model of an object capable of performing some operation on
	 *                  the passed object.
	 * @throws NullPointerException if provided processor refers to null
	 */
	@Override
	public void forEach(Processor processor) {
		if (processor == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

}
