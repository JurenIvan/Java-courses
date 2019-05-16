package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.buttons.JButtonBinarOp;
import hr.fer.zemris.java.gui.calc.buttons.JButtonNumber;
import hr.fer.zemris.java.gui.calc.buttons.JButtonSpecial;
import hr.fer.zemris.java.gui.calc.buttons.JButtonUnaryOp;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.JLabelImpl;
import hr.fer.zemris.java.gui.calc.model.JCheckBoxImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * Class that has main method and holds implementation of basic windows 95 like
 * calculator.
 * 
 * @author juren
 *
 */
public class Calculator extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor that calls methods used to set up GUI
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	/**
	 * Method used to clear up the constructor.Contains commands to configure GUI.
	 */
	private void initGUI() {
		Stack<Double> stack = new Stack<Double>();
		CalcModel model = new CalcModelImpl();
		JLabelImpl display = new JLabelImpl();
		JCheckBoxImpl jrb = new JCheckBoxImpl();

		model.addCalcValueListener(display);
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(5));
		cp.add(jrb, "5,7");
		cp.add(display, "1,1");
		jrb.setText("Inv");

		// display setup
		displaySetup(display);
		// digits
		configureDigitsButtons(cp, model);
		// unary operators
		configureUnaryOperators(cp, model, jrb);
		// binary
		configureBinaryOperators(cp, model, jrb);
		// special
		configureSpecialButtons(cp, model, stack);
	}

	/**
	 * Method used to clear up method initGUI. Its function is to configure buttons
	 * such as = push pop clr reset etc.
	 * 
	 * @param cp    Container of calculator
	 * @param model Implementation of Calculator model interface
	 * @param stack used to enable push and pop to calculator
	 */
	private void configureSpecialButtons(Container cp, CalcModel model, Stack<Double> stack) {
		try {
		cp.add(new JButtonSpecial((t) -> {
			double result = t.getPendingBinaryOperation().applyAsDouble(t.getActiveOperand(), t.getValue());
			t.setValue(result);
			t.clearActiveOperand();
			t.setPendingBinaryOperation(null);
		}, "=", model), "1,6");
		cp.add(new JButtonSpecial((t) -> model.swapSign(), "+/-", model), "5,4");
		cp.add(new JButtonSpecial((t) -> model.clear(), "clr", model), "1,7");
		cp.add(new JButtonSpecial((t) -> model.clearAll(), "res", model), "2,7");
		cp.add(new JButtonSpecial((t) -> model.insertDecimalPoint(), ".", model), "5,5");
		cp.add(new JButtonSpecial((t) -> stack.add(model.getValue()), "push", model), "3,7");
		cp.add(new JButtonSpecial((t) -> model.setValue(stack.pop()), "pop", model), "4,7");
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,"error occured! " + e.getMessage());
		}
	}

	/**
	 * Method used to set up display of calculator
	 * 
	 * @param display JLabel upon actions are performed
	 */
	private void displaySetup(JLabelImpl display) {
		display.setEnabled(false);
		display.setText("0");
		display.setBackground(Color.yellow);
		display.setOpaque(true);
	}

	/**
	 * Method used to clear up method initGUI. Its function is to configure buttons
	 * such as * / - +
	 * 
	 * @param cp    Container of calculator
	 * @param model Implementation of Calculator model interface
	 * @param jrb   its a checkbox what determines whether normal or inverse
	 *              function is needed
	 */
	private void configureBinaryOperators(Container cp, CalcModel model, JCheckBoxImpl jrb) {
		cp.add(new JButtonBinarOp((a, b) -> a / b, "/", jrb, model), "2,6");
		cp.add(new JButtonBinarOp((a, b) -> a * b, "*", jrb, model), "3,6");
		cp.add(new JButtonBinarOp((a, b) -> a - b, "-", jrb, model), "4,6");
		cp.add(new JButtonBinarOp((a, b) -> a + b, "+", jrb, model), "5,6");
		cp.add(new JButtonBinarOp((a, b) -> Math.pow(a, 1 / b), (a, b) -> Math.pow(a, b), "x^n", "x^(1/n)", jrb, model),
				"5,1");
	}

	/**
	 * Method used to clear up method initGUI. Its function is to configure buttons
	 * such as sin cos pow etc
	 * 
	 * @param cp    Container of calculator
	 * @param model Implementation of Calculator model interface
	 * @param jrb   its a checkbox what determines whether normal or inverse
	 *              function is needed
	 */
	private void configureUnaryOperators(Container cp, CalcModel model, JCheckBoxImpl jrb) {
		cp.add(new JButtonUnaryOp((a) -> 1 / a, "1/x", jrb, model), "2,1");
		cp.add(new JButtonUnaryOp((a) -> Math.sin(a), (a) -> Math.asin(a), "sin", "arcsin", jrb, model), "2,2");
		cp.add(new JButtonUnaryOp((a) -> Math.log10(a), (a) -> Math.pow(10, a), "log", "10^x", jrb, model), "3,1");
		cp.add(new JButtonUnaryOp((a) -> Math.cos(a), (a) -> Math.acos(a), "cos", "arccos", jrb, model), "3,2");
		cp.add(new JButtonUnaryOp((a) -> Math.log(a), (a) -> Math.pow(Math.E, a), "ln", "e^x", jrb, model), "4,1");
		cp.add(new JButtonUnaryOp((a) -> Math.tan(a), (a) -> Math.atan(a), "tan", "atan", jrb, model), "4,2");
		cp.add(new JButtonUnaryOp((a) -> 1 / Math.tan(a), (a) -> Math.PI / 2 - Math.atan(a), "ctg", "arcctg", jrb,
				model), "5,2");
	}

	/**
	 * Method used to clear up method initGUI. Its function is to configure buttons
	 * that represent digits
	 * 
	 * @param cp    Container of calculator
	 * @param model Implementation of Calculator model interface
	 */
	private void configureDigitsButtons(Container cp, CalcModel model) {
		cp.add(new JButtonNumber(0, model), "5,3");
		cp.add(new JButtonNumber(1, model), "4,3");
		cp.add(new JButtonNumber(2, model), "4,4");
		cp.add(new JButtonNumber(3, model), "4,5");
		cp.add(new JButtonNumber(4, model), "3,3");
		cp.add(new JButtonNumber(5, model), "3,4");
		cp.add(new JButtonNumber(6, model), "3,5");
		cp.add(new JButtonNumber(7, model), "2,3");
		cp.add(new JButtonNumber(8, model), "2,4");
		cp.add(new JButtonNumber(9, model), "2,5");
	}

	/**
	 * Main method used to start the program-calculator
	 * @param args not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}