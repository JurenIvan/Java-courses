package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class in which Linked-List-backed collection of objects is implemented.
 * Duplicate values can be stored, but no null references is allowed.
 * 
 * Has a bundle of methods used to manipulate over storing, retrieving or
 * deleting data out of collection.
 * 
 * @author juren
 */
public class LinkedListIndexedCollection<T> implements List<T> {

	/**
	 * Private static class used as a structure which contains two references to
	 * previous and next LisNode and Object in which a value is stored.
	 * 
	 * @author juren
	 *
	 */
	private static class ListNode<T> {
		ListNode<T> previous;
		ListNode<T> next;
		T value;

		/**
		 * Constructor for local class whose primary use is code organization and
		 * simplification
		 * 
		 * @param value Object stored in ListNode
		 */
		public ListNode(T value) {
			this.value = value;
			this.next = null;
			this.previous = null;
		}
	}

	private int size;
	private ListNode<T> first;
	private ListNode<T> last;
	private long modificationCount;

	/**
	 * Default constructor of {@link LinkedListIndexedCollection}. Sets current size
	 * to 0 and both ListNode references to null. Node first is reference to first
	 * Node in collection. It's index is 0. Node last is reference to last Node in
	 * collection. It's index can be obtained by .size(). When collection is created
	 * modificationCount is set to zero. Later while use of collection it is used to
	 * determine whether the collection has been modified to insure proper
	 * functioning of ElementGetters.
	 * 
	 */
	public LinkedListIndexedCollection() {
		this.first = null;
		this.last = null;
		this.size = 0;

		this.modificationCount = 0;

	}

	/**
	 * Constructor with reference to other collection whose Nodes can be added into
	 * this collection using {@link #addAll(Collection)}. Throws
	 * {@link NullPointerException} if referenced collection is null.
	 * 
	 * @param other collection that'll be copied from
	 * @throws NullPointerException if referenced collection is null reference
	 */
	public LinkedListIndexedCollection(Collection<T> other) {
		this();
		addAll(other);
	}

	/**
	 * Adds the given object into this collection. Newly added element becomes the
	 * element at the biggest index. Complexity: O(1)
	 * 
	 * @param value Object that'll be added into collection, mustn't be null.
	 * @throws NullPointerException if referenced Object is null.
	 */
	public void add(T value) {
		insert(value, this.size);		//Delegating results with cleaner code but it is slower to run due to sheer number of if statements in insert method that it has to go through
	}

	/**
	 * Method used to create {@link LinkedListIndexedCollectionElementsGetter} which
	 * provides iterator-like capabilities over this collection.
	 * 
	 */
	public ElementsGetter<T> createElementsGetter() {
		return new LinkedListIndexedCollectionElementsGetter<T>(first, size, modificationCount, this);
	}

	private static class LinkedListIndexedCollectionElementsGetter<T> implements ElementsGetter<T> {

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

		// Could have been avoided because we have reference to collection.
		/**
		 * Stores the first node in list that it iterates through.
		 */
		private ListNode<T> nextNodeInList;
		/**
		 * Stores the reference to objects that we iterate through.
		 */
		private LinkedListIndexedCollection<T> reference;

		/**
		 * Default constructor of {@link LinkedListIndexedCollectionElementsGetter}.
		 * Requires reference to first node of elements through which it iterates,
		 * number of elements in collection to be able to stop iterating when end of
		 * stored values is reached, modification count which is later used to check
		 * whether any change has been made in collection, and reference to
		 * LinkedListIndexedCollection to be able to check that difference in
		 * modification count.
		 * 
		 * @throws NullPointerException when reference to elements provided is null
		 *                              reference
		 * 
		 * @param elements               reference to elements array which it will
		 *                               iterate through
		 * @param numberOfElementsInList number of elements stored in array used know
		 *                               when the end of stored elements is reached
		 * @param modificationCount      number of changes made to collection at the
		 *                               time of creation of this class
		 * @param reference              reference to collection that enables us to
		 *                               check(at any time) the current state of
		 *                               modificationCount at the collection instance
		 */

		private LinkedListIndexedCollectionElementsGetter(ListNode<T> first, int numberOfElementsInList,
				long modificationCount, LinkedListIndexedCollection<T> reference) {
			Objects.requireNonNull(first);

			this.indexOfCurrentElement = 0;
			this.numberOfElementsInArray = numberOfElementsInList;
			this.nextNodeInList = first;
			this.reference = reference;

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
			ListNode<T> temp = nextNodeInList;
			nextNodeInList = nextNodeInList.next;
			return temp.value;
		}
	}

	@Override
	public T get(int index) {
		return getNode(index).value;
	}

