package hr.fer.zemris.java.hw07.observer1;

/**
 * Concrete implementation of Observer. Waits for change and then prints
 * predefined message and value multiplied with 2. Can be triggered only
 * predefined number of times. After that, it deletes itself from list of
 * observers.
 * 
 * @author juren
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	/**
	 * Variable defining number of triggers before deactivation
	 */
	private int numberOfUsesLeft;

	/**
	 * Standard constructor that sets number of triggers.
	 * 
	 * @param numbersOfUses number of times observer does it's job
	 * @throws IllegalArgumentException if provided argument is negative
	 */
	public DoubleValue(int numbersOfUses) {
		if (numbersOfUses < 0)
			throw new IllegalArgumentException("Number of uses cannot be negative.");
		this.numberOfUsesLeft = numbersOfUses;
	}

	@Override
	public void valueChanged(IntegerStorage istorage) {
		if (numberOfUsesLeft-- > 0) {
			System.out.println("Double value: " + istorage.getValue() * 2);
			return;
		}
		if (numberOfUsesLeft <= 0) {
			istorage.removeObserver(this);
		}
	}

}
