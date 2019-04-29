package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class that hold implementation of iterator for prime numbers. Prime is
 * calculated only when needed.
 * 
 * @author juren
 *
 */
public class PrimesCollection implements Iterable<Integer> {
	/**
	 * Variable storing number of primes that can be returned
	 */
	private int howManyInCollection;

	/**
	 * Constructor of this class accepts a number of consecutive primes that must be
	 * in this collection
	 * 
	 * @param howManyInCollection number of consecutive primes that must be in this
	 *                            collection
	 */
	public PrimesCollection(int howManyInCollection) {
		if (howManyInCollection < 0)
			throw new IllegalArgumentException("Collection with negative number of elements doesn't exist");
		this.howManyInCollection = howManyInCollection;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new iteratorImpl(this.howManyInCollection);
	}

	/**
	 * Implementation of iterator that iterates through primes and calculates next
	 * one only on demand.
	 * 
	 * @author juren
	 *
	 */
	private static class iteratorImpl implements Iterator<Integer> {
		/**
		 * Variable storing number of primes that can be returned
		 */
		private int howManyToReturn;
		/**
		 * Variable that stores last calculated prime.
		 */
		private int lastPrime;

		/**
		 * Basic constructor that gets number of primes that will be returned.
		 * 
		 * @param howManyToReturn
		 */
		public iteratorImpl(int howManyToReturn) {
			this.howManyToReturn = howManyToReturn;
			lastPrime = 1;
		}

		@Override
		public boolean hasNext() {
			return howManyToReturn > 0;
		}

		@Override
		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException("No more primes available!");
			while (!isPrime(++lastPrime));
			howManyToReturn--;
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
			if(number==1) return false;	
			return true;
		}

	}

}
