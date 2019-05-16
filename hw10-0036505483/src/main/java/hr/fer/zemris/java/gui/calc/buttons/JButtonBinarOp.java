package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.JCheckBoxImpl;
import hr.fer.zemris.java.gui.calc.model.NameListener;

/**
 * Class that represents button that has two {@link DoubleBinaryOperator}
 * 
 * @author juren
 *
 */
public class JButtonBinarOp extends MyJButton implements NameListener {

	private static final long serialVersionUID = 1L;
	/**
	 * private variable used for storing names of buttons
	 */
	private String[] name;
	/**
	 * private variable used for storing first function for which button is
	 * responsible
	 */
	private DoubleBinaryOperator bifunc;
	/**
	 * private variable used for storing inverse function for which button is
	 * responsible
	 */
	private DoubleBinaryOperator bifuncInversed;
	/**
	 * private variable used for storing reference to {@link JCheckBoxImpl} so that
	 * we can change to inverse function if needed
	 */
	private JCheckBoxImpl inv;

	/**
	 * Constructor for class that takes functions, name, reference to
	 * {@link JCheckBoxImpl} and {@link CalcModel} so that button can call all
	 * apropriate methods and insure that operation will be done.
	 * 
	 * @param bifunc         regular function that is called when button is pressed
	 * @param bifuncInversed inverse function that is called when button is pressed
	 * @param nameNormal     name displayed on button
	 * @param nameInversed   inversed name displayed on button
	 * @param inv            reference to {@link JCheckBoxImpl} used to check for
	 *                       change to inverse function
	 * @param model          model of calculator
	 */
	public JButtonBinarOp(DoubleBinaryOperator bifunc, DoubleBinaryOperator bifuncInversed, String nameNormal,
			String nameInversed, JCheckBoxImpl inv, CalcModel model) {
		super(nameNormal, model);

		name = new String[2];
		name[0] = nameNormal;
		name[1] = nameInversed;

		this.inv = inv;
		this.bifunc = bifunc;
		this.bifuncInversed = bifuncInversed;
		try {
			addActionListener((e) -> {
				if (model.isActiveOperandSet()) {
					double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(),
							model.getValue());
					model.setValue(result);
					model.clearActiveOperand();
				}
				model.setActiveOperand(model.getValue());
				model.setPendingBinaryOperation(chooseOperation());
				model.clear();
			});
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
		inv.addListener(this);

	}

	/**
	 * Constructor that duplicates given values and delegates to another constructor
	 * 
	 * @param bifunc     regular function that is called when button is pressed
	 * @param nameNormal name displayed on button
	 * @param inv        reference to {@link JCheckBoxImpl} used to check for change
	 *                   to inverse function
	 * @param model      model of calculator
	 */
	public JButtonBinarOp(DoubleBinaryOperator bifunc, String nameNormal, JCheckBoxImpl inv, CalcModel model) {
		this(bifunc, bifunc, nameNormal, nameNormal, inv, model);
	}

	/**
	 * method that is basically used as ternary operator. returns normal or inverse
	 * function
	 * 
	 * @return
	 */
	private DoubleBinaryOperator chooseOperation() {
		if (inv.isSelected())
			return bifunc;
		return bifuncInversed;
	}

	/**
	 * method used to switch to another name
	 */
	private void changeText() {
		if (inv.isSelected()) {
			setText(name[1]);
		} else {
			setText(name[0]);
		}
	}

	@Override
	public void valueChanged() {
		changeText();
	}

}
