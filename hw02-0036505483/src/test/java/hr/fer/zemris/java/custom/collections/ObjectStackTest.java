package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjectStackTest {

	@Test
	void isEmptytestEmpty() {
		ObjectStack os=new ObjectStack();
		assertTrue(os.isEmpty());
	}
	@Test
	void isEmptytestNotEmpty() {
		ObjectStack os=new ObjectStack();
		os.push("ivan");
		os.pop();
		assertTrue(os.isEmpty());
		os.push(2);
		assertFalse(os.isEmpty());
	}
	
	@Test
	void popTest() {
		ObjectStack os=new ObjectStack();
		Object something="Ivan";
		os.push(something);
		assertTrue(something.equals(os.pop()));
		assertTrue(os.isEmpty());
	}
	
	@Test
	void popTestEmptyStack() {
		ObjectStack os=new ObjectStack();
		assertThrows(EmptyStackException.class, () -> os.pop());
	}
	
	@Test
	void peekTest() {
		ObjectStack os=new ObjectStack();
		Object something="Ivan";
		os.push(something);
		assertTrue(something.equals(os.peek()));
		assertTrue(something.equals(os.peek()));
		assertFalse(os.isEmpty());
		assertTrue(something.equals(os.pop()));
		assertTrue(os.isEmpty());
	}
	
	@Test
	void peekTestEmptyStack() {
		ObjectStack os=new ObjectStack();
		assertThrows(EmptyStackException.class, () -> os.peek());
	}
	
	
	@Test
	void test() {
		ObjectStack os=new ObjectStack();
		os.push(123);
		os.push(123);
		os.push(123);
		os.clear();
		assertTrue(os.isEmpty());
	}
	

}
