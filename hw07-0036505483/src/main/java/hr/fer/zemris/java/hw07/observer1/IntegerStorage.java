package hr.fer.zemris.java.hw07.observer1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class that models source of update/change. Has list of observers that it
 * notifies of value update.
 * 
 * @author juren
 *
 */
public class IntegerStorage {
	/**
	 * Variable that stores current value;
	 */
	private int value;
	
	/**
	 * List that stores observers that should be triggered when update happens.
	 */
	private List<IntegerStorageObserver> observers;

	/**
	 * Standard constructor that sets initialValue.
	 * 
	 * @param initialValue initial value.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
	}

	/**
	 * Method that adds objects of type {@link IntegerStorageObserver} that get
	 * notified when change is registered.
	 * 
	 * @param observer {@link IntegerStorageObserver} object that gets notified when
	 *                 change happens
	 * @throws NullPointerException when provided observer is null reference
	 */
	public void addObserver(IntegerStorageObserver observer) {
		Objects.requireNonNull(observer, "Referenced observer can not be null reference.");
		if (observers.contains(observer))
			return;
		
		observers=new ArrayList<IntegerStorageObserver>(observers);
		observers.add(observer);
	}

	/**
	 * Method that removes object of type {@link IntegerStorageObserver} that is
	 * referenced from list of observers that get notified upon change.
	 * 
	 * @param observer observer to be removed.
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (!observers.contains(observer))
			return;
		observers=new ArrayList<IntegerStorageObserver>(observers);
		observers.remove(observer);
	}

	/**
	 * Method that clears list of observers.
	 */
	public void clearObservers() {
		observers=new ArrayList<IntegerStorageObserver>();
	}

	/**
	 * standard getter for value
	 * 
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Method that notifies all registered observers of change that happened if new
	 * value differs from old one.
	 * 
	 * @param value new value that caused update
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			// Update current value
			this.value = value;
			// Notify all registered observers
			if (observers != null) {
				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(this);
				}
			}
		}
	}

}
