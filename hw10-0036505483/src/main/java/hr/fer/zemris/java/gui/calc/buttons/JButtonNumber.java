package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class JButtonNumber extends MyJButton {

	private static final long serialVersionUID = 1L;

	private int digit;

	public JButtonNumber(int digit, CalcModel model) {
		super(digit + "", model);
		try {
			addActionListener((e) -> model.insertDigit(digit));
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
	}

	public int getValueStored() {
		return digit;
	}

}
