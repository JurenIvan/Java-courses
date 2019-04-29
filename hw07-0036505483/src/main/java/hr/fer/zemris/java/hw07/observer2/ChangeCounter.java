package hr.fer.zemris.java.hw07.observer2;
/**
 * Concrete implementation of Observer. Waits for change and then prints
 * predefined message and number of times it has been triggered so far
 * 
 * @author juren
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	/**
	 * Number of times it has been triggered
	 */
	private long numberOfChanges;

	/**
	 * Standard constructor that sets number of triggers so far to 0.
	 */
	public ChangeCounter() {
		numberOfChanges=0;
	}
	
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		System.out.println("Number of value changes since tracking: " + ++numberOfChanges);
	}

}
