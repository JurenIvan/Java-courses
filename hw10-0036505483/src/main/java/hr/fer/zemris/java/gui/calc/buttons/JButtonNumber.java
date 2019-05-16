package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Class that holds implementation of button that serves as digit button.
 * 
 * @author juren
 *
 */
public class JButtonNumber extends MyJButton {

	private static final long serialVersionUID = 1L;

	/**
	 * variable that stores value of button
	 */
	private int digit;

	/**
	 * constructor that sets value to number and connets it to appropriate
	 * {@link CalcModel}
	 * 
	 * @param digit
	 * @param model
	 */
	public JButtonNumber(int digit, CalcModel model) {

		super(digit + "", model);
		setFont(getFont().deriveFont(30f));
		try {
			addActionListener((e) -> model.insertDigit(digit));
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
	}

	/**
	 * Method that returns value of button
	 * 
	 * @return
	 */
	public int getValueStored() {
		return digit;
	}

}
