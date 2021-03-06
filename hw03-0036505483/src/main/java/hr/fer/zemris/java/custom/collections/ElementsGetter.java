package hr.fer.zemris.java.custom.collections;

public interface ElementsGetter {
	/**
	 * Method that returns boolean representing availability of nextElement. Should
	 * be used before getNextElement method.
	 * 
	 * If any change is made to collection, method cannot insure that the next
	 * element exists so it throws exception
	 * 
	 * @throws ConcurrentModificationException if collection has been changed
	 * 
	 */
	boolean hasNextElement();

	/**
	 * Method that returns next element stored in collection. If such is not present
	 * or collection has been modified, an exception is thrown.
	 * 
	 * @throws ConcurrentModificationException if collection has been changed
	 * @throws NoSuchElementException          if the end of the collection has been
	 *                                         reached
	 */
	Object getNextElement();

	/**
	 * EQUIVALENT TO processRemaining.
	 * 
	 * Method that is called upon ElementGetter and receives {@link Processor} so it
	 * performs it's action over all elements that ElementGetter can get.
	 * 
	 * @param p functional interface with method process whose action is applied to
	 *          every element that ElementGetter can give back.
	 */
	default void forEachRemaining(Processor p) {
		processRemaining(p);
	}

	/**
	 * EQUIVALENT TO forEachRemaining.
	 * 
	 * Method that is called upon ElementGetter and receives {@link Processor} so it
	 * performs it's action over all elements that ElementGetter can get.
	 * 
	 * @param p functional interface with method process whose action is applied to
	 *          every element that ElementGetter can give back.
	 */
	default void processRemaining(Processor p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
