package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	private LinkedListIndexedCollection makeNumbersCollection() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
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
		assertTrue(new LinkedListIndexedCollection().isEmpty());
	}

	@Test
	void isEmptyTestNotEmpty() {
		assertFalse(makeNumbersCollection().isEmpty());
	}

	@Test
	void sizeTestEmpty() {
		assertTrue(new LinkedListIndexedCollection().size() == 0);
	}

	@Test
	void sizeTestNotEmptyJustAdding() {
		assertTrue(makeNumbersCollection().size() == 9);
	}

	@Test
	void sizeTestNotEmptyAfterAddingAndRemoving() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertFalse(col.remove((Integer) 101));
		assertTrue(col.size() == 8);
	}

	@Test
	void addTestNull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();

		assertThrows(NullPointerException.class, () -> col.add(null));
	}

	@Test
	void addTestNotANull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add("ivan");
		assertTrue(col.size() == 1);
		assertTrue(col.contains("ivan"));
		assertTrue(col.get(0).equals("ivan"));
		assertFalse(col.isEmpty());
	}

	@Test
	void addTestContainsTest() {
		LinkedListIndexedCollection col = makeNumbersCollection();
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
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();

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
		assertFalse(col.get(4).equals("ivan"));

	}

	@Test
	void addTestAddAfterRemove() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.remove(0);
		col.add(100);

		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.get(8).equals(100));
		assertFalse(col.get(4).equals("ivan"));

	}

	@Test
	void addTestAddAfterEmptied() {
		LinkedListIndexedCollection col = makeNumbersCollection();
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
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.clear();
		col.add(1);
		col.add(2);
		col.add(3);
		assertTrue(col.get(0).equals(1));
		assertTrue(col.get(1).equals(2));
		assertFalse(col.get(2).equals("ivan"));

	}

	@Test
	void containsTestNull() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertFalse(col.contains(null));
	}

	@Test
	void containsTestFalse() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertFalse(col.contains("ivan"));
	}

	@Test
	void containsTestTrueFirst() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.contains(1));
	}

	@Test
	void containsTestTrueMiddle() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.contains(4));
	}

	@Test
	void containsTestTrueEndOfArray() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.contains(9));
	}

	@Test
	void removeTestNull() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertFalse(col.remove(null));
	}

	@Test
	void removeTestNotInCollection() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertFalse(col.remove("ivan"));
		assertTrue(col.get(8).equals(9));
		assertTrue(col.size() == 9);
	}

	@Test
	void removeTestFirstElement() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertTrue(col.get(0).equals(2));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddleElement() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 5));
		assertTrue(col.get(3).equals(4));
		assertTrue(col.get(4).equals(6));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLastElement() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 9));
		assertTrue(col.get(7).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestEmptyCollection() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		assertFalse(col.remove((Integer) 0));

	}

	@Test
	void toArrayTestEmpty() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		Object[] array = col.toArray();

		for (int i = 0; i < col.size(); i++) {
			assertTrue(array[i] == (Integer) (i + 1));
		}
	}

	@Test
	void toArrayTestNotEmpty() {
		LinkedListIndexedCollection col = makeNumbersCollection();
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
	void forEachTestNotEmpty() {

		class ProcessorSum extends Processor {
			public int sum = 0;

			@Override
			public void process(Object value) {
				sum = sum + (Integer) value;
			}
		}

		ProcessorSum processorSum = new ProcessorSum();
		makeNumbersCollection().forEach(processorSum);
		assertEquals(45, processorSum.sum);

	}

	@Test
	void addAllTestNull() {
		LinkedListIndexedCollection col1 = makeNumbersCollection();
		LinkedListIndexedCollection col2 = null;
		assertThrows(NullPointerException.class, () -> col1.addAll(col2));
	}

	@Test
	void addAllTestBig() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		LinkedListIndexedCollection col2 = makeNumbersCollection();
		col1.addAll(col2);
		assertTrue(col1.size() == col2.size());
		for (int i = 0; i < col1.size(); i++) {
			assertTrue(col1.get(i).equals(col2.get(i)));
		}

	}

	@Test
	void clearTest() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.clear();
		assertTrue(col.size() == 0);
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(0));
	}

	@Test
	void getTestUnreachableTooSmall() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(-2));
	}

	@Test
	void getTestUnreachableTooBig() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(123));
	}

	@Test
	void insertTestPositionTooSmall() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert("ivan", -23));
	}

	@Test
	void insertTestPositionTooBig() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert("ivan", 23));
	}

	@Test
	void insertTestObjectNull() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(NullPointerException.class, () -> col.insert(null, 2));
	}

	@Test
	void insertTestFirst() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.insert(0, 0);
		assertTrue(col.get(0).equals(0));
		assertTrue(col.get(1).equals(1));
		assertTrue(col.get(5).equals(5));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestLast() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.insert(10, 9);
		assertTrue(col.get(9).equals(10));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestMiddle() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.insert(5.5, 5);
		assertTrue(col.get(5).equals(5.5));
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(6).equals(6));
		assertTrue(col.size() == 10);
	}

	@Test
	void indexOfTestNotPresent() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.indexOf("ivan") == -1);
	}

	@Test
	void indexOfTestFirst() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.indexOf(1) == 0);
	}

	@Test
	void indexOfLast() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.indexOf(9) == 8);
	}

	@Test
	void indexOfMiddle() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertTrue(col.indexOf(5) == 4);
	}

	@Test
	void removeTestUnreachableTooSmall() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(-23));
	}

	@Test
	void removeTestUnreachableTooBig() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(23));
	}

	@Test
	void removeTestFirst() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.remove(0);
		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddle() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.remove(5);
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLast() {
		LinkedListIndexedCollection col = makeNumbersCollection();
		col.remove(8);
		assertTrue(col.get(7).equals(8));
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(9));
		assertTrue(col.size() == 8);
	}

	@Test
	void sizeTestSizeIsOkayAfterAddRemoveIndexRemoveObjectAndInsert() {
		LinkedListIndexedCollection col = makeNumbersCollection();

		assertTrue(col.size() == 9);
		col.add("lol1");
		assertTrue(col.size() == 10);
		col.add("lol2");
		assertTrue(col.size() == 11);
		col.add("lol3");
		assertTrue(col.size() == 12);
		col.add("lol4");
		assertTrue(col.size() == 13);
		col.add("lol5");
		assertTrue(col.size() == 14);
		col.remove(0);
		assertTrue(col.size() == 13);
		col.remove(0);
		assertTrue(col.size() == 12);
		col.remove("lol2");
		assertTrue(col.size() == 11);
		col.remove("lol1");
		assertTrue(col.size() == 10);
		col.remove("lol5");
		assertTrue(col.size() == 9);
		col.insert("proba", 0);
		assertTrue(col.size() == 10);
		col.insert("proba", 0);
		assertTrue(col.size() == 11);
		col.insert("proba", 11);
		assertTrue(col.size() == 12);
		col.insert("proba", 0);

		col.clear();
		col.add(1);
		col.add((Integer) 1);
		assertTrue(col.size() == 2);
		col.insert(2, 0);
		col.insert(3, 3);
		assertTrue(col.size() == 4);
	}

	// constructors

	@Test
	void defaultConstructorTest() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		assertTrue(col.isEmpty());

	}

	@Test
	void constructorTestOtherCollection() {
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
	}

	@Test
	void constructorTestOtherCollectionNull() {
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(makeNumbersCollection());
		Object[] col1Array = makeNumbersCollection().toArray();
		Object[] col2Array = col2.toArray();

		for (int i = 0; i < col1Array.length; i++) {
			assertTrue(col1Array[i] == col2Array[i]);
		}
	}

}
