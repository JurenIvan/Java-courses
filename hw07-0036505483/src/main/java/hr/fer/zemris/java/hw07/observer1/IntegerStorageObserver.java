package hr.fer.zemris.java.hw07.observer1;

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
	 * @param istorage reference to IntegerStorage that caused the change
	 */
	public void valueChanged(IntegerStorage istorage);
}