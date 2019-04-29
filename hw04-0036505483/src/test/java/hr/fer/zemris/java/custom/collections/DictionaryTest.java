package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DictionaryTest {

	@Test
	void isEmptyTrueTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		assertTrue(dictonary.isEmpty());
	}

	@Test
	void isEmptyFalseTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		assertFalse(dictonary.isEmpty());
	}

	@Test
	void clearTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		assertFalse(dictonary.isEmpty());
		dictonary.clear();
		assertTrue(dictonary.isEmpty());
		assertEquals(null,dictonary.get("Ivan"));
	}

	@Test
	void putTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Sup", "BOOIII");
		
		assertFalse(dictonary.isEmpty());
		assertEquals("Juren",dictonary.get("Ivan"));
		assertEquals("BOOIII",dictonary.get("Sup"));
		assertEquals(2, dictonary.size());
	}

	@Test
	void putThatOverridesTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Ivan", "BOOIII");
		
		assertFalse(dictonary.isEmpty());
		assertNotEquals("Juren",dictonary.get("Ivan"));
		assertEquals("BOOIII",dictonary.get("Ivan"));
		assertEquals(1, dictonary.size());
	}

	@Test
	void putNullKeyTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		assertThrows(NullPointerException.class, ()->dictonary.put(null, "123"));;
	}

	@Test
	void getNonExistanTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Ivan", "BOOIII");
		assertEquals(null,dictonary.get("wut wut"));
	}

	@Test
	void getTest() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		dictonary.put("Ivan", "Juren");
		dictonary.put("Juren", "BOOIII");
		
		assertFalse(dictonary.isEmpty());
		assertEquals("Juren",dictonary.get("Ivan"));
		assertEquals("BOOIII",dictonary.get("Juren"));
		assertEquals(null, dictonary.get(null));
	}
	
	@Test
	void size() {
		Dictionary<String, String> dictonary = new Dictionary<>();
		assertTrue(dictonary.size()==0);
		dictonary.put("Ivan", "Juren");
		assertTrue(dictonary.size()==1);
		dictonary.put("Juren", "BOOIII");
		assertTrue(dictonary.size()==2);
		dictonary.clear();
		assertTrue(dictonary.size()==0);
	}
}
	
