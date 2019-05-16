package hr.fer.zemris.java.gui.calc.buttons;

import java.util.EmptyStackException;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Class that represents button that has {@link Consumer}
 * 
 * @author juren
 *
 */

public class JButtonSpecial extends MyJButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for button that can consume given set of commands. also sets name
	 * and model of calculator
	 * 
	 * @param consumer set of command that are executed when button is pressed
	 * @param nameNormal name of button
	 * @param model model of calculator
	 */
	public JButtonSpecial(Consumer<CalcModel> consumer, String nameNormal, CalcModel model) {
		super(nameNormal, model);

		try {
			addActionListener((e) -> {
				try {
				consumer.accept(model);
				}catch (CalculatorInputException | IllegalStateException | IllegalArgumentException | EmptyStackException e1 ) {
					JOptionPane.showMessageDialog(null,"Error occured! Ignoring command! " + e1.getMessage());
				}
			});
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
	}

}
