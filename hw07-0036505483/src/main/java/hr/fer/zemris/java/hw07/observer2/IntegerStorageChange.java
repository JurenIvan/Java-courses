package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Class that models a "change report". Has old value, new value and reference
 * to source of change.
 * 
 * @author juren
 *
 */
public class IntegerStorageChange {
	/**
	 * Variable that stores reference to source of change
	 */
	private IntegerStorage reference;
	/**
	 * Variable that stores old value.
	 */
	private int valueBefore;
	/**
	 * Variable that stores new(current) value of source of change
	 */
	private int value;

	/**
	 * Standard constructor for this method. Has old value, new value and reference
	 * to source of change as arguments.
	 * 
	 * @param reference   reference to IntegerStorage that caused the change
	 * @param valueBefore value that was previous state
	 * @param valueNow    value that is relevant
	 * 
	 * @throws NullPointerException if referenced source is null
	 */
	public IntegerStorageChange(IntegerStorage reference, int valueBefore, int valueNow) {
		this.reference = Objects.requireNonNull(reference, "referenced source cannot be null");
		this.valueBefore = valueBefore;
		this.value = valueNow;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the reference
	 */
	public IntegerStorage getReference() {
		return reference;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the valueBefore
	 */
	public int getValueBefore() {
		return valueBefore;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the valueNow
	 */
	public int getValue() {
		return value;
	}

}
