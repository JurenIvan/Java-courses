package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

//with no javadoc, body of class, with all proper formating, is under 80 lines long
/**
 * Class that models a custom Value wrapper. Any dataType can be stored in this
 * structure and arithmetic operations are allowed and defined when those types
 * are string,double,integer or any type but value is null.
 * 
 * @author juren
 *
 */
public class ValueWrapper {
	/**
	 * Variable that stores data.
	 */
	private Object value;

	/**
	 * Standard constructor that stores data into variables.
	 * 
	 * @param value that shall be stored
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Standard getter for value
	 * 
	 * @return value stored in Structure
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Standard setter for value
	 * 
	 * @param value to be set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Method that checks whether arithmetic operation is allowed with passed object
	 * 
	 * @param valueToCheck data object whose type and value gets checked
	 * @param message      message that is encapsulated in exception if no such
	 *                     operation is allowed
	 * @throws RuntimeException if type of data is not appropriate for arithmetic
	 *                          operations.
	 */
	private void checkForTypeOfArgument(Object valueToCheck, String message) {
		if (!(valueToCheck == null || valueToCheck instanceof Integer || valueToCheck instanceof Double
				|| valueToCheck instanceof String))
			throw new RuntimeException(message);
	}

	/**
	 * Method that checks both stored value whether their value or type is
	 * appropriate for arithmetic operation and "normalizes" its value to Integer or
	 * Double.
	 * 
	 * @param argument        object that is applied over value stored in
	 *                        valueWrapper
	 * @param nameOfOperation used to have constructive exception explanations
	 * @return argument but as an integer or double
	 * @throws RuntimeException if argument or value isn't appropriate for
	 *                          arithmetic operation
	 */
	private Object checkForTypes(Object argument, String nameOfOperation) {
		checkForTypeOfArgument(argument, "Cant " + nameOfOperation + " due to type of argument");
		checkForTypeOfArgument(this.value, "Cant " + nameOfOperation + " due to type of stored value");
		if (value == null) {
			this.value = Integer.valueOf(0);
		} else if (value instanceof String) {
			this.value = doString((String) value);
		}
		if (argument == null)
			return Integer.valueOf(0);
		if (argument instanceof String)
			return doString((String) argument);
		return argument;
	}

	/**
	 * Method that transforms string into Integer or Double if possible. if neither
	 * of that is possible, RuntimeException is thrown.
	 * 
	 * @param argument that needs to be transformed into Double or Integer
	 * @return argument in form of Integer of Double
	 * @throws RuntimeException if String cannot be interpreted as neither Integer
	 *                          of DoubleF
	 */
	private Object doString(String argument) {
		try {
			try {
				return Integer.parseInt(argument);
			} catch (NumberFormatException e) {
				return Double.parseDouble(argument);
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("Undefined operation");
		}
	}

	/**
	 * Method representing Addition of passed object and stored value if such
	 * operation is possible. Stored value gets changed
	 * 
	 * @param incValue increment value
	 * @throws RuntimeException if no such operation is possible
	 */
	public void add(Object incValue) {
		value = DoMath(checkForTypes(incValue, "add"), (t1, t2) -> t1 + t2);
	}

	/**
	 * Method representing Substraction of passed object and stored value if such
	 * operation is possible. Stored value gets changed
	 * 
	 * @param decValue decrement value
	 * @throws RuntimeException if no such operation is possible
	 */
	public void subtract(Object decValue) {
		value = DoMath(checkForTypes(decValue, "subtract"), (t1, t2) -> t1 - t2);
	}

	/**
	 * Method representing multiplication of passed object and stored value if such
	 * operation is possible. Stored value gets changed
	 * 
	 * @param mulValue factor value
	 * @throws RuntimeException if no such operation is possible
	 */
	public void multiply(Object mulValue) {
		value = DoMath(checkForTypes(mulValue, "multiply"), (t1, t2) -> t1 * t2);
	}

	/**
	 * Method representing division of passed object and stored value if such
	 * operation is possible. Stored value gets changed. If divisor is null or zero,
	 * i set value to zero.
	 * 
	 * @param divValue divisor value
	 * @throws RuntimeException if no such operation is possible
	 */
	public void divide(Object divValue) {
		if (divValue == null)
			throw new IllegalArgumentException("Can't devide with zero");
		value = DoMath(checkForTypes(divValue, "divide"), (a, b) -> Math.signum(a * b) * Math.abs(a) / (Math.abs(b)));
	}

	/**
	 * Method that takes argument, and does arithmetic operation defined upon them
	 * by Bifunction.
	 * 
	 * @param arg      argument that gets passed as increment,decrement,factor or
	 *                 divisor.F
	 * @param function
	 * @return Object that is is result of aritmetic operation
	 */
	private Object DoMath(Object arg, BiFunction<Double, Double, Double> function) {
		double tempVal = value instanceof Double ? (Double) value : Double.valueOf((Integer) value);
		double tempArg = arg instanceof Double ? (Double) arg : Double.valueOf((Integer) arg);
		double tempSol = function.apply(tempVal, tempArg);
		if (value instanceof Double || arg instanceof Double)
			return (double) tempSol;
		return (int) Math.floor(tempSol);
	}

	/**
	 * Method that compares value stored with object provided.
	 * 
	 * @param other value that is compared to stored value
	 * @return positive number if stored one is bigger, zero if they are the same or
	 *         negative if second one is bigger
	 */
	public int numCompare(Object other) {
		Object tempValue = this.value;
		if (value == null && other != null)
			return -1;
		if (value != null && other == null)
			return 1;
		Object comparason = DoMath(checkForTypes(other, "compare"), (t1, t2) -> t1 - t2);
		this.value = tempValue;
		return comparason instanceof Double ? (int) Math.signum((double) comparason) : (int) comparason;
	}
}