	/**
	 * Returns the node that is stored in linked list at position index. Valid
	 * indexes are 0 to size-1. If invalid index is chosen
	 * {@link LinkedListIndexedCollection} is thrown.Complexity is O(n/2) because,
	 * depending on index and current size of list, we start either from start or
	 * end of list.
	 * 
	 * @return Node that is index-th in order
	 * @throws IndexOutOfBoundsException if invalid index is chosen.Valid indexes
	 *                                   are in range 0 to size-1.
	 */
	private ListNode<T> getNode(int index) {
		if (index < 0 || index > (size - 1)) {
			throw new IndexOutOfBoundsException();
		}

		ListNode<T> temp;
		if (index > (size / 2)) {
			temp = last;
			for (int i = size - 1; i > index; i--) {
				temp = temp.previous;
			}
		} else {
			temp = first;
			for (int i = 0; i < index; i++) {
				temp = temp.next;
			}
		}
		return temp;
	}

	/**
	 * Returns the node that has Object with value value stored in it. Equality is
	 * checked with .equals(). If argument value is null then method returns null.
	 * 
	 * @param value Object based on whose content we search for nodes.
	 * @return Node that contains Object equal to value or null if no such object is
	 *         found
	 */
	private ListNode<T> getNode(Object value) {
		if (value == null)
			return null;

		ListNode<T> curr = first;
		while (curr != null) {
			if (curr.value.equals(value))
				return curr;
			curr = curr.next;
		}
		return null;
	}

	/**
	 * Removes all elements from the collection. Collection “forgets” about current
	 * linked list by setting first and last references to null, and setting it's
	 * current size to 0.
	 * 
	 * All nodes that used to be in Collection get unreachable, therefore memory can
	 * be freed by garbage collector.
	 * 
	 */
	public void clear() {
		first = null;
		last = null;
		size = 0;

		modificationCount++;

	}

	/**
	 * Inserts (does not overwrite) the ListNode with given value at the given
	 * position in linked-list. Elements starting from this position are shifted one
	 * position to the right.
	 * 
	 * @param value    Object that will be stored into collection. Mustn't be null.
	 * @param position index of Node with Object stored in it in Collection.
	 * 
	 * @throws NullPointerException      if value is reference to null
	 * @throws IndexOutOfBoundsException if position is not legal(The legal
	 *                                   positions are 0 to size).
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

		ListNode<T> toBeInserted = new ListNode<T>(value);

		// insertion into empty list
		if (size == 0) {
			first = toBeInserted;
			last = toBeInserted;
			size = 1;
			return;
		}
		// insertion onto first place
		if (position == 0) {
			toBeInserted.next = first;
			first.previous = toBeInserted;
			first = toBeInserted;
			size++;
			return;
		}
		// insertion onto last place
		if (position == size) {
			last.next = toBeInserted;
			toBeInserted.previous = last;
			last = toBeInserted;
			size++;
			return;
		}

		ListNode<T> right = getNode(position);
		ListNode<T> left = right.previous;

		left.next = toBeInserted;
		right.previous = left.next;
		toBeInserted.previous = left;
		toBeInserted.next = right;
		size++;
	}

	@Override
	public int indexOf(Object value) {
		int index = 0;
		ListNode<T> current = first;

		while (current != null) {
			if (current.value.equals(value)) {
				return index;
			}
			current = current.next;
			index++;
		}
		return -1;
	}

	@Override
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListNode<T> curr = getNode(index);
		removeNode(curr);
	}

	/**
	 * Method that knows how to remove given node and removes it from list.
	 * 
	 * @param node to be removed
	 */
	private void removeNode(ListNode<T> node) {
		size--;

		modificationCount++;

		// there is only one node in list
		if (first == last) {
			clear();
			return;
		}
		// node is first node in list
		if (node.previous == null) {
			first = first.next;
			first.previous = null;
			return;
		}
		// node is last node in list
		if (node.next == null) {
			last = last.previous;
			last.next = null;
			return;
		}
		// node is somewhere in the middle of list
		ListNode<T> left = node.previous;
		ListNode<T> right = node.next;
		left.next = right;
		right.previous = left;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Method that returns the number of values stored in Collection.
	 * 
	 * @return number of Objects stored in Collection
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Method used to check presence of value in collection.
	 * 
	 * @param value Object whose presence is checked
	 * @return boolean dependent on presence of referenced object
	 */
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}

	/**
	 * Method that removes object out of collection based on .equals() method.
	 * Returns true only if the collection contains given value (as determined by
	 * .equals() method) and then removes one occurrence of it.
	 * 
	 * @return true if referenced object was found and removed, otherwise false
	 */
	@Override
	public boolean remove(Object value) {
		ListNode<T> curr = getNode(value);
		if (curr == null) {
			return false;
		}
		removeNode(curr);
		return true;
	}

	/**
	 * Method used to allocate new array of Objects with size equal to the size of
	 * this collection. After allocation of memory, array is filled with collection
	 * content(copies Objects). Then array is returned.
	 * 
	 * @return array of Objects stored in Collection.
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray() {
		T[] array = (T[]) new Object[size];
		int counter = 0;
		ListNode<T> temp = first;
		while (temp != null) {
			array[counter++] = temp.value;
			temp = temp.next;
		}
		return array;
	}

}
