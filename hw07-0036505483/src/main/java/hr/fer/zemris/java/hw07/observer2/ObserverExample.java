package hr.fer.zemris.java.hw07.observer2;

/**
 * Simple class containing main method used to demonstrate functionality of
 * observers
 * 
 * @author juren
 *
 */
public class ObserverExample {
	/**
	 * Main method used to start program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		
		istorage.removeObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));		
		
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);

	}
}
