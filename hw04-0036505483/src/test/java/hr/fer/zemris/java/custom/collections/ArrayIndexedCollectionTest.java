package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection<Integer> makeNumbersCollection() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<>(2);
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
	private ArrayIndexedCollection<Integer> makeNumbersCollectionInsertVersion() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<>(2);
		col.insert(1,0);
		col.insert(2,1);
		col.insert(4,2);
		col.insert(5,3);
		col.insert(3,2);
		col.insert(6,5);
		col.insert(7,6);
		col.insert(8,7);
		col.insert(9,8);
		return col;
	}

	@Test
	void isEmptyTestEmpty() {
		assertTrue(new ArrayIndexedCollection<Integer>().isEmpty());
	}

	@Test
	void isEmptyTestNotEmpty() {
		assertFalse(makeNumbersCollection().isEmpty());
	}

	@Test
	void sizeTestEmpty() {
		assertTrue(new ArrayIndexedCollection<Integer>(2).size() == 0);
	}

	@Test
	void sizeTestNotEmptyJustAdding() {
		assertTrue(makeNumbersCollection().size() == 9);
	}

	@Test
	void sizeTestNotEmptyAfterAddingAndRemoving() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertFalse(col.remove((Integer) 101));
		assertTrue(col.size() == 8);
	}

	@Test
	void addTestNull() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>(2);

		assertThrows(NullPointerException.class, () -> col.add(null));
	}

	@Test
	void addTestNotANull() {
		ArrayIndexedCollection<String> col = new ArrayIndexedCollection<String>(2);
		col.add("ivan");
		assertTrue(col.size() == 1);
		assertTrue(col.contains("ivan"));
		assertTrue(col.get(0).equals("ivan"));
		assertFalse(col.isEmpty());
	}

	@Test
	void addTestContainsTest() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
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
	void addTestContainsTestInsertVersion() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollectionInsertVersion();
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
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>(2);

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
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(0);
		col.add(100);

		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.get(8).equals(100));


	}

	@Test
	void addTestAddAfterEmptied() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
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
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.clear();
		col.add(1);
		col.add(2);
		col.add(3);
		assertTrue(col.get(0).equals(1));
		assertTrue(col.get(1).equals(2));


	}

	@Test
	void containsTestNull() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.contains(null));
	}

	@Test
	void containsTestFalse() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.contains("ivan"));
	}

	@Test
	void containsTestTrueFirst() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(1));
	}

	@Test
	void containsTestTrueMiddle() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(4));
	}

	@Test
	void containsTestTrueEndOfArray() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.contains(9));
	}

	@Test
	void removeTestNull() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.remove(null));
	}

	@Test
	void removeTestNotInCollection() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertFalse(col.remove("ivan"));
		assertTrue(col.get(8).equals(9));
		assertTrue(col.size() == 9);
	}

	@Test
	void removeTestFirstElement() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 1));
		assertTrue(col.get(0).equals(2));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddleElement() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 5));
		assertTrue(col.get(3).equals(4));
		assertTrue(col.get(4).equals(6));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLastElement() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.remove((Integer) 9));
		assertTrue(col.get(7).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestEmptyCollection() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>(2);
		assertFalse(col.remove((Integer) 0));

	}

	@Test
	void toArrayTestEmpty() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>();
		Object[] array = col.toArray();

		for (int i = 0; i < col.size(); i++) {
			assertTrue(array[i] == (Integer) (i + 1));
		}
	}

	@Test
	void toArrayTestNotEmpty() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
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
		ArrayIndexedCollection<Integer> col1 = makeNumbersCollection();
		ArrayIndexedCollection<Integer> col2 = null;
		assertThrows(NullPointerException.class, () -> col1.addAll(col2));
	}

	@Test
	void addAllTestBig() {
		ArrayIndexedCollection<Integer> col1 = new ArrayIndexedCollection<Integer>();
		ArrayIndexedCollection<Integer> col2 = makeNumbersCollection();
		col1.addAll(col2);
		assertTrue(col1.size() == col2.size());
		for (int i = 0; i < col1.size(); i++) {
			assertTrue(col1.get(i).equals(col2.get(i)));
		}

	}

	@Test
	void clearTest() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.clear();
		assertTrue(col.size() == 0);
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(0));
	}

	@Test
	void getTestUnreachableTooSmall() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(-2));
	}

	@Test
	void getTestUnreachableTooBig() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(123));
	}

	@Test
	void insertTestPositionTooSmall() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert(2, -23));
	}

	@Test
	void insertTestPositionTooBig() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert(2, 23));
	}

	@Test
	void insertTestObjectNull() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(NullPointerException.class, () -> col.insert(null, 2));
	}

	@Test
	void insertTestFirst() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(0, 0);
		assertTrue(col.get(0).equals(0));
		assertTrue(col.get(1).equals(1));
		assertTrue(col.get(5).equals(5));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestLast() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(10, 9);
		assertTrue(col.get(9).equals(10));
		assertTrue(col.size() == 10);
	}

	@Test
	void insertTestMiddle() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.insert(5, 5);
		assertTrue(col.get(5).equals(5));
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(6).equals(6));
		assertTrue(col.size() == 10);
	}

	@Test
	void indexOfTestNotPresent() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf("ivan") == -1);
	}

	@Test
	void indexOfTestFirst() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(1) == 0);
	}

	@Test
	void indexOfLast() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(9) == 8);
	}

	@Test
	void indexOfMiddle() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertTrue(col.indexOf(5) == 4);
	}

	@Test
	void removeTestUnreachableTooSmall() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(-23));
	}

	@Test
	void removeTestUnreachableTooBig() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(23));
	}

	@Test
	void removeTestFirst() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(0);
		assertTrue(col.get(0).equals(2));
		assertTrue(col.get(1).equals(3));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestMiddle() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(5);
		assertTrue(col.get(4).equals(5));
		assertTrue(col.get(5).equals(7));
		assertTrue(col.get(6).equals(8));
		assertTrue(col.size() == 8);
	}

	@Test
	void removeTestLast() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();
		col.remove(8);
		assertTrue(col.get(7).equals(8));
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(9));
		assertTrue(col.size() == 8);
	}

	@Test
	void sizeTestSizeIsOkayAfterAddRemoveIndexRemoveObjectAndInsert() {
		ArrayIndexedCollection<Integer> col = makeNumbersCollection();

		assertTrue(col.size() == 9);
		col.add(1);
		assertTrue(col.size() == 10);
		col.add(2);
		assertTrue(col.size() == 11);
		col.add(3);
		assertTrue(col.size() == 12);
		col.add(4);
		assertTrue(col.size() == 13);
		col.add(5);
		assertTrue(col.size() == 14);
		col.remove(0);
		assertTrue(col.size() == 13);
		col.remove(0);
		assertTrue(col.size() == 12);
	

	}

	// constructors

	@Test
	void testdefaultConstructor() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>();
		assertTrue(col.size()==0);
	}

	@Test
	void testconstructorWithPredefinedArraySizeLessThanOne() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<Integer>(0));
	}

	@Test
	void testconstructorWithPredefinedArraySize() {
		ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<Integer>(123);
		assertTrue(col.size()==0);
	}

	@Test
	void testconstructorWithPredefinedArraySizeAndCollectionNULL() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<Integer>(null, 123));
	}

	@Test
	void testconstructorWithPredefinedArraySizeAndCollection() {
		ArrayIndexedCollection<Integer> col1 = makeNumbersCollection();
		ArrayIndexedCollection<Integer> col2 = new ArrayIndexedCollection<Integer>(col1, 123);

		Object[] col1Array = col1.toArray();
		Object[] col2Array = col2.toArray();

		for (int i = 0; i < col1Array.length; i++) {
			assertTrue(col1Array[i] == col2Array[i]);
		}
	}

	@Test
	void testconstructorWithOtherCollectionNULL() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<Integer>(null));
	}

	@Test
	void testconstructorWithPredefinedArraySizeSmallerThanOtherCollection() {
		ArrayIndexedCollection<Integer> col1 = makeNumbersCollection();
		ArrayIndexedCollection<Integer> col2 = new ArrayIndexedCollection<>(col1, -2);

		Object[] col1Array = col1.toArray();
		Object[] col2Array = col2.toArray();

		for (int i = 0; i < col1Array.length; i++) {
			assertTrue(col1Array[i] == col2Array[i]);
		}
	}

	@Test
	void testconstructorWithOtherCollection() {
		ArrayIndexedCollection<Integer> col1 = makeNumbersCollection();
		ArrayIndexedCollection<Integer> col2 = new ArrayIndexedCollection<Integer>(col1);

		Object[] col1Array = col1.toArray();
		Object[] col2Array = col2.toArray();

		for (int i = 0; i < col1Array.length; i++) {
			assertTrue(col1Array[i] == col2Array[i]);
		}
	}
}
