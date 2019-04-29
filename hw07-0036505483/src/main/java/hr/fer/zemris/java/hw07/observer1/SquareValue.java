package hr.fer.zemris.java.hw07.observer1;

/**
 * Concrete implementation of Observer. Waits for change and then prints
 * predefined message and square of value.
 * 
 * @author juren
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Provided new value: " + istorage.getValue() + ", square is "
				+ (long) Math.pow(istorage.getValue(), 2));
	}

}
