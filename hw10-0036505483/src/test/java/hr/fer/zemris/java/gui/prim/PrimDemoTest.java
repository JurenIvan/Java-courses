package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.gui.prim.PrimDemo.DemoListModel;

class PrimDemoTest {

	@Test
	void testSize() {
		DemoListModel dlm=new DemoListModel();
		dlm.add(3);
		assertEquals(2, dlm.getSize());
	}
	
	@Test
	void testSize2() {
		DemoListModel dlm=new DemoListModel();
		assertEquals(1, dlm.getSize());
		dlm.add(3);
		assertEquals(2, dlm.getSize());
		dlm.add(3);
		assertEquals(3, dlm.getSize());
		dlm.add(3);
		assertEquals(4, dlm.getSize());
	}
	
	@Test
	void testSize3() {
		DemoListModel dlm=new DemoListModel();
		assertEquals(1, dlm.getSize());
	}
	
	@Test
	void testGetElementAtIndex1() {
		DemoListModel dlm=new DemoListModel();
		dlm.add(3);
		
		assertEquals(3, dlm.getElementAt(1));
	}
	
	@Test
	void testGetElementAtIndex2() {
		DemoListModel dlm=new DemoListModel();
		dlm.add(3);
		dlm.add(4);
		assertEquals(4, dlm.getElementAt(2));
	}
	
	@Test
	void testGetElementAtIndex3() {
		DemoListModel dlm=new DemoListModel();
		dlm.add(3);
		dlm.add(4);
		dlm.add(5);
		assertEquals(5, dlm.getElementAt(3));
	}
	
	@Test
	void test() {
		
		iteratorImpl iter=new iteratorImpl();
		
		assertEquals(iter.next(), 2);
		assertEquals(iter.next(), 3);
		assertEquals(iter.next(), 5);
		assertEquals(iter.next(), 7);
		assertEquals(iter.next(), 11);		
	}

	private static class iteratorImpl implements Iterator<Integer> {

		/**
		 * Variable that stores last calculated prime.
		 */
		private int lastPrime;

		/**
		 * Basic constructor that gets number of primes that will be returned.
		 * 
		 * @param howManyToReturn
		 */
		public iteratorImpl() {
			lastPrime = 1;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException("No more primes available!");
			while (!isPrime(++lastPrime))
				;
			return lastPrime;
		}

		/**
		 * Method that analyzes whether a given number is prime or not. works in
		 * O(sqrt(n)/2) time.
		 * 
		 * @param number candidate for being a prime
		 * @return prime-ness of number
		 */
		private boolean isPrime(int number) {
			if (number % 2 == 0 && number > 2)
				return false;

			for (int i = 3; i <= Math.sqrt(number); i = i + 2) {
				if (number % i == 0)
					return false;
			}
			if (number == 1)
				return false;
			return true;
		}

	}
	
	
}
