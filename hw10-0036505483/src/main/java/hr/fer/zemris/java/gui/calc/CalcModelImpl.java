package hr.fer.zemris.java.gui.calc;

import java.util.HashSet;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class CalcModelImpl implements CalcModel {
	/**
	 * private variable used for storing flag whether calculator is currenlty editable or not
	 */
	private boolean editable;
	/**
	 * private variable used for storing boolean flag representing whether number is negative or not
	 */
	private boolean isNegative;
	/**
	 * private variable used for storing string representation of value
	 */
	private String currValueString;
	/**
	 * private variable used for storing numerical represetation of valueF
	 */
	private double currValue;
	/**
	 * private variable used for storing active operand
	 */
	private double activeOperand;
	/**
	 * private variable used for storing boolean flag representing whether operator is set
	 */
	private boolean activeOperatorSet;
	/**
	 * private variable used for storing operation that will take place upon numbers
	 */
	private DoubleBinaryOperator pendingOperation;
	/**
	 * private variable used for storing listeners
	 */
	private Set<CalcValueListener> listeners;

	/**
	 * Standard constructor
	 */
	public CalcModelImpl() {
		currValueString = "";
		editable = true;
		isNegative = false;
		activeOperatorSet = false;
		currValue = 0;

		activeOperand = 0;
		pendingOperation = null;
		listeners = new HashSet<>();
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return currValue * (isNegative ? (-1) : 1);
	}

	@Override
	public void setValue(double value) {
		if (value < 0) {
			value = value * (-1);
			isNegative = true;
		} else {
			isNegative = false;
		}

		this.currValue = value;
		currValueString = String.valueOf(value);
		editable = false;
		notifyObservers();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		currValue = 0;
		isNegative = false;
		currValueString = "";
		editable = true;
		notifyObservers();
	}

	@Override
	public void clearAll() {
		clear();
		activeOperatorSet = false;
		activeOperand = 0;
		pendingOperation = null;
		notifyObservers();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException("Illegal input. You may restart.");
		}
		this.isNegative = !isNegative;
		notifyObservers();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable)
			throw new CalculatorInputException("Illegal input. You may restart.");
		if (currValueString.contains("."))
			throw new CalculatorInputException("Illegal input. You may restart.");
		if (currValueString.length() == 0)
			throw new CalculatorInputException("Illegal input. You may restart.");

		currValueString = currValueString + '.';
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (digit < 0 || digit > 9)
			throw new IllegalArgumentException("Not a digit");
		if (!editable)
			throw new CalculatorInputException("Calculator is not editable at the moment! You may restart ");
		try {
			currValue = Double.parseDouble(currValueString + digit);
		} catch (NumberFormatException e) {
			throw new CalculatorInputException("Can not parse that number");
		}
		if (currValue == Double.POSITIVE_INFINITY) {
			throw new CalculatorInputException("Too big number");
		}

		if (currValueString.isBlank()) {
			currValueString = digit + "";
		} else if (currValueString.equals("0") && digit != 0) {
			currValueString = digit + "";
		} else if (currValueString.equals("0") && digit == 0) {

		} else {
			currValueString = currValueString + digit;
		}
		notifyObservers();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperatorSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!activeOperatorSet)
			throw new IllegalStateException("No active operand for now.");
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		if (activeOperatorSet) {
			throw new CalculatorInputException("Operand is already set.");
		}
		this.activeOperand = activeOperand;
		activeOperatorSet = true;
	}

	@Override
	public void clearActiveOperand() {
		this.activeOperatorSet = false;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
	}

	@Override
	public String toString() {
		String output = isNegative ? "-" : "";
		if (Double.compare(currValue, Double.NaN) == 0) {
			return "NaN";
		}
		if (currValueString.isBlank())
			return output + '0';
		return output + currValueString;
	}

	/**
	 * Method used to notify registered Observers
	 */
	private void notifyObservers() {
		for (var observer : listeners) {
			observer.valueChanged(this);
		}
	}

}
