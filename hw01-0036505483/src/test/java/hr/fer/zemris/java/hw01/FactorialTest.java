package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FactorialTest {

	@Test
	void factorialTestNegative() {
		assertThrows(IllegalArgumentException.class, ()->Factorial.getFactorial(-12));
	}
	
	@Test
	void factorialTestTooMuch() {
		assertThrows(IllegalArgumentException.class, ()->Factorial.getFactorial(22));
	}
	
	@Test
	void factorialTestNormal() {
		assertEquals(120, Factorial.getFactorial(5));
		assertEquals(1, Factorial.getFactorial(1));
		assertEquals(2, Factorial.getFactorial(2));
		assertEquals(6, Factorial.getFactorial(3));
		assertEquals(24, Factorial.getFactorial(4));
		
	}
	
	@Test
	void factorialTestZero() {
		assertEquals(1, Factorial.getFactorial(0));
	}
	
}



