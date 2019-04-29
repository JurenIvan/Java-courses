package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Test;

class ObjectMultistackTest {

	@Test
	void testPushPushKey() {
		ObjectMultistack om = new ObjectMultistack();

		om.push("ivan", new ValueWrapper("ren"));
		om.push("ivan", new ValueWrapper("Ju"));
		assertEquals("Ju", om.pop("ivan").getValue());
		assertEquals("ren", om.pop("ivan").getValue());

		assertTrue(om.isEmpty("ivan"));
	}

	@Test
	void testWrongKey() {
		ObjectMultistack om = new ObjectMultistack();

		om.push("ivan", new ValueWrapper("ren"));
		om.push("ivan", new ValueWrapper("Ju"));
		assertThrows(EmptyStackException.class, ()->om.pop("trololo"));
	}
	
	@Test
	void testMultiStackRetrieve() {
		ObjectMultistack om = new ObjectMultistack();

		om.push("ivan", new ValueWrapper(2));
		om.push("ivan", new ValueWrapper(null));
		om.push("ivan", new ValueWrapper(1));
		
		
		assertEquals(1, om.pop("ivan").getValue());
		assertEquals(null, om.pop("ivan").getValue());
		assertEquals(2, om.pop("ivan").getValue());
		
		
	}

	@Test
	void testPushKey() {
		ObjectMultistack om = new ObjectMultistack();

		om.push("ivan", new ValueWrapper("ren"));
		assertEquals("ren", om.pop("ivan").getValue());

		assertTrue(om.isEmpty("ivan"));
	}

	@Test
	void testPushKeyNull() {
		ObjectMultistack om = new ObjectMultistack();

		assertThrows(NullPointerException.class, () -> om.push(null, new ValueWrapper("ren")));
	}

	@Test
	void testPushPeek() {
		ObjectMultistack om = new ObjectMultistack();

		om.push("ivan", new ValueWrapper("ren"));
		assertEquals("ren", om.peek("ivan").getValue());

		assertFalse(om.isEmpty("ivan"));
	}

	@Test
	void testIsEmptyTrue() {
		ObjectMultistack om = new ObjectMultistack();
		om.push("ivan", new ValueWrapper("ren"));
		assertTrue(om.isEmpty("juren"));
	}

	@Test
	void testIsEmptyFalse() {
		ObjectMultistack om = new ObjectMultistack();
		om.push("ivan", new ValueWrapper("ren"));
		assertFalse(om.isEmpty("ivan"));
	}

}
