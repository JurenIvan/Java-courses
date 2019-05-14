package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.Consumer;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

public class JButtonSpecial extends MyJButton {

	private static final long serialVersionUID = 1L;

	public JButtonSpecial(Consumer<CalcModel> consumer, String nameNormal, CalcModel model) {
		super(nameNormal, model);

		try {
			addActionListener((e) -> {
				consumer.accept(model);
			});
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
	}

}
