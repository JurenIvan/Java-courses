package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.Function;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.JRadioButtonImpl;
import hr.fer.zemris.java.gui.calc.model.NameListener;

public class JButtonUnaryOp extends MyJButton implements NameListener {

	private static final long serialVersionUID = 1L;

	private String[] name;
	private Function<Double, Double> func;
	private Function<Double, Double> funcInversed;
	private JRadioButtonImpl inv;

	public JButtonUnaryOp(Function<Double, Double> func, Function<Double, Double> funcInversed, String nameNormal,
			String nameInversed, JRadioButtonImpl inv, CalcModel model) {
		super(nameNormal, model);

		name = new String[2];
		name[0] = nameNormal;
		name[1] = nameInversed;

		this.inv = inv;
		this.func = func;
		this.funcInversed = funcInversed;
		try {
			addActionListener((e) -> {
				double result = calculate(model.getValue());
				model.clear();
				model.setValue(result);
			});
		} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e) {

		}
		inv.addListener(this);

	}

	public JButtonUnaryOp(Function<Double, Double> func, String nameNormal, JRadioButtonImpl inv, CalcModel model) {
		this(func, func, nameNormal, nameNormal, inv, model);
	}

	public double calculate(double argument) {
		if (inv.isSelected())
			return funcInversed.apply(argument);
		return func.apply(argument);
	}

	public void changeText() {
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
