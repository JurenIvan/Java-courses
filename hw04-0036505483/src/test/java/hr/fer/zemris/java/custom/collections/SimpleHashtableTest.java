package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;

import org.junit.jupiter.api.Test;

class SimpleHashtableTest {

	@Test
	void constructorTooBigArgumentTest() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, String>(Integer.MAX_VALUE - 23));
	}

	@Test
	void constructorNegativeArgumentTest() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, String>(-23));
	}

	@Test
	void containsTrueTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		assertTrue(dictonary.containsKey("Ivan"));
	}

	@Test
	void containsFalseTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		assertFalse(dictonary.containsKey("Juren"));
	}

	@Test
	void putToCauseDouble() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>(2);
		dictonary.put("Ivan", "Juren");
		dictonary.put("Mans", "Not");
		dictonary.put("Hot", "Never");
		dictonary.put("Hot", "Never2");
		dictonary.put("Two", "Two");

		assertEquals(4, dictonary.size());

	}

	@Test
	void iteratoriteratingTest() {
		SimpleHashtable<Integer, Integer> dictonary = new SimpleHashtable<>(2);
		dictonary.put(1, 10);
		dictonary.put(2, 20);
		dictonary.put(3, 30);
		String s = "";
		for (var pair1 : dictonary) {
			for (var pair2 : dictonary) {
				s = s + pair1.getKey() + pair1.getValue() + pair2.getKey() + pair2.getValue();
			}
		}
		
		assertEquals("110110110220110330220110220220220330330110330220330330",s);
	}
	
	@Test
	void iteratorRemoveTest() {
		SimpleHashtable<Integer, Integer> dictonary = new SimpleHashtable<>(2);
		dictonary.put(1, 10);
		dictonary.put(123,0);
		
		dictonary.put(2, 20);
		dictonary.put(3, 30);
		var iterator=dictonary.iterator();
		iterator.next();
		iterator.next();
		iterator.next();
		iterator.remove();
		
		String s = "";
		for (var pair1 : dictonary) {
			for (var pair2 : dictonary) {
				s = s + pair1.getKey() + pair1.getValue() + pair2.getKey() + pair2.getValue();
			}
		}
		
		assertEquals("110110110220110330220110220220220330330110330220330330",s);
	}
	
	@Test
	void IteratorRemovesTwoArgumentsInRowTest() {
		SimpleHashtable<Integer, Integer> dictonary = new SimpleHashtable<>(2);
		dictonary.put(1, 10);
		dictonary.put(123,0);
		
		var iterator=dictonary.iterator();
		iterator.next();
		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class,()->iterator.remove());
		
	}
	
	
	@Test
	void IteratorConcurentNOTException() {
		SimpleHashtable<Integer, Integer> dictonary = new SimpleHashtable<>(20);
		dictonary.put(1, 10);
		dictonary.put(123,0);
		
		var iterator=dictonary.iterator();
		dictonary.put(123, 2);
		iterator.next();
	}
	
	@Test
	void IteratorConcurentException() {
		SimpleHashtable<Integer, Integer> dictonary = new SimpleHashtable<>(20);
		dictonary.put(1, 10);
		dictonary.put(123,0);
		
		var iterator=dictonary.iterator();
		dictonary.put(122, 2);
		assertThrows(ConcurrentModificationException.class, ()->iterator.next());
	}
	
	@Test
	void findbyValue() {
		SimpleHashtable<String,Integer> dictonary = new SimpleHashtable<>(2);
		dictonary.put("Ivana", 1);
		dictonary.put("Ante", 2);
		dictonary.put("Jasna", 3);
		dictonary.put("Kristina", 4);
		assertTrue(dictonary.containsValue(1));
		assertTrue(dictonary.containsValue(2));
		assertTrue(dictonary.containsValue(3));
		assertTrue(dictonary.containsValue(4));
		assertFalse(dictonary.containsValue(0));
	}
	
	@Test
	void toStringTest() {
		SimpleHashtable<String,Integer> dictonary = new SimpleHashtable<>(2);
		dictonary.put("Ivana", 1);
		dictonary.put("Ante", 2);
		dictonary.put("Jasna", 3);
		dictonary.put("Kristina", 4);
		assertEquals("[Ante=2, Ivana=1, Jasna=3, Kristina=4]", dictonary.toString().trim());
	}
	
	@Test
	void remove() {
		// create collection:
		SimpleHashtable<String,Integer> dictonary = new SimpleHashtable<>(2);
		// fill data:
		dictonary.put("Ivana", 2);
		dictonary.put("Ante", 2);
		dictonary.put("Jasna", 2);
		dictonary.put("Kristina", 5);
		dictonary.put("Ivana", 5); // overwrites old grade for Ivana
		
		assertTrue(dictonary.size()==4);
		dictonary.remove(null);
		assertTrue(dictonary.size()==4);
		dictonary.remove("Ivana");
		assertTrue(dictonary.size()==3);
		dictonary.remove("Kristina");
		assertTrue(dictonary.size()==2);
		dictonary.remove("Jasna");
		assertTrue(dictonary.size()==1);
		dictonary.remove("Ante");
		assertTrue(dictonary.size()==0);
		
	}

	@Test
	void isEmptyTrueTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		assertTrue(dictonary.isEmpty());
	}

	@Test
	void isEmptyFalseTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		assertFalse(dictonary.isEmpty());
	}

	@Test
	void clearTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		assertFalse(dictonary.isEmpty());
		dictonary.clear();
		assertTrue(dictonary.isEmpty());
		assertEquals(null, dictonary.get("Ivan"));
	}

	@Test
	void putTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Sup", "BOOIII");

		assertFalse(dictonary.isEmpty());
		assertEquals("Juren", dictonary.get("Ivan"));
		assertEquals("BOOIII", dictonary.get("Sup"));
		assertEquals(2, dictonary.size());
	}

	@Test
	void putThatOverridesTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Ivan", "BOOIII");

		assertFalse(dictonary.isEmpty());
		assertNotEquals("Juren", dictonary.get("Ivan"));
		assertEquals("BOOIII", dictonary.get("Ivan"));
		assertEquals(1, dictonary.size());
	}

	@Test
	void putNullKeyTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		assertThrows(NullPointerException.class, () -> dictonary.put(null, "123"));
		;
	}

	@Test
	void getNonExistanTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Ivan", "BOOIII");
		assertEquals(null, dictonary.get("wut wut"));
	}

	@Test
	void getTest() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Juren", "BOOIII");

		assertFalse(dictonary.isEmpty());
		assertEquals("Juren", dictonary.get("Ivan"));
		assertEquals("BOOIII", dictonary.get("Juren"));
		assertEquals(null, dictonary.get(null));
	}

	@Test
	void size() {
		SimpleHashtable<String, String> dictonary = new SimpleHashtable<>();
		assertTrue(dictonary.size() == 0);
		dictonary.put("Ivan", "Juren");
		assertTrue(dictonary.size() == 1);
		dictonary.put("Juren", "BOOIII");
		assertTrue(dictonary.size() == 2);
		dictonary.clear();
		assertTrue(dictonary.size() == 0);
	}

}
