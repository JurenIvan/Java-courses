package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjectStackTest {

	@Test
	void isEmptytestEmpty() {
		ObjectStack<Integer> os=new ObjectStack<>();
		assertTrue(os.isEmpty());
	}
	@Test
	void isEmptytestNotEmpty() {
		ObjectStack<Integer> os=new ObjectStack<>();
		os.push(2);
		os.pop();
		assertTrue(os.isEmpty());
		os.push(2);
		assertFalse(os.isEmpty());
	}
	
	@Test
	void popTest() {
		ObjectStack<String> os=new ObjectStack<>();
		String something="Ivan";
		os.push(something);
		assertTrue(something.equals(os.pop()));
		assertTrue(os.isEmpty());
	}
	
	@Test
	void popTestEmptyStack() {
		ObjectStack<Integer> os=new ObjectStack<>();
		assertThrows(EmptyStackException.class, () -> os.pop());
	}
	
	@Test
	void peekTest() {
		ObjectStack<String> os=new ObjectStack<>();
		String something="Ivan";
		os.push(something);
		assertTrue(something.equals(os.peek()));
		assertTrue(something.equals(os.peek()));
		assertFalse(os.isEmpty());
		assertTrue(something.equals(os.pop()));
		assertTrue(os.isEmpty());
	}
	
	@Test
	void peekTestEmptyStack() {
		ObjectStack<Integer> os=new ObjectStack<>();
		assertThrows(EmptyStackException.class, () -> os.peek());
	}
	
	
	@Test
	void test() {
		ObjectStack<Integer> os=new ObjectStack<>();
		os.push(123);
		os.push(123);
		os.push(123);
		os.clear();
		assertTrue(os.isEmpty());
	}
	

}
