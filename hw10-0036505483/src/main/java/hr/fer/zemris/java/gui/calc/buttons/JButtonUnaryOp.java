package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.Function;

import javax.swing.JOptionPane;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.JCheckBoxImpl;
import hr.fer.zemris.java.gui.calc.model.NameListener;

/**
 * Class that represents button that has two {@link Function} and is able to
 * perform operations upon it.
 * 
 * @author juren
 *
 */
public class JButtonUnaryOp extends MyJButton implements NameListener {

	private static final long serialVersionUID = 1L;
	/**
	 * private variable used for storing name of button
	 */
	private String[] name;
	/**
	 * private variable used for storing first function for which button is
	 * responsible
	 */
	private Function<Double, Double> func;
	/**
	 * private variable used for storing inverse function for which button is
	 * responsible
	 */
	private Function<Double, Double> funcInversed;
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
	 * @param func         regular function that is called when button is pressed
	 * @param funcInversed inverse function that is called when button is pressed
	 * @param nameNormal   name displayed on button
	 * @param nameInversed inversed name displayed on button
	 * @param inv          reference to {@link JCheckBoxImpl} used to check for
	 *                     change to inverse function
	 * @param model        model of calculator
	 */
	public JButtonUnaryOp(Function<Double, Double> func, Function<Double, Double> funcInversed, String nameNormal,
			String nameInversed, JCheckBoxImpl inv, CalcModel model) {
		super(nameNormal, model);

		name = new String[2];
		name[0] = nameNormal;
		name[1] = nameInversed;

		this.inv = inv;
		this.func = func;
		this.funcInversed = funcInversed;

		addActionListener((e) -> {
			try {
				double result = calculate(model.getValue());
				model.clear();
				model.setValue(result);
			} catch (CalculatorInputException | IllegalStateException | IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(null,"Error occured! Ignoring command! " + e1.getMessage());
			}
			;
		});

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
	public JButtonUnaryOp(Function<Double, Double> func, String nameNormal, JCheckBoxImpl inv, CalcModel model) {
		this(func, func, nameNormal, nameNormal, inv, model);
	}

	/**
	 * Method that takes argument and applies function upon it.
	 * 
	 * @param argument upon a function is applied
	 * @return result of operation
	 */
	public double calculate(double argument) {
		if (inv.isSelected())
			return funcInversed.apply(argument);
		return func.apply(argument);
	}

	/**
	 * method used to switch to another name
	 */
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
