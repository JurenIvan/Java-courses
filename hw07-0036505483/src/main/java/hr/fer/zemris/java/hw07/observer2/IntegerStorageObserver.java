package hr.fer.zemris.java.hw07.observer2;

/**
 * Interface containing list of method used to define Observer.
 * 
 * @author juren
 *
 */
public interface IntegerStorageObserver {
	/**
	 * Method that defines action of observer when change is detected.
	 * 
	 * @param istorage reference to IntegerStorageChange that hold data relevant for
	 *                 this particular update in value
	 */
	public void valueChanged(IntegerStorageChange istorage);
}