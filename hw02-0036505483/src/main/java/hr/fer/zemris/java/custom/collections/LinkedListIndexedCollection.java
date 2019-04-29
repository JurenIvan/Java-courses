package hr.fer.zemris.java.custom.collections;

/**
 * Class in which Linked-List-backed collection of objects is implemented.
 * Duplicate values can be stored, but no null references is allowed.
 * 
 * Has a bundle of methods used to manipulate over storing, retrieving or
 * deleting data out of collection.
 * 
 * @author juren
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * Private static class used as a structure which contains two references to
	 * previous and next LisNode and Object in which a value is stored.
	 * 
	 * @author juren
	 *
	 */
	private static class ListNode {
		ListNode previous;
		ListNode next;
		Object value;

		/**
		 * Constructor for local class whose primary use is code organization and
		 * simplification
		 * 
		 * @param value Object stored in ListNode
		 */
		public ListNode(Object value) {
			this.value = value;
			this.next = null;
			this.previous = null;
		}
	}

	private int size;
	private ListNode first;
	private ListNode last;

	/**
	 * Default constructor of {@link LinkedListIndexedCollection}. Sets current size
	 * to 0 and both ListNode references to null. Node first is reference to first
	 * Node in collection. It's index is 0. Node last is reference to last Node in
	 * collection. It's index can be obtained by .size()
	 */
	public LinkedListIndexedCollection() {
		this.size = 0;
		this.first = null;
		this.last = null;
	}

	/**
	 * Constructor with reference to other collection whose Nodes can be added into
	 * this collection using {@link #addAll(Collection)}. Throws
	 * {@link NullPointerException} if referenced collection is null.
	 * 
	 * @param other collection that'll be copied from
	 * @throws NullPointerException if referenced collection is null reference
	 */
	public LinkedListIndexedCollection(Collection other) {
		addAll(other);
	}

	/**
	 * Adds the given object into this collection. Newly added element becomes the
	 * element at the biggest index. Complexity: O(1)
	 * 
	 * @param value Object that'll be added into collection, mustn't be null.
	 * @throws NullPointerException if referenced Object is null.
	 */
	public void add(Object value) {
//		insert(value, this.size);		//Delegating results with cleaner code but it is slower to run due to sheer number of if statements in insert method that it has to go through
		if (value == null) {
			throw new NullPointerException();
		}

		ListNode toBeAdded = new ListNode(value);
		size++;

		if (first == null) {
			first = toBeAdded;
			last = toBeAdded;
			return;
		}

		last.next = toBeAdded;
		toBeAdded.previous = last;
		last = toBeAdded;
	}

	/**
	 * Returns the object that is stored in linked list at position index. Valid
	 * indexes are 0 to size-1. If invalid index is chosen
	 * {@link LinkedListIndexedCollection} is thrown. Complexity is O(n/2) because,
	 * depending on index and current size of list, we start either from start or
	 * end of list.
	 * 
	 * @return Object stored in ListNode that is index-th in order
	 * @throws IndexOutOfBoundsException if invalid index is chosen. Valid indexes
	 *                                   are 0 to size-1.
	 */

	public Object get(int index) {
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
	private ListNode getNode(int index) {
		if (index < 0 || index > (size - 1)) {
			throw new IndexOutOfBoundsException();
		}

		ListNode temp;
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
	private ListNode getNode(Object value) {
		if (value == null)
			return null;

		ListNode curr = first;
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
	public void insert(Object value, int position) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

		ListNode toBeInserted = new ListNode(value);

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

		ListNode right = getNode(position);
		ListNode left = right.previous;

		left.next = toBeInserted;
		right.previous = left.next;
		toBeInserted.previous = left;
		toBeInserted.next = right;
		size++;
	}

	/**
	 * Searches the collection and returns the index of the first occurrence of the
	 * given value or -1 if the value is not found. Null is valid argument. The
	 * equality is determined using the .equals() method.
	 * 
	 * Complexity: O(n)
	 * 
	 * @param value Object which is searched for in Collection
	 * @return integer number that resembles position of searched value in
	 *         Collection
	 * 
	 */
	public int indexOf(Object value) {
		int index = 0;
		ListNode current = first;

		while (current != null) {
			if (current.value.equals(value)) {
				return index;
			}
			current = current.next;
			index++;
		}
		return -1;
	}

	/**
	 * Removes element at specified index. Element that was previously at location
	 * index+1 after this operation moves to location index, etc.
	 * 
	 * @param index of node that will be removed. Legal indexes are 0 to size-1. If
	 *              index is illegal exception is thrown
	 * 
	 * @throws IndexOutOfBoundsException if provided index is illegal.
	 */

	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListNode curr = getNode(index);
		removeNode(curr);
	}
	/**
	 * Method that knows how to remove given node and removes it from list.
	 * 
	 * @param node to be removed
	 */
	private void removeNode(ListNode node) {
		size--;
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
		ListNode left = node.previous;
		ListNode right = node.next;
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
		ListNode curr = getNode(value);
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
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];

		int counter = 0;
		ListNode temp = first;
		while (temp != null) {
			array[counter++] = temp.value;
			temp = temp.next;
		}
		return array;
	}

	/**
	 * Method that calls processor.process(value) for each value of collection.
	 */
	@Override
	public void forEach(Processor processor) {
		ListNode temp = first;
		while (temp != null) {
			processor.process(temp.value);
			temp = temp.next;
		}
	}

}
