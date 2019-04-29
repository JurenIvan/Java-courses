package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	private LinkedListIndexedCollection<Integer> makeNumbersCollection() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();
		col.add(1);
		col.add(2);
		col.add(3);
		col.add(4);
		col.add(5);
		col.add(6);
		col.add(7);
		col.add(8);
		col.add(9);
		return col;
	}

	@Test
	void isEmptyTestEmpty() {
		assertTrue(new LinkedListIndexedCollection<Integer>().isEmpty());
	}

	@Test
	void isEmptyTestNotEmpty() {
		assertFalse(makeNumbersCollection().isEmpty());
	}

	@Test
	void sizeTestEmpty() {
		assertTrue(new LinkedListIndexedCollection<Integer>().size() == 0);
	}

	@Test
	void sizeTestNotEmptyJustAdding() {
		assertTrue(makeNumbersCollection().size() == 9);
	}

	@Test
	void sizeTestNotEmptyAfterAddingAndRemoving() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertFalse(col.remove((Integer) 101));
		assertTrue(col.size() == 8);
	}

	@Test
	void addTestNull() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<Integer>();

		assertThrows(NullPointerException.class, () -> col.add(null));
	}

	@Test
	void addTestNotANull() {
		LinkedListIndexedCollection<String> col = new LinkedListIndexedCollection<>();
		col.add("ivan");
		assertTrue(col.size() == 1);
		assertTrue(col.contains("ivan"));
		assertTrue(col.get(0).equals("ivan"));
		assertFalse(col.isEmpty());
	}

	@Test
	void addTestContainsTest() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(9));
		assertTrue(col.contains(1));
		assertFalse(col.contains(202));
		assertTrue(col.contains(8));

		col.remove(0);
		col.add(100);

		assertFalse(col.contains(1));
		assertTrue(col.contains(100));
		assertTrue(col.size() == 9);

	}

	@Test
	void addTestDoublingArray() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();

		col.add(1);
		assertTrue(col.size() == 1);
		col.add(2);
		col.add(3);
		col.add(4);
		col.add(5);
		assertTrue(col.size() == 5);

		col.add(6);
		col.add(7);
		col.add(8);
		col.add(9);
		assertTrue(col.size() == 9);

		assertTrue(col.get(0).equals(1));
		assertTrue(col.get(1).equals(2));
		assertTrue(col.get(6).equals(7));
		assertTrue(col.get(8).equals(9));


	}

	@Test
	void addTestAddAfterRemove() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(0);
		col.add(100);

		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.get(8).equals(100));


	}

	@Test
	void addTestAddAfterEmptied() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.clear();
		assertTrue(col.isEmpty());
		col.add(1);
		col.add(2);
		col.add(3);
		col.add(4);
		assertTrue(col.contains(4));
		assertTrue(col.size() == 4);
		assertFalse(col.contains(5));

	}

	@Test
	void addTestAddAfterCleaned() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.clear();
		col.add(1);
		col.add(2);
		col.add(3);
		assertTrue(col.get(0).equals(1));
		assertTrue(col.get(1).equals(2));


	}

	@Test
	void containsTestNull() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.contains(null));
	}

	@Test
	void containsTestFalse() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.contains("ivan"));
	}

	@Test
	void containsTestTrueFirst() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(1));
	}

	@Test
	void containsTestTrueMiddle() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(4));
	}

	@Test
	void containsTestTrueEndOfArray() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(9));
	}

	@Test
	void removeTestNull() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.remove(null));
	}

	@Test
	void removeTestNotInCollection() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.remove("ivan"));
		assertTrue(col.get(8).equals(9));
		assertTrue(col.size() == 9);
	}

	@Test
	void removeTestFirstElement() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertTrue(col.get(0).equals(2));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddleElement() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 5));
		assertTrue(col.get(3).equals(4));
		assertTrue(col.get(4).equals(6));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLastElement() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 9));
		assertTrue(col.get(7).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestEmptyCollection() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();
		assertFalse(col.remove((Integer) 0));

	}

	@Test
	void toArrayTestEmpty() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();
		Object[] array = col.toArray();

		for (int i = 0; i < col.size(); i++) {
			assertTrue(array[i] == (Integer) (i + 1));
		}
	}

	@Test
	void toArrayTestNotEmpty() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		Object[] array = col.toArray();

		for (int i = 0; i < col.size(); i++) {
			assertTrue(array[i] == (Integer) (i + 1));
		}
	}

	@Test
	void forEachTestNullProcessor() {
		assertThrows(NullPointerException.class, () -> makeNumbersCollection().forEach(null));
	}


	@Test
	void addAllTestNull() {
		LinkedListIndexedCollection<Integer> col1 = makeNumbersCollection();
		LinkedListIndexedCollection<Integer> col2 = null;
		assertThrows(NullPointerException.class, () -> col1.addAll(col2));
	}

	@Test
	void addAllTestBig() {
		LinkedListIndexedCollection<Integer> col1 = new LinkedListIndexedCollection<>();
		LinkedListIndexedCollection<Integer> col2 = makeNumbersCollection();
		col1.addAll(col2);
		assertTrue(col1.size() == col2.size());
		for (int i = 0; i < col1.size(); i++) {
			assertTrue(col1.get(i).equals(col2.get(i)));
		}

	}

	@Test
	void clearTest() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.clear();
		assertTrue(col.size() == 0);
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(0));
	}

	@Test
	void getTestUnreachableTooSmall() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(-2));
	}

	@Test
	void getTestUnreachableTooBig() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(123));
	}

	@Test
	void insertTestPositionTooSmall() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert(23, -23));
	}

	@Test
	void insertTestPositionTooBig() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert(22, 23));
	}

	@Test
	void insertTestObjectNull() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(NullPointerException.class, () -> col.insert(null, 2));
	}

	@Test
	void insertTestFirst() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(0, 0);
		assertTrue(col.get(0).equals(0));
		assertTrue(col.get(1).equals(1));
		assertTrue(col.get(5).equals(5));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestLast() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(10, 9);
		assertTrue(col.get(9).equals(10));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestMiddle() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(5, 5);
		assertTrue(col.get(5).equals(5));
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(6).equals(6));
		assertTrue(col.size() == 10);
	}

	@Test
	void indexOfTestNotPresent() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf("ivan") == -1);
	}

	@Test
	void indexOfTestFirst() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(1) == 0);
	}

	@Test
	void indexOfLast() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(9) == 8);
	}

	@Test
	void indexOfMiddle() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(5) == 4);
	}

	@Test
	void removeTestUnreachableTooSmall() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(-23));
	}

	@Test
	void removeTestUnreachableTooBig() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(23));
	}

	@Test
	void removeTestFirst() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(0);
		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddle() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(5);
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLast() {
		LinkedListIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(8);
		assertTrue(col.get(7).equals(8));
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(9));
		assertTrue(col.size() == 8);
	}

	
	@Test
	void defaultConstructorTest() {
		LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();
		assertTrue(col.isEmpty());

	}

	@Test
	void constructorTestOtherCollection() {
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection<>(null));
	}

	@Test
	void constructorTestOtherCollectionNull() {
		LinkedListIndexedCollection<Integer> col2 = new LinkedListIndexedCollection<>(makeNumbersCollection());
		Object[] col1Array = makeNumbersCollection().toArray();
		Object[] col2Array = col2.toArray();

		for (int i = 0; i < col1Array.length; i++) {
			assertTrue(col1Array[i] == col2Array[i]);
		}
	}

}
