package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.JRadioButtonImpl;
import hr.fer.zemris.java.gui.calc.model.NameListener;

public class JButtonBinarOp extends MyJButton implements NameListener {

	private static final long serialVersionUID = 1L;

	private String[] name;
	private DoubleBinaryOperator bifunc;
	private DoubleBinaryOperator bifuncInversed;
	private JRadioButtonImpl inv;

	public JButtonBinarOp(DoubleBinaryOperator bifunc, DoubleBinaryOperator bifuncInversed, String nameNormal,
			String nameInversed, JRadioButtonImpl inv, CalcModel model) {
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

	public JButtonBinarOp(DoubleBinaryOperator bifunc, String nameNormal, JRadioButtonImpl inv, CalcModel model) {
		this(bifunc, bifunc, nameNormal, nameNormal, inv, model);
	}

	public DoubleBinaryOperator chooseOperation() {
		if (inv.isSelected())
			return bifunc;
		return bifuncInversed;
	}

	public String getName(int i) {
		return name[i];
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